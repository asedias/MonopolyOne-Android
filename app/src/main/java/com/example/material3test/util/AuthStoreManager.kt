package com.example.material3test.util

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.material3test.repository.AuthRepository
import com.haroldadmin.cnradapter.NetworkResponse

class AuthStoreManager(context: Context, owner: LifecycleOwner) {

    private val sharedPreferences by lazy {
        context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
    }
    private val auth by lazy { Auth() }

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
            putInt(USER_ID, Auth.userId)
            putString(ACCESS_TOKEN, Auth.accessToken)
            putString(REFRESH_TOKEN, Auth.refreshToken)
            putLong(EXPIRES_IN, Auth.expiresTime)
            apply()
        }
    }

    private suspend fun refresh() {
        when(val req = AuthRepository().refreshAuth()) {
            is NetworkResponse.Success -> {
                val session = req.body.data
                session?.let { Auth().authorize(it) }
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
        println("AuthStoreManager: Saved user ${Auth.accessToken}@${Auth.userId}")
        if(System.currentTimeMillis() > Auth.expiresTime) {
            println("AuthStoreManager: Updating user token")
            owner.lifecycleScope.launchWhenStarted {
                refresh()
            }
        }
        Auth.observableSession.observe(owner) {
            println("AuthStoreManager: User login ${Auth.accessToken}@${Auth.userId}")
            save()
        }
    }
}