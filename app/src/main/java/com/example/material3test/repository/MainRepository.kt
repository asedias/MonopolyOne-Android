package com.example.material3test.repository

import com.example.material3test.api.RetrofitInstance

class MainRepository : Repository {
    suspend fun getAccountInfo() = RetrofitInstance.api.getAccountInfo()
}