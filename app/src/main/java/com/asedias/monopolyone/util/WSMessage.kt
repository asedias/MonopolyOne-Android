package com.asedias.monopolyone.util

import com.asedias.monopolyone.model.websocket.AuthMessage
import com.asedias.monopolyone.model.websocket.EventMessage
import com.asedias.monopolyone.model.websocket.StatusMessage
import okhttp3.Response

sealed class WSMessage {
    data class Event(val data: EventMessage) : WSMessage()
    data class Status(val data: StatusMessage) : WSMessage()
    data class Auth(val data: AuthMessage) : WSMessage()
    data class Error(val t: Throwable, val response: Response?) : WSMessage()
}
