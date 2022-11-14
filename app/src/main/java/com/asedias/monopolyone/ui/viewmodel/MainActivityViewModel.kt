package com.asedias.monopolyone.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.asedias.monopolyone.model.account.Account
import com.asedias.monopolyone.repository.MainRepository
import com.haroldadmin.cnradapter.NetworkResponse
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class MainActivityViewModel(private val repository: MainRepository) : ViewModel() {

    private val _account = MutableSharedFlow<Account?>()
    val account = _account.asSharedFlow()

    class ProviderFactory(private val repository: MainRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainActivityViewModel(repository) as T
        }
    }

    fun getAccountInfo() = viewModelScope.launch {
        when(val req = repository.getAccountInfo()) {
            is NetworkResponse.Success -> req.body.data.let { _account.emit(it) }
            is NetworkResponse.Error -> {
                _account.emit(null)
            }
        }
    }
}