package com.example.material3test.repository

import com.example.material3test.api.RetrofitInstance

class AuthRepository : Repository {
    suspend fun signIn(email: String, password: String) = RetrofitInstance.api.signIn(email, password)
    suspend fun refreshAuth() = RetrofitInstance.api.refreshAuth()
}