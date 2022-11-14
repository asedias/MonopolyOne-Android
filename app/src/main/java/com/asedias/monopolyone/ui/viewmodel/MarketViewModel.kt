package com.asedias.monopolyone.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.asedias.monopolyone.model.DataResponse
import com.asedias.monopolyone.model.ErrorResponse
import com.asedias.monopolyone.model.market.Market
import com.asedias.monopolyone.repository.MarketRepository
import com.haroldadmin.cnradapter.NetworkResponse
import kotlinx.coroutines.launch

class MarketViewModel(private val repository: MarketRepository) : ViewModel() {

    val marketData: MutableLiveData<NetworkResponse<DataResponse<Market>, ErrorResponse>?> =
        MutableLiveData()
    private val countPerPage = 20

    private fun getLastSellups(offset: Int = 0) = viewModelScope.launch {
        marketData.postValue(repository.getLastSellups(offset, countPerPage))
    }

    fun tryAgain() {
        getLastSellups()
    }

    init {
        getLastSellups()
    }

    class ProviderFactory(private val repository: MarketRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MarketViewModel(repository) as T
        }
    }
}

