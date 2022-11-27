package com.asedias.monopolyone.domain.repository

import com.asedias.monopolyone.domain.model.Response
import com.asedias.monopolyone.domain.model.market.Market

interface MarketRepository: AwaitSessionRepository {
    suspend fun getLastSellups(offset: Int): Response<Market>
}