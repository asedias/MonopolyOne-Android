package com.asedias.monopolyone.util

sealed class WSState {
    object Open : WSState()
    object Connected : WSState()
    data class Failure(val t: Throwable) : WSState()
    data class Closed(val reason: String) : WSState()
}
