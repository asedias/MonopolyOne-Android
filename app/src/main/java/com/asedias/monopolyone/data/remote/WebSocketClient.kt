package com.asedias.monopolyone.data.remote

import com.asedias.monopolyone.MonopolyApp
import com.asedias.monopolyone.data.repository.AuthRepositoryImpl
import com.asedias.monopolyone.domain.model.websocket.AuthMessage
import com.asedias.monopolyone.domain.model.websocket.EventMessage
import com.asedias.monopolyone.domain.model.websocket.StatusMessage
import com.asedias.monopolyone.domain.websocket.WebSocketClient
import com.asedias.monopolyone.domain.websocket.WebSocketMessage
import com.asedias.monopolyone.domain.websocket.WebSocketState
import com.google.gson.Gson
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.*
import javax.inject.Inject

class WebSocketClient @Inject constructor(
    private val client: OkHttpClient,
    val authRepositoryImpl: AuthRepositoryImpl,
    val app: MonopolyApp
) : WebSocketClient {

    private val BASE_URL = "wss://monopoly-one.com/ws" + "?subs=rooms"
    var reconnectOnFailure = false

    private var currentSocket: WebSocket? = null

    private val _state = MutableStateFlow<WebSocketState>(WebSocketState.Unconnected)

    override suspend fun connect() {
        authRepositoryImpl.loadFromLocal().first()
        if (currentSocket != null) return
        currentSocket = client.newWebSocket(buildRequest(), object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                app.applicationScope.launch { _state.emit(WebSocketState.Open) }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                app.applicationScope.launch { _state.emit(WebSocketState.Failure(t)) }
                if (reconnectOnFailure) reconnect()
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                app.applicationScope.launch { _state.emit(WebSocketState.Closed(code, reason)) }
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                app.applicationScope.launch {
                    val message = handleMessage(text)
                    _state.emit(WebSocketState.Connected(message))
                }
            }
        })
    }

    fun reconnect() {
        disconnect()
        app.applicationScope.launch { connect() }
    }

    override suspend fun handleMessage(message: String): WebSocketMessage {
        if (message == "2") {
            sendMessage("3")
        }
        if (message.startsWith("4auth")) {
            val authMessage = Gson().fromJson(message.drop(5), AuthMessage::class.java)
            return WebSocketMessage.Auth(authMessage)
        }
        if (message.startsWith("4status")) {
            val statusMessage = Gson().fromJson(message.drop(7), StatusMessage::class.java)
            return WebSocketMessage.Status(statusMessage)
        }
        if (message.startsWith("4events")) {
            val eventMessage = Gson().fromJson(message.drop(7), EventMessage::class.java)
            return WebSocketMessage.Event(eventMessage)
        }
        return WebSocketMessage.Unknown(message)
    }

    override fun disconnect() {
        currentSocket?.close(1000, "Calling disconnect")
        currentSocket = null
    }

    override fun getMessages(): Flow<WebSocketMessage> =
        flow {
            if (currentSocket == null) connect()
            emitAll(_state.filter { it is WebSocketState.Connected }.map {
                (it as WebSocketState.Connected).message
            })
        }

    override suspend fun getState(): MutableStateFlow<WebSocketState> {
        if(currentSocket == null) connect()
        return _state
    }

    override suspend fun sendMessage(message: String) {
        currentSocket?.send(message)
    }

    private fun buildUrl() =
        if (authRepositoryImpl.currentSession.user_id > 0)
            "$BASE_URL&access_token=${authRepositoryImpl.currentSession.access_token}"
        else BASE_URL

    private fun buildRequest() = Request.Builder()
        .url(buildUrl())
        .build()
}