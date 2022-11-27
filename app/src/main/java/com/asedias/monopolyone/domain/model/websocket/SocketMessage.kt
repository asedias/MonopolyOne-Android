package com.asedias.monopolyone.domain.model.websocket

import okhttp3.Response

sealed class SocketMessage {
    data class Event(val data: EventMessage) : SocketMessage()
    data class Status(val data: StatusMessage) : SocketMessage()
    data class Auth(val data: AuthMessage) : SocketMessage()
    data class Error(val t: Throwable, val response: Response?) : SocketMessage()
}
