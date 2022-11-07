package com.example.material3test.repository

import com.example.material3test.api.RetrofitInstance

class MarketRepository {

    suspend fun getLastSellups(offset: Int, count:Int) = RetrofitInstance.api.getLastSellups(offset, count)

}