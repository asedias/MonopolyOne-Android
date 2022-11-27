package com.asedias.monopolyone.domain.model.auth

data class Session(
    val user_id: Int = 0,
    val access_token: String = "",
    val expires_in: Int = 0,
    val refresh_token: String = "",

    val totp_session_token: String? = "",

)
