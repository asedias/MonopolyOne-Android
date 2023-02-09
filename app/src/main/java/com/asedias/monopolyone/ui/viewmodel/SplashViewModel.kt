package com.asedias.monopolyone.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asedias.monopolyone.BuildConfig
import com.asedias.monopolyone.R
import com.asedias.monopolyone.data.remote.WebSocketClient
import com.asedias.monopolyone.domain.model.auth.LoginData
import com.asedias.monopolyone.domain.websocket.WebSocketMessage
import com.asedias.monopolyone.util.getErrorStringRes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filter
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    val webSocketClient: WebSocketClient
) : ViewModel() {

    val state = MutableLiveData(SplashState())

    suspend fun collectRequires() {
        webSocketClient.reconnect()
        if(BuildConfig.DEBUG) delay(1000L)
        webSocketClient.getMessages()
            .filter { message -> message is WebSocketMessage.Auth }
            .collect { message ->
                val auth = message as WebSocketMessage.Auth
                if(auth.data.status == 0 && hasUserData()) {
                    state.postValue(
                        SplashState(
                            startMainActivity = false,
                            text = R.string.splash_half
                        )
                    )
                    when (val loginData = webSocketClient.authRepositoryImpl.refresh()) {
                        is LoginData.Success -> {
                            state.postValue(
                                SplashState(
                                    startMainActivity = true,
                                    text = R.string.splash_connected
                                )
                            )
                        }
                        is LoginData.Error -> {
                            webSocketClient.authRepositoryImpl.logout()
                            state.postValue(
                                SplashState(
                                    startMainActivity = true,
                                    text = getErrorStringRes(loginData.code)
                                )
                            )
                        }
                        is LoginData.TOTPNeeded -> {
                            state.postValue(
                                SplashState(
                                    startMainActivity = false,
                                    text = R.string.network_error
                                )
                            )
                        }
                    }
                } else {
                    state.postValue(
                        SplashState(
                            startMainActivity = true,
                            text = R.string.splash_connected
                        )
                    )
                }
            }
    }

    fun hasUserData() = webSocketClient.authRepositoryImpl.currentSession.user_id > 0
}

data class SplashState(
    val hasUser: Boolean = false,
    val startMainActivity: Boolean = false,
    val text: Int = R.string.splash_connecting,
)