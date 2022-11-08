package com.example.material3test.model

data class DataResponse<T: Any>(
    val loading: Boolean = false,
    val code: Int = -1,
    val data: T? = null,

)