package com.asedias.monopolyone.ui.viewmodel

import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asedias.monopolyone.data.MonopolyRepository
import com.asedias.monopolyone.data.MonopolyWebSocket
import com.asedias.monopolyone.util.SocketState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivityViewModel : ViewModel() {

    var avatarDrawable: Drawable? = null

    private val _userData = MutableSharedFlow<com.asedias.monopolyone.domain.model.websocket.AuthMessage>()
    val userData = _userData.asSharedFlow()

    private fun collectUserData() = viewModelScope.launch {
        MonopolyWebSocket.state.collectLatest {
            when (it) {
                is SocketState.Authenticated -> {
                    _userData.emit(MonopolyWebSocket.authMessage!!)
                }
                else -> Unit
            }
        }
    }

    init {
        collectUserData()
    }
}