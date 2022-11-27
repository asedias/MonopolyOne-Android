package com.asedias.monopolyone.domain.model.websocket

import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.asedias.monopolyone.MonopolyApp
import com.asedias.monopolyone.authDataStore
import com.asedias.monopolyone.data.MonopolyWebSocket
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


class SessionManager /*@Inject*/ constructor(
    val app: MonopolyApp,
    /*val repository: MonopolyRepository, */
) {
    
    companion object {
        private val ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        private val USER_ID = intPreferencesKey("user_id")
        private val EXPIRES_IN = intPreferencesKey("expires_in")
        private val EXPIRES_TIME = longPreferencesKey("expires_time")
        
        private const val TAG = "SessionManager"
    }

    private var currentSession = com.asedias.monopolyone.domain.model.auth.Session(
        access_token = "",
        refresh_token = "",
        user_id = 0,
        expires_in = 0,
    )
    val sessionFlow = MutableSharedFlow<com.asedias.monopolyone.domain.model.auth.Session>()

    init {
        app.applicationScope.launch {
            collectSessionFromDataStore()
        }
    }

    suspend fun collectSessionFromDataStore() {
        app.authDataStore.data.map {
            com.asedias.monopolyone.domain.model.auth.Session(
                access_token = it[ACCESS_TOKEN] ?: "",
                refresh_token = it[REFRESH_TOKEN] ?: "",
                user_id = it[USER_ID] ?: 0,
                expires_in = it[EXPIRES_IN] ?: 0,
                //expires_time = it[EXPIRES_TIME] ?: 0,
            )
        }.collectLatest {
            //currentSession = it
            Log.d(TAG, "$it")
            if (needRefreshToken()) {
                refreshToken()
            } else {
                MonopolyWebSocket.reconnect()
                //sessionFlow.emit(currentSession)
            }
        }
    }

    private suspend fun refreshToken() {
        /* TODO AuthRepository */
        /*when (val req = repository.refreshAuth()) {
            is NetworkResponse.Success -> {
                if(req.body.data != null) {
                    saveSession(req.body.data!!)
                } else {
                    sessionFlow.emit(currentSession)
                }
            }
            is NetworkResponse.Error -> {
                Log.d(TAG, "Error while refreshing token")
                sessionFlow.emit(currentSession)
            }
        }*/
    }

    suspend fun saveSession(data: com.asedias.monopolyone.domain.model.auth.Session) {
        app.authDataStore.edit {
            it[ACCESS_TOKEN] = data.access_token
            it[REFRESH_TOKEN] = data.refresh_token
            it[USER_ID] = data.user_id
            it[EXPIRES_IN] = data.expires_in
            it[EXPIRES_TIME] = data.expires_in * 1000 + System.currentTimeMillis()
        }
    }

    fun isUserLogged() = currentSession.user_id > 0
    fun getAccessToken() = currentSession.access_token
    fun getRefreshToken() = currentSession.refresh_token
    fun getExpiresTime() = currentSession.expires_in
    fun getUserID() = currentSession.user_id

    private fun needRefreshToken() =
        currentSession.refresh_token.isNotEmpty()
}