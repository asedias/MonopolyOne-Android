package com.asedias.monopolyone.util

import com.asedias.monopolyone.model.websocket.AuthMessage
import com.asedias.monopolyone.model.websocket.EventMessage
import com.asedias.monopolyone.model.websocket.StatusMessage
import okhttp3.Response

sealed class SocketMessage {
    data class Event(val data: EventMessage) : SocketMessage()
    data class Status(val data: StatusMessage) : SocketMessage()
    data class Auth(val data: AuthMessage) : SocketMessage()
    data class Error(val t: Throwable, val response: Response?) : SocketMessage()
}
