package com.asedias.monopolyone.util

sealed class SocketState {
    object Open : SocketState()
    object Authenticated : SocketState()
    data class Failure(val t: Throwable) : SocketState()
    data class Closed(val reason: String) : SocketState()
}
