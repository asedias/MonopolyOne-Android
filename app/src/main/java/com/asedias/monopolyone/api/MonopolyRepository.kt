package com.asedias.monopolyone.api

import com.asedias.monopolyone.util.SessionManager
import com.asedias.monopolyone.util.SessionManager.toInt
import kotlinx.coroutines.flow.first

class MonopolyRepository {

    suspend fun login(email: String, password: String) =
        RetrofitInstance.api.signIn(email, password)

    suspend fun refreshAuth() = RetrofitInstance.api.refreshAuth()

    suspend fun getFriends(user_id: Int = 0) = RetrofitInstance.api.getFriends(user_id)

    suspend fun getGames() = sessionWaitCall {
        RetrofitInstance.api.getGames(
            mapOf(
                "logged_in" to "${SessionManager.isUserLogged().toInt()}",
                "access_token" to SessionManager.getAccessToken()
            ).filterValues {
                it.isNotEmpty()
            }
        )
    }

    suspend fun getAccountInfo() = RetrofitInstance.api.getAccountInfo()

    suspend fun getLastSellups(offset: Int, count: Int) =
        RetrofitInstance.api.getLastSellups(offset, count)

    suspend fun getUser(user_id: Int) = RetrofitInstance.api.getUser(user_id)

    private suspend fun <Type> sessionWaitCall(call: suspend () -> Type): Type {
        SessionManager.sessionFlow.first()
        return call()
    }
}