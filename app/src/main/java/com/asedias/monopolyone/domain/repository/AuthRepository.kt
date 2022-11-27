package com.asedias.monopolyone.domain.repository

import com.asedias.monopolyone.domain.model.auth.LoginData
import com.asedias.monopolyone.domain.model.auth.Session
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    // API Calls
    suspend fun login(email: String, pass: String): LoginData
    suspend fun refresh(): LoginData
    suspend fun totpVerify(totp_session_token: String): Session
    // Remove Session from local
    fun logout()
    // Save Session to local
    suspend fun saveToLocal(session: Session)
    // Load Session from local
    fun loadFromLocal(): Flow<Session>
}