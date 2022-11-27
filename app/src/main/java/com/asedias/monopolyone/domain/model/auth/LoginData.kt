package com.asedias.monopolyone.domain.model.auth

sealed class LoginData {
    data class Success(val session: Session): LoginData()
    data class Error(val code: Int = 0): LoginData()
    data class TOTPNeeded(val totp_session_token: String): LoginData()
}
