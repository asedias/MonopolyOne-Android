package com.asedias.monopolyone.util

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.asedias.monopolyone.api.MonopolyRepository
import com.asedias.monopolyone.model.auth.Session
import com.haroldadmin.cnradapter.NetworkResponse
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map

val Context.authDataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_data")

object SessionManager {

    private val ACCESS_TOKEN = stringPreferencesKey("access_token")
    private val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    private val USER_ID = intPreferencesKey("user_id")
    private val EXPIRES_IN = intPreferencesKey("expires_in")
    private val EXPIRES_TIME = longPreferencesKey("expires_time")

    private var currentSession = Session()
    val sessionFlow = MutableSharedFlow<Session>()

    suspend fun collectSessionFromDataStore(context: Context) {
        context.authDataStore.data.map {
            Session(
                access_token = it[ACCESS_TOKEN] ?: "",
                refresh_token = it[REFRESH_TOKEN] ?: "",
                user_id = it[USER_ID] ?: 0,
                expires_in = it[EXPIRES_IN] ?: 0,
                expires_time = it[EXPIRES_TIME] ?: 0,
            )
        }.collectLatest {
            currentSession = it
            if (needRefreshToken()) {
                refreshToken(context)
            } else {
                sessionFlow.emit(currentSession)
            }
        }
    }

    private suspend fun refreshToken(context: Context) {
        when (val req = MonopolyRepository().refreshAuth()) {
            is NetworkResponse.Success -> {
                if(req.body.data != null) {
                    saveSession(context, req.body.data!!)
                } else {
                    sessionFlow.emit(currentSession)
                }
            }
            is NetworkResponse.Error -> {
                Log.i(Constants.TAG_SESSION_MANAGER, "Error while refreshing token")
                sessionFlow.emit(currentSession)
            }
        }
    }

    suspend fun saveSession(context: Context, data: Session) {
        context.authDataStore.edit {
            it[ACCESS_TOKEN] = data.access_token
            it[REFRESH_TOKEN] = data.refresh_token
            it[USER_ID] = data.user_id
            it[EXPIRES_IN] = data.expires_in
            it[EXPIRES_TIME] = data.expires_in * 1000 + System.currentTimeMillis()
        }
    }

    fun isUserLogged() = currentSession.user_id > 0
    fun Boolean.toInt() = if (this) 1 else 0

    fun getAccessToken() = currentSession.access_token
    fun getRefreshToken() = currentSession.refresh_token
    fun getExpiresTime() = currentSession.expires_in
    fun getUserID() = currentSession.user_id

    private fun needRefreshToken() =
        currentSession.refresh_token.isNotEmpty() && System.currentTimeMillis() > currentSession.expires_time
}