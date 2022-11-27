package com.asedias.monopolyone.ui

sealed class UIState<T> {
    data class Loading<T>(val temp: Int = 0) : UIState<T>()
    data class Error<T>(val code: Int) : UIState<T>()
    data class Show<T>(val data: T) : UIState<T>()
    data class Update<T>(val data: T) : UIState<T>()
}
