package com.example.material3test.repository

import com.example.material3test.api.RetrofitInstance

class UserRepository : Repository {
    suspend fun getUser(user_id: Int) = RetrofitInstance.api.getUser(user_id)
}