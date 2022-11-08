package com.example.material3test

import android.content.Context
import com.example.material3test.model.auth.Session

/**
 * Session manager to save and fetch data from SharedPreferences
 */
class SessionManager(context: Context) {

    private val prefs by lazy {
        context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    fun saveSessionData(session: Session) = prefs.edit().run {
        currentSession = session
        putInt(USER_ID, session.user_id)
        putString(ACCESS_TOKEN, session.access_token)
        putString(REFRESH_TOKEN, session.refresh_token)
        putInt(EXPIRES_IN, session.expires_in)
        apply()
    }

    private fun loadUserID(): Int = prefs.getInt(USER_ID, 0)
    private fun loadAccessToken(): String? = prefs.getString(ACCESS_TOKEN, "")
    private fun loadRefreshToken(): String? = prefs.getString(REFRESH_TOKEN, "")
    private fun loadExpiresIn(): Int = prefs.getInt(EXPIRES_IN, 0)

    private fun loadSessionData(): Session = Session(
        user_id = loadUserID(),
        access_token = "74XXJ3gCx9HxTPNMRsfFb17pk27C8Dzn",//loadAccessToken(),
        expires_in = loadExpiresIn(),
        refresh_token = loadRefreshToken(),
    )

    companion object {
        const val PREFERENCE_NAME = "auth"
        const val ACCESS_TOKEN = "access_token"
        const val REFRESH_TOKEN = "refresh_token"
        const val USER_ID = "user_id"
        const val EXPIRES_IN = "expires_in"

        var currentSession: Session? = null
        var accessToken: String? = null
        var user_id: Int = -1
    }

    init {
        val session = loadSessionData()
        if(session.user_id > 0) {
            currentSession = session
            accessToken = currentSession!!.access_token
        }
    }

}