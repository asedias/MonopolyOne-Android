package com.example.material3test.model

data class DataResponse<T: Any>(
    val code: Int,
    val `data`: List<T>,
)