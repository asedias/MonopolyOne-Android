package com.example.material3test.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.material3test.repository.MarketRepository

class MarketViewModelProviderFactory(
    private val repository: MarketRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MarketViewModel(repository) as T
    }
}