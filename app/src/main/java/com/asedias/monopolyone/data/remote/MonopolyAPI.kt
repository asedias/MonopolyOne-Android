package com.asedias.monopolyone.data.remote

import com.asedias.monopolyone.domain.model.*
import com.asedias.monopolyone.domain.model.account.Account
import com.asedias.monopolyone.domain.model.auth.Session
import com.asedias.monopolyone.domain.model.basic.User
import com.asedias.monopolyone.domain.model.friends.Friends
import com.asedias.monopolyone.domain.model.main_page.MainPageResponse
import com.asedias.monopolyone.domain.model.market.Market
import com.haroldadmin.cnradapter.NetworkResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface MonopolyAPI {

    companion object {
        const val BASE_URL = "https://monopoly-one.com/api/"
    }

    //Auth
    @GET("auth.signin")
    suspend fun signIn(
        @Query("email")
        email: String,
        @Query("password")
        password: String
    ): NetworkResponse<DataResponse<Session>, ErrorResponse>

    @GET("auth.totpVerify")
    suspend fun totpVerify(
        @Query("code")
        email: String,
        @Query("totp_session_token")
        totp_session_token: String
    ): NetworkResponse<DataResponse<Session>, ErrorResponse>

    @GET("auth.refresh")
    suspend fun refreshAuth(
        @Query("refresh_token")
        refresh_token: String
    ): NetworkResponse<DataResponse<Session>, ErrorResponse>

    // Main Page
    @GET("execute.games")
    suspend fun getMainPageData(
        @QueryMap
        options: Map<String, String>,
    ): NetworkResponse<MainPageResponse, ErrorResponse>

    //Market
    @GET("market.getLastSellups")
    suspend fun getLastSellups(
        @QueryMap
        options: Map<String, String>,
    ): NetworkResponse<DataResponse<Market>, ErrorResponse>

    // TODO(Refactor old calls)

    @GET("users.get")
    suspend fun getUser(
        @Query("user_id")
        user_id: Int = 0
    ): NetworkResponse<DataResponse<List<User>>, ErrorResponse>

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
        user_id: Int,
        @Query("access_token")
        access_token: String?,
    ): NetworkResponse<DataResponse<Friends>, ErrorResponse>

    //Account
    @GET("account.info")
    suspend fun getAccountInfo(
        @Query("access_token")
        access_token: String,
        @Query("sct")
        stc: Long = System.currentTimeMillis()
    ): NetworkResponse<DataResponse<Account>, ErrorResponse>

    //Games

    @GET("execute.games")
    suspend fun getGames(
        @QueryMap
        options: Map<String, String>,
    ): NetworkResponse<MainPageResponse, ErrorResponse>
}