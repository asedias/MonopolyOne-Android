package com.example.material3test.model

data class ListResponse<T: Any>(
    val code: Int,
    val `data`: List<T>,
)