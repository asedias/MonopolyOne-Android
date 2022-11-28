package com.asedias.monopolyone.domain.repository

import com.asedias.monopolyone.domain.model.ResponseState
import com.asedias.monopolyone.domain.model.market.Market

interface MarketRepository: AwaitSessionRepository {
    suspend fun getLastSellups(offset: Int): ResponseState<Market>
}