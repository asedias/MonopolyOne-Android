package com.asedias.monopolyone.data

import com.asedias.monopolyone.domain.model.websocket.SessionManager
import com.asedias.monopolyone.util.toInt
import kotlinx.coroutines.flow.first

class MonopolyRepository /*@Inject*/ constructor(
    val api: MonopolyAPI,
    val sessionManager: SessionManager
) {

    suspend fun refreshAuth() = api.refreshAuth(sessionManager.getRefreshToken())

    suspend fun getFriends(user_id: Int = 0) = api.getFriends(
        user_id = user_id,
        access_token = sessionManager.getAccessToken(),
    )

    suspend fun getGames() = sessionWaitCall {
        api.getGames(
            mapOf(
                "logged_in" to "${sessionManager.isUserLogged().toInt()}",
                "access_token" to sessionManager.getAccessToken()
            ).filterValues {
                it.isNotEmpty()
            }
        )
    }

    suspend fun getAccountInfo() = sessionWaitCall { api.getAccountInfo(sessionManager.getAccessToken()) }

    suspend fun getUser(user_id: Int) = api.getUser(user_id)

    private suspend fun <Type> sessionWaitCall(call: suspend () -> Type): Type {
        sessionManager.sessionFlow.first()
        return call()
    }
}