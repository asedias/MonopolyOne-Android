package com.asedias.monopolyone.repository

import com.asedias.monopolyone.api.RetrofitInstance

class AuthRepository : Blank {
    suspend fun signIn(email: String, password: String) = RetrofitInstance.api.signIn(email, password)
    suspend fun refreshAuth() = RetrofitInstance.api.refreshAuth()
}