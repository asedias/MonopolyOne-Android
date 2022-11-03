package com.example.material3test.api

import com.example.material3test.model.ErrorResponse
import com.example.material3test.model.DataResponse
import com.example.material3test.model.User
import com.haroldadmin.cnradapter.NetworkResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MonopolyAPI {

    companion object {
        const val BASE_URL = "https://monopoly-one.com/api/"
    }

    @GET("users.get")
    suspend fun getUser(
        @Query("user_id")
        user_id: Int = 0
    ) : NetworkResponse<DataResponse<User>, ErrorResponse>
}