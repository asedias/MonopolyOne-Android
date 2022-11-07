package com.example.material3test.model.auth

data class Session(
    val user_id: Int = 0,
    val access_token: String? = "",
    val expires_in: Int = 0,
    val refresh_token: String? = "",
)
