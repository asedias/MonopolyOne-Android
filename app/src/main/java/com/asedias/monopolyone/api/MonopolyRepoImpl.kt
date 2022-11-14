package com.asedias.monopolyone.api

import com.asedias.monopolyone.model.DataResponse
import com.asedias.monopolyone.model.ErrorResponse
import com.asedias.monopolyone.model.account.Account
import com.asedias.monopolyone.model.auth.Session
import com.asedias.monopolyone.model.friends.FriendsData
import com.asedias.monopolyone.model.games.GamesResponse
import com.asedias.monopolyone.model.market.Market
import com.haroldadmin.cnradapter.NetworkResponse

class MonopolyRepoImpl : MonopolyRepository {

    override suspend fun getUser( user_id: Int ) = RetrofitInstance.api.getUser(user_id)

    override suspend fun signIn(
        email: String,
        password: String
    ): NetworkResponse<DataResponse<Session>, ErrorResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun refreshAuth(refresh_token: String?): NetworkResponse<DataResponse<Session>, ErrorResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getLastSellups(
        offset: Int,
        count: Int
    ): NetworkResponse<DataResponse<Market>, ErrorResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getFriends(
        offset: Int,
        count: Int,
        add_user: Int,
        online: Int,
        user_id: Int,
        access_token: String?
    ): NetworkResponse<DataResponse<FriendsData>, ErrorResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getAccountInfo(
        access_token: String?,
        stc: Long
    ): NetworkResponse<DataResponse<Account>, ErrorResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getGamesInfo(
        logged_in: Int,
        access_token: String?,
        stc: Long
    ): NetworkResponse<GamesResponse, ErrorResponse> {
        TODO("Not yet implemented")
    }

}