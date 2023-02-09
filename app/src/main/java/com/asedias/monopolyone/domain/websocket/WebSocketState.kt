package com.asedias.monopolyone.domain.websocket

sealed class WebSocketState {
    object Unconnected : WebSocketState()
    object Open : WebSocketState()
    data class Connected(val message: WebSocketMessage) : WebSocketState()
    data class Failure(val t: Throwable) : WebSocketState()
    data class Closed(val code: Int, val reason: String) : WebSocketState()
}
