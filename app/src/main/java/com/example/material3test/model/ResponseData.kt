package com.example.material3test.model

data class ResponseData<T: Any>(
    val code: Int,
    val `data`: List<T>,
    val description: String,
)