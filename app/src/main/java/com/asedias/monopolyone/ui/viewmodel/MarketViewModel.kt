package com.asedias.monopolyone.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asedias.monopolyone.domain.model.ResponseState
import com.asedias.monopolyone.domain.model.market.Market
import com.asedias.monopolyone.domain.repository.MarketRepository
import com.asedias.monopolyone.ui.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarketViewModel @Inject constructor(private val repository: MarketRepository) : ViewModel() {

    private val _state = MutableLiveData<UIState<Market>>(UIState.Loading())
    val state: LiveData<UIState<Market>>
        get() = _state

    fun getLastSellups(offset: Int = 0) = viewModelScope.launch {
        when(val result = repository.getLastSellups(offset)) {
            is ResponseState.Success -> _state.postValue(UIState.Show(result.data))
            is ResponseState.Error -> _state.postValue(UIState.Error(result.code))
            is ResponseState.Nothing -> Unit
        }
    }



    init {
        getLastSellups()
    }
}

