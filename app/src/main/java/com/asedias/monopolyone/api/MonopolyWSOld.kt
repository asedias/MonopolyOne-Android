package com.asedias.monopolyone.api

import androidx.lifecycle.MutableLiveData
import com.asedias.monopolyone.model.ErrorResponse
import com.asedias.monopolyone.model.websocket.AuthMessage
import com.asedias.monopolyone.model.websocket.EventMessage
import com.asedias.monopolyone.model.websocket.StatusMessage
import com.asedias.monopolyone.util.AuthData
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableSharedFlow
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor

class MonopolyWSOld() : WebSocketListener() {

    private lateinit var webSocket: WebSocket

    private val request by lazy {
        Request.Builder()
            .url("wss://monopoly-one.com/ws?subs=rooms&access_token=${AuthData.accessToken}")
            .build()
    }
    private val client by lazy {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            //.addInterceptor(logging)
            .build()
    }

    private val gson by lazy { Gson() }

    private lateinit var callback: (type: Any) -> Unit

    fun startEcho() {
        webSocket = client.newWebSocket(request, this)
    }

    fun startEcho(call: (type: Any) -> Unit) : MonopolyWSOld {
        startEcho()
        callback = call
        return this
    }

    fun cancel() {
        webSocket.cancel()
    }

    companion object {
        val events = MutableLiveData<String>()
        val auth = MutableLiveData<String>()
        val status = MutableSharedFlow<StatusMessage>()
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        println("WS: Open ${webSocket.request().url}")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        callback.invoke(ErrorResponse(99, ""))
        t.printStackTrace()
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
        println("WS: Closed ${code}:${reason}")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        if(text == "2") webSocket.send("3") //??
        if(text.startsWith("4events")) {
            val event = gson.fromJson(text.drop(7), EventMessage::class.java)
            //println("WS: Receive Event ${event.events[0].type}@${event.events[0].room_id}")
            callback.invoke(event)
        }
        if(text.startsWith("4status")) {
            val status = gson.fromJson(text.drop(7), StatusMessage::class.java)
            //println("WS: Receive Status ${status.status.time}")
            callback.invoke(status)
        }
        if(text.startsWith("4auth")) {
            val auth = gson.fromJson(text.drop(5), AuthMessage::class.java)
            //println("WS: Receive Auth ${auth.user_data.nick}")
            AuthData.observableAuthMessage.postValue(auth)
        }
    }
}
