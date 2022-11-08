package com.example.material3test.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.material3test.model.DataResponse
import com.example.material3test.model.ErrorResponse
import com.example.material3test.model.market.Market
import com.example.material3test.repository.MarketRepository
import com.haroldadmin.cnradapter.NetworkResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MarketViewModel(private val repository: MarketRepository): ViewModel() {

    val marketData: MutableLiveData<NetworkResponse<DataResponse<Market>, ErrorResponse>?> = MutableLiveData()
    private val countPerPage = 20

    private fun getLastSellups() = viewModelScope.launch {
        delay(3000L)
        marketData.postValue(null)
        when(val market = repository.getLastSellups(0, countPerPage)) {
            is NetworkResponse.Success -> {
                marketData.postValue(market)
            }
            is NetworkResponse.Error -> {
                marketData.postValue(market)
            }
        }
    }

    fun tryAgain() {
        getLastSellups()
    }

    init {
        getLastSellups()
    }

}