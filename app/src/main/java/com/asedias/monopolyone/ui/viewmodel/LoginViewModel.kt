package com.asedias.monopolyone.ui.viewmodel

import androidx.lifecycle.*

class LoginViewModel : ViewModel() {

    private var _loading = MutableLiveData(false)
    val loading: LiveData<Boolean>
        get() = _loading

    fun setIsLoading(state: Boolean?) {
        _loading.value = state
    }

}