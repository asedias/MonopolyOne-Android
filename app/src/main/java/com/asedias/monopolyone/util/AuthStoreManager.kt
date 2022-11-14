package com.asedias.monopolyone.util

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.asedias.monopolyone.repository.AuthRepository
import com.haroldadmin.cnradapter.NetworkResponse

class AuthStoreManager(context: Context, owner: LifecycleOwner) {

    private val sharedPreferences by lazy {
        context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
    }
    private val auth by lazy { AuthData() }

    private fun load() {
        sharedPreferences.run {
            auth.setup(
                access_token = getString(ACCESS_TOKEN, "null"),
                refresh_token = getString(REFRESH_TOKEN, "null"),
                user_id = getInt(USER_ID, 0),
                expires_in = getLong(EXPIRES_IN, 0)
            )
        }
    }

    private fun save() {
        sharedPreferences.edit().run {
            putInt(USER_ID, AuthData.userId)
            putString(ACCESS_TOKEN, AuthData.accessToken)
            putString(REFRESH_TOKEN, AuthData.refreshToken)
            putLong(EXPIRES_IN, AuthData.expiresTime)
            apply()
        }
    }

    private suspend fun refresh() {
        when(val req = AuthRepository().refreshAuth()) {
            is NetworkResponse.Success -> {
                val session = req.body.data
                session?.let { AuthData().authorize(it) }
            }
            is NetworkResponse.Error -> {
                req.body?.description.let { println("Refreshing Token Error: $it") }
            }
        }
    }

    companion object {
        const val PREFERENCE_NAME = "auth2"
        const val ACCESS_TOKEN = "access_token"
        const val REFRESH_TOKEN = "refresh_token"
        const val USER_ID = "user_id"
        const val EXPIRES_IN = "expires_in"
    }

    init {
        load()
        println("AuthStoreManager: Saved user ${AuthData.accessToken}@${AuthData.userId}")
        if(AuthData.userId > 0 && System.currentTimeMillis() > AuthData.expiresTime) {
            println("AuthStoreManager: Updating user token")
            owner.lifecycleScope.launchWhenStarted {
                refresh()
            }
        }
        AuthData.observableSession.observe(owner) {
            println("AuthStoreManager: User login ${AuthData.accessToken}@${AuthData.userId}")
            save()
        }
    }
}