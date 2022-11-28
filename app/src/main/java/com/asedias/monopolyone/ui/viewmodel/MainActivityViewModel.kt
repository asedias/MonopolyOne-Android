package com.asedias.monopolyone.ui.viewmodel

import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asedias.monopolyone.data.remote.WebSocketClient
import com.asedias.monopolyone.domain.model.websocket.AuthMessage
import com.asedias.monopolyone.domain.websocket.WebSocketMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val webSocketClient: WebSocketClient
) : ViewModel() {

    var avatarDrawable: Drawable? = null

    private val _userData = MutableSharedFlow<AuthMessage>()
    val userData = _userData.asSharedFlow()

    private fun collectUserData() = viewModelScope.launch {
        webSocketClient.getMessages().collect { message ->
            if(message is WebSocketMessage.Auth) _userData.emit(message.data)
        }
    }

    init {
        collectUserData()
    }
}