package com.asedias.monopolyone.api

import com.asedias.monopolyone.model.*
import com.asedias.monopolyone.model.account.Account
import com.asedias.monopolyone.model.auth.Session
import com.asedias.monopolyone.model.basic.User
import com.asedias.monopolyone.model.friends.FriendsData
import com.asedias.monopolyone.model.games.GamesResponse
import com.asedias.monopolyone.model.market.Market
import com.asedias.monopolyone.util.SessionManager
import com.haroldadmin.cnradapter.NetworkResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface MonopolyAPI {

    companion object {
        const val BASE_URL = "https://monopoly-one.com/api/"
    }

    @GET("users.get")
    suspend fun getUser(
        @Query("user_id")
        user_id: Int = 0
    ): NetworkResponse<ListResponse<User>, ErrorResponse>

    //Auth
    @GET("auth.signin")
    suspend fun signIn(
        @Query("email")
        email: String,
        @Query("password")
        password: String
    ): NetworkResponse<DataResponse<Session>, ErrorResponse>

    @GET("auth.refresh")
    suspend fun refreshAuth(
        @Query("refresh_token")
        refresh_token: String? = SessionManager.getRefreshToken()
    ): NetworkResponse<DataResponse<Session>, ErrorResponse>

    //Market
    @GET("market.getLastSellups")
    suspend fun getLastSellups(
        @Query("offset")
        offset: Int = 0,
        @Query("count")
        count: Int = 20,
        /*@Query("stc")
        stc: Int = -1         */
    ): NetworkResponse<DataResponse<Market>, ErrorResponse>

    //Friends
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
        user_id: Int = SessionManager.getUserID(),
        @Query("access_token")
        access_token: String? = SessionManager.getAccessToken()
    ): NetworkResponse<DataResponse<FriendsData>, ErrorResponse>

    //Account
    @GET("account.info")
    suspend fun getAccountInfo(
        @Query("access_token")
        access_token: String? = SessionManager.getAccessToken(),
        @Query("sct")
        stc: Long = System.currentTimeMillis()
    ): NetworkResponse<DataResponse<Account>, ErrorResponse>

    //Games

    @GET("execute.games")
    suspend fun getGames(
        @QueryMap
        options: Map<String, String>,
    ): NetworkResponse<GamesResponse, ErrorResponse>
}