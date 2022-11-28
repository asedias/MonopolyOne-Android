package com.asedias.monopolyone.domain.websocket

import com.asedias.monopolyone.domain.model.websocket.AuthMessage
import com.asedias.monopolyone.domain.model.websocket.EventMessage
import com.asedias.monopolyone.domain.model.websocket.StatusMessage
import okhttp3.Response

sealed class WebSocketMessage {
    data class Event(val data: EventMessage) : WebSocketMessage()
    data class Status(val data: StatusMessage) : WebSocketMessage()
    data class Auth(val data: AuthMessage) : WebSocketMessage()
    data class Unknown(val text: String) : WebSocketMessage()
}


