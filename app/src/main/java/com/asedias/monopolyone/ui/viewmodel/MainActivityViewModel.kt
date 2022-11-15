package com.asedias.monopolyone.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asedias.monopolyone.model.account.Account
import com.asedias.monopolyone.repository.MainRepository
import com.haroldadmin.cnradapter.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    private val _account = MutableSharedFlow<Account?>()
    val account = _account.asSharedFlow()

    fun getAccountInfo() = viewModelScope.launch {
        when(val req = repository.getAccountInfo()) {
            is NetworkResponse.Success -> req.body.data.let { _account.emit(it) }
            is NetworkResponse.Error -> {
                _account.emit(null)
            }
        }
    }
}