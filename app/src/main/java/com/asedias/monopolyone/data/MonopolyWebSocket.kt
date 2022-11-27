package com.asedias.monopolyone.data

import android.util.Log
import com.asedias.monopolyone.domain.model.websocket.SocketMessage
import com.asedias.monopolyone.util.SocketState
import com.google.gson.Gson
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import okhttp3.*

object MonopolyWebSocket {

    private const val WS_URL = "wss://monopoly-one.com/ws?subs=rooms"

    val state = MutableSharedFlow<SocketState>()

    private val _channel = Channel<SocketMessage>()

    val channel by lazy {
        if(webSocket == null) webSocket = client.newWebSocket(buildRequest(), listener)
        _channel.receiveAsFlow()
    }

    private val client by lazy {
        OkHttpClient.Builder()
            .build()
    }

    private var webSocket: WebSocket? = null
    var authMessage: com.asedias.monopolyone.domain.model.websocket.AuthMessage? = null

    @OptIn(DelicateCoroutinesApi::class)
    private var listener = object : WebSocketListener() {

        override fun onOpen(webSocket: WebSocket, response: Response) {
            Log.i(javaClass.name, "WebSocket open on $WS_URL")
            GlobalScope.launch {
                state.emit(SocketState.Open)
            }
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            Log.i(javaClass.name, "WebSocket failure: ${t.localizedMessage}")
            GlobalScope.launch {
                state.emit(SocketState.Failure(t))
            }
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            //Log.d(javaClass.name, text)
            if (text == "2") {
                return keepAlive()
            }
            when (text.subSequence(0, 5)) {
                "4auth" -> {
                    val obj = Gson().fromJson(text.drop(5), com.asedias.monopolyone.domain.model.websocket.AuthMessage::class.java)
                    authMessage = obj
                    GlobalScope.launch {
                        state.emit(SocketState.Authenticated)
                        _channel.send(SocketMessage.Auth(obj))
                    }
                }
                "4stat" -> {
                    val obj = Gson().fromJson(text.drop(7), com.asedias.monopolyone.domain.model.websocket.StatusMessage::class.java)
                    GlobalScope.launch {
                        _channel.send(SocketMessage.Status(obj))
                    }
                }
                "4even" -> {
                    val obj = Gson().fromJson(text.drop(7), com.asedias.monopolyone.domain.model.websocket.EventMessage::class.java)
                    GlobalScope.launch {
                        _channel.send(SocketMessage.Event(obj))
                    }
                }
            }
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            Log.i(javaClass.name, "WebSocket closed: $code@$reason")
            GlobalScope.launch {
                state.emit(SocketState.Closed(reason))
            }
        }
    }

    fun start() {
        webSocket = client.newWebSocket(buildRequest(), listener)
    }

    fun reconnect() {
        cancel()
        webSocket = client.newWebSocket(buildRequest(), listener)
    }

    fun keepAlive() {
        webSocket?.send("3")
    }

    private fun buildUrl() =
        // TODO inject SessionManager
        /*if (SessionManager.isUserLogged())
            "$WS_URL&access_token=${SessionManager.getAccessToken()}"
        else*/ WS_URL

    private fun buildRequest() = Request.Builder()
        .url(buildUrl())
        .build()

    fun cancel() {
        webSocket?.cancel()
    }

}