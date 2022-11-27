package com.asedias.monopolyone.data.repository

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.asedias.monopolyone.MonopolyApp
import com.asedias.monopolyone.data.MonopolyAPI
import com.asedias.monopolyone.domain.model.auth.LoginData
import com.asedias.monopolyone.domain.model.DataResponse
import com.asedias.monopolyone.domain.model.ErrorResponse
import com.asedias.monopolyone.domain.model.auth.Session
import com.asedias.monopolyone.domain.repository.AuthRepository
import com.asedias.monopolyone.sessionDataStore
import com.haroldadmin.cnradapter.NetworkResponse
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    val api: MonopolyAPI,
    val app: MonopolyApp,
) : AuthRepository {

    var currentSession: Session = Session()

    override suspend fun login(email: String, pass: String): LoginData {
        return retrieveLoginData(api.signIn(email, pass))
    }

    override suspend fun refresh(): LoginData {
        loadFromLocal().first()
        return retrieveLoginData(api.refreshAuth(currentSession.refresh_token))
    }

    private fun retrieveLoginData(result: NetworkResponse<DataResponse<Session>, ErrorResponse>): LoginData {
        when (result) {
            is NetworkResponse.Success -> {
                if (result.body.code == 0) {
                    return if (result.body.data.user_id > 0) {
                        currentSession = result.body.data
                        LoginData.Success(result.body.data)
                    } else {
                        LoginData.TOTPNeeded(result.body.data.totp_session_token!!)
                    }
                }
                return LoginData.Error(result.body.code)
            }
            is NetworkResponse.ServerError -> return LoginData.Error(10)
            is NetworkResponse.UnknownError -> return LoginData.Error(99)
            is NetworkResponse.NetworkError -> return LoginData.Error()
        }
    }

    override suspend fun totpVerify(totp_session_token: String): Session {
        TODO("Not yet implemented")
    }

    override fun logout() {
        app.applicationScope.launch {
            app.sessionDataStore.edit {
                it[KEY_ACCESS_TOKEN] = ""
                it[KEY_REFRESH_TOKEN] = ""
                it[KEY_USER_ID] = 0
                it[KEY_EXPIRES_IN] = 0
            }
        }
    }

    override suspend fun saveToLocal(session: Session) {
        app.sessionDataStore.edit {
            it[KEY_ACCESS_TOKEN] = session.access_token
            it[KEY_REFRESH_TOKEN] = session.refresh_token
            it[KEY_USER_ID] = session.user_id
            it[KEY_EXPIRES_IN] = session.expires_in
        }
    }

    override fun loadFromLocal(): Flow<Session> {
        val flow = app.sessionDataStore.data.map {
            currentSession = Session(
                access_token = it[KEY_ACCESS_TOKEN] ?: "",
                refresh_token = it[KEY_REFRESH_TOKEN] ?: "",
                user_id = it[KEY_USER_ID] ?: 0,
                expires_in = it[KEY_EXPIRES_IN] ?: 0,
            )
            currentSession
        }
        return flow {
            emitAll(flow)
        }
    }
    
    companion object {
        private val KEY_ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val KEY_REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        private val KEY_USER_ID = intPreferencesKey("user_id")
        private val KEY_EXPIRES_IN = intPreferencesKey("expires_in")
    }

    init {
        loadFromLocal()
    }
}