package com.asedias.monopolyone.domain.model

sealed class Response<T: Any> {
    data class Success<T: Any>(val data: T): Response<T>()
    data class Error<T: Any>(val code: Int = 0): Response<T>()
}

data class DataResponse<T: Any>(
    val code: Int,
    val data: T,
)

data class ErrorResponse(
    val code: Int,
    val description: String,
)

data class ListResponse<T: Any>(
    val code: Int,
    val `data`: List<T>,
)
