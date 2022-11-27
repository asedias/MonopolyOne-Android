package com.asedias.monopolyone.data.repository

import android.util.Log
import com.asedias.monopolyone.MonopolyApp
import com.asedias.monopolyone.data.MonopolyAPI
import com.asedias.monopolyone.domain.model.Response
import com.asedias.monopolyone.domain.model.market.Market
import com.asedias.monopolyone.domain.repository.MarketRepository
import com.haroldadmin.cnradapter.NetworkResponse
import javax.inject.Inject

class MarketRepositoryImpl @Inject constructor(
    val api: MonopolyAPI,
    val app: MonopolyApp,
    private val authRepositoryImpl: AuthRepositoryImpl
) : MarketRepository {

    override suspend fun getLastSellups(offset: Int): Response<Market> {
        return awaitSessionCall(authRepositoryImpl) {
            return@awaitSessionCall when (val result =
                api.getLastSellups(generateParams(offset))) {
                is NetworkResponse.Success -> Response.Success(result.body.data)
                is NetworkResponse.UnknownError ->
                    if(result.body != null)
                        Response.Error(result.body!!.code)
                    else
                        Response.Error(99)
                is NetworkResponse.ServerError ->
                    if(result.body != null)
                        Response.Error(result.body!!.code)
                    else
                        Response.Error(10)
                is NetworkResponse.NetworkError -> Response.Error()
            }
        }
    }

    private fun generateParams(offset: Int) = mapOf(
        "offset" to offset.toString(),
        "count" to "20",
        "access_token" to authRepositoryImpl.currentSession.access_token
    ).filterValues {
        it.isNotEmpty()
    }
}