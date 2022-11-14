package com.asedias.monopolyone.repository

import com.asedias.monopolyone.api.RetrofitInstance

class MarketRepository : Blank {
    suspend fun getLastSellups(offset: Int, count:Int) = RetrofitInstance.api.getLastSellups(offset, count)
}