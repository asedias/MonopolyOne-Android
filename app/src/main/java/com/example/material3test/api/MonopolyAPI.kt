package com.example.material3test.api

import com.example.material3test.model.ResponseData
import com.example.material3test.model.User
import retrofit2.http.GET
import retrofit2.http.Query

interface MonopolyAPI {

    companion object {
        const val BASE_URL = "https://monopoly-one.com/api/"
    }

    @GET("users.get")
    suspend fun GetUser(
        @Query("user_id")
        user_id: Int = 0
    ) : ResponseData<User>
}