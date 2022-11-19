package com.asedias.monopolyone.ui.viewmodel

import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asedias.monopolyone.api.MonopolyRepository
import com.asedias.monopolyone.api.MonopolyWebSocket
import com.asedias.monopolyone.model.websocket.AuthMessage
import com.asedias.monopolyone.util.SocketState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(private val repository: MonopolyRepository) : ViewModel() {

    var avatarDrawable: Drawable? = null

    private val _userData = MutableSharedFlow<AuthMessage>()
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