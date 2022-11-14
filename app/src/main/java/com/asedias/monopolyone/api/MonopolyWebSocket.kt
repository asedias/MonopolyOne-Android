package com.asedias.monopolyone.api

import com.asedias.monopolyone.model.websocket.AuthMessage
import com.asedias.monopolyone.model.websocket.EventMessage
import com.asedias.monopolyone.model.websocket.StatusMessage
import com.asedias.monopolyone.util.AuthData
import com.asedias.monopolyone.util.WSMessage
import com.google.gson.Gson
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import okhttp3.*

class MonopolyWebSocket {

    private var webSocket: WebSocket? = null
    private var observer: (type: WSMessage) -> Unit? = {}

    @OptIn(DelicateCoroutinesApi::class)
    private var listener = object : WebSocketListener() {
        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            observer.invoke(WSMessage.Error(t, response))
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            if(text == "2") {
                webSocket.send("3")
                return
            }
            when (val type = text.subSequence(0, 5)) {
                "4auth" -> {
                    val obj = Gson().fromJson(text.drop(5), AuthMessage::class.java)
                    //observer.invoke(WSMessage.Auth(obj))
                    GlobalScope.launch {
                        _channel.send(WSMessage.Auth(obj))
                    }
                }
                "4stat" -> {
                    val obj = Gson().fromJson(text.drop(7), StatusMessage::class.java)
                    //observer.invoke(WSMessage.Status(obj))
                    GlobalScope.launch {
                        _channel.send(WSMessage.Status(obj))
                    }
                }
                "4even" -> {
                    val obj = Gson().fromJson(text.drop(7), EventMessage::class.java)
                    //observer.invoke(WSMessage.Event(obj))
                    GlobalScope.launch {
                        _channel.send(WSMessage.Event(obj))
                    }
                }
            }
        }
    }

    companion object {
        const val WS_URL = "wss://monopoly-one.com/ws?subs=rooms"

        private val _channel = Channel<WSMessage>()
        var channel = _channel.receiveAsFlow()
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