package com.asedias.monopolyone.api

import com.asedias.monopolyone.model.websocket.AuthMessage
import com.asedias.monopolyone.model.websocket.EventMessage
import com.asedias.monopolyone.model.websocket.StatusMessage
import com.asedias.monopolyone.util.AuthData
import com.asedias.monopolyone.util.WSMessage
import com.asedias.monopolyone.util.WSState
import com.google.gson.Gson
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import okhttp3.*

class MonopolyWebSocket {

    private var webSocket: WebSocket? = null

    @OptIn(DelicateCoroutinesApi::class)
    private var listener = object : WebSocketListener() {

        override fun onOpen(webSocket: WebSocket, response: Response) {
            GlobalScope.launch {
                state.emit(WSState.Open)
            }
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            GlobalScope.launch {
                state.emit(WSState.Failure(t))
            }
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            if (text == "2") {
                return keepAlive()
            }
            when (val type = text.subSequence(0, 5)) {
                "4auth" -> {
                    val obj = Gson().fromJson(text.drop(5), AuthMessage::class.java)
                    GlobalScope.launch {
                        _channel.send(WSMessage.Auth(obj))
                    }
                }
                "4stat" -> {
                    val obj = Gson().fromJson(text.drop(7), StatusMessage::class.java)
                    GlobalScope.launch {
                        _channel.send(WSMessage.Status(obj))
                    }
                }
                "4even" -> {
                    val obj = Gson().fromJson(text.drop(7), EventMessage::class.java)
                    GlobalScope.launch {
                        _channel.send(WSMessage.Event(obj))
                    }
                }
            }
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            GlobalScope.launch {
                state.emit(WSState.Closed(reason))
            }
        }
    }

    companion object {
        const val WS_URL = "wss://monopoly-one.com/ws?subs=rooms"

        private val _channel = Channel<WSMessage>()
        var channel = _channel.receiveAsFlow()

        val state = MutableSharedFlow<WSState>()
    }

    fun keepAlive() {
        webSocket?.send("3")
    }

    fun start() {
        val req = Request.Builder()
            .url("$WS_URL&access_token=${AuthData.accessToken}")
            .build()
        val client = OkHttpClient.Builder()
            .build()
        webSocket = client.newWebSocket(req, listener)
    }

    fun cancel() {
        webSocket!!.cancel()
    }
}