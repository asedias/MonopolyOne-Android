package com.asedias.monopolyone.api

import android.util.Log
import com.asedias.monopolyone.model.websocket.AuthMessage
import com.asedias.monopolyone.model.websocket.EventMessage
import com.asedias.monopolyone.model.websocket.StatusMessage
import com.asedias.monopolyone.util.Constants
import com.asedias.monopolyone.util.SessionManager
import com.asedias.monopolyone.util.SocketMessage
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
    var authMessage: AuthMessage? = null

    @OptIn(DelicateCoroutinesApi::class)
    private var listener = object : WebSocketListener() {

        override fun onOpen(webSocket: WebSocket, response: Response) {
            Log.i(Constants.TAG_WEB_SOCKET, "WebSocket open on $WS_URL")
            GlobalScope.launch {
                state.emit(SocketState.Open)
            }
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            Log.i(Constants.TAG_WEB_SOCKET, "WebSocket failure: ${t.localizedMessage}")
            GlobalScope.launch {
                state.emit(SocketState.Failure(t))
            }
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            if (text == "2") {
                return keepAlive()
            }
            when (val type = text.subSequence(0, 5)) {
                "4auth" -> {
                    val obj = Gson().fromJson(text.drop(5), AuthMessage::class.java)
                    authMessage = obj
                    GlobalScope.launch {
                        state.emit(SocketState.Authenticated)
                        _channel.send(SocketMessage.Auth(obj))
                    }
                }
                "4stat" -> {
                    val obj = Gson().fromJson(text.drop(7), StatusMessage::class.java)
                    GlobalScope.launch {
                        _channel.send(SocketMessage.Status(obj))
                    }
                }
                "4even" -> {
                    val obj = Gson().fromJson(text.drop(7), EventMessage::class.java)
                    GlobalScope.launch {
                        _channel.send(SocketMessage.Event(obj))
                    }
                }
            }
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            Log.i(Constants.TAG_WEB_SOCKET, "WebSocket closed: $code@$reason")
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
        if (SessionManager.isUserLogged())
            "$WS_URL&access_token=${SessionManager.getAccessToken()}"
        else WS_URL

    private fun buildRequest() = Request.Builder()
        .url(buildUrl())
        .build()

    fun cancel() {
        webSocket?.cancel()
    }

}