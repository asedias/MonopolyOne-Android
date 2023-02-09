package com.asedias.monopolyone.domain.model

//Name conflict with okHttp3.Response
sealed class ResponseState<T : Any> {
    data class Nothing<T : Any>(val code: Int = 0) : ResponseState<Any>()
    data class Success<T : Any>(val data: T) : ResponseState<T>()
    data class Error<T : Any>(val code: Int = 0) : ResponseState<T>()
}

data class DataResponse<T : Any>(
    val code: Int,
    val data: T,
)

data class ErrorResponse(
    val code: Int,
    val description: String,
)
