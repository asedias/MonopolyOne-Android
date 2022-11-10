package com.example.material3test.util

import androidx.lifecycle.MutableLiveData
import com.example.material3test.model.auth.Session

open class Auth {

    /* Call only from AuthStoreManager */
    fun setup(
        access_token: String? = "null",
        refresh_token: String? = "null",
        user_id: Int = 0,
        expires_in: Long = 0
    ) {
        accessToken = access_token.toString()
        refreshToken = refresh_token.toString()
        userId = user_id
        expiresTime = expires_in
        observableUserLogged.postValue(isUserLogged())
    }

    fun authorize(session: Session) {
        accessToken = session.access_token.toString()
        refreshToken = session.refresh_token.toString()
        userId = session.user_id
        expiresTime = session.expires_in * 1000 + System.currentTimeMillis()
        observableSession.postValue(session)
        observableUserLogged.postValue(isUserLogged())
    }

    fun isUserLogged() = userId > 0 && expiresTime > System.currentTimeMillis()

    companion object {
        var accessToken = ""
        var refreshToken = ""
        var userId = 0
        var expiresTime = 0L

        var observableUserLogged = MutableLiveData(false)

        var observableSession = MutableLiveData<Session>()
    }
}
