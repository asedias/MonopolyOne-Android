package com.asedias.monopolyone.model

data class ListResponse<T: Any>(
    val code: Int,
    val `data`: List<T>,
)