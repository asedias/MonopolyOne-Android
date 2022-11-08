package com.example.material3test.api

import com.example.material3test.SessionManager
import com.example.material3test.model.*
import com.example.material3test.model.auth.Session
import com.example.material3test.model.friends.FriendsData
import com.example.material3test.model.market.Market
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
    ) : NetworkResponse<ListResponse<User>, ErrorResponse>

    //Auth
    @GET("auth.signin")
    suspend fun signIn(
        @Query("email")
        email: String,
        @Query("password")
        password: String
    ): NetworkResponse<DataResponse<Session>, ErrorResponse>

    @GET("market.getLastSellups")
    suspend fun getLastSellups(
        @Query("offset")
        offset: Int = 0,
        @Query("count")
        count: Int = 20,
        /*@Query("stc")
        stc: Int = -1         */
    ): NetworkResponse<DataResponse<Market>, ErrorResponse>

    @GET("friends.get")
    suspend fun getFriends(
        @Query("offset")
        offset: Int = 0,
        @Query("count")
        count: Int = 20,
        @Query("add_user")
        add_user: Int = 0,
        @Query("online")
        online: Int = 0,
        @Query("user_id")
        user_id: Int = SessionManager.currentSession!!.user_id,
        @Query("access_token")
        access_token: String? = SessionManager.accessToken
    ): NetworkResponse<DataResponse<FriendsData>, ErrorResponse>
}