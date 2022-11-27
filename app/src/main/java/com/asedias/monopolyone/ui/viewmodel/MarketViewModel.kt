package com.asedias.monopolyone.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asedias.monopolyone.domain.model.Response
import com.asedias.monopolyone.domain.model.market.Market
import com.asedias.monopolyone.domain.repository.MarketRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarketViewModel @Inject constructor(private val repository: MarketRepository) : ViewModel() {

    val marketData: MutableLiveData<Response<Market>> =
        MutableLiveData()

    private fun getLastSellups(offset: Int = 0) = viewModelScope.launch {
        marketData.postValue(repository.getLastSellups(offset))
    }

    fun tryAgain() {
        getLastSellups()
    }

    init {
        getLastSellups()
    }
}

