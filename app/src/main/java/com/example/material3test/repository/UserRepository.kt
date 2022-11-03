package com.example.material3test.repository

import com.example.material3test.api.RetrofitInstance

class UserRepository {
    suspend fun GetUser(user_id: Int) = RetrofitInstance.api.GetUser(user_id)
}