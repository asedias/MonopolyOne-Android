package com.asedias.monopolyone.model

data class DataResponse<T: Any>(
    val code: Int = -1,
    val data: T? = null,
)