package com.asedias.monopolyone.data.repository

import com.asedias.monopolyone.MonopolyApp
import com.asedias.monopolyone.data.remote.MonopolyAPI
import com.asedias.monopolyone.domain.model.ResponseState
import com.asedias.monopolyone.domain.model.auth.LoginData
import com.asedias.monopolyone.domain.model.main_page.MainPageData
import com.asedias.monopolyone.domain.model.market.Market
import com.asedias.monopolyone.domain.repository.MarketRepository
import com.haroldadmin.cnradapter.NetworkResponse
import javax.inject.Inject

class MarketRepositoryImpl @Inject constructor(
    val api: MonopolyAPI,
    val app: MonopolyApp,
    private val authRepositoryImpl: AuthRepositoryImpl
) : MarketRepository {

    override suspend fun getLastSellups(offset: Int): ResponseState<Market> {
        return awaitSessionCall(authRepositoryImpl) {
            return@awaitSessionCall when (val result =
                api.getLastSellups(generateParams(offset))) {
                is NetworkResponse.Success -> ResponseState.Success(result.body.data)
                is NetworkResponse.ServerError ->
                    if (result.body != null) {
                        if (result.body!!.code == 1) {
                            val login = authRepositoryImpl.refresh()
                            if(login is LoginData.Error) {
                                ResponseState.Error<Market>(login.code)
                            }
                            ResponseState.Nothing<Market>()
                            getLastSellups(offset)
                        }
                        ResponseState.Error(result.body!!.code)
                    } else
                        ResponseState.Error(10)
                is NetworkResponse.UnknownError ->
                    if (result.body != null)
                        ResponseState.Error(result.body!!.code)
                    else
                        ResponseState.Error(99)
                is NetworkResponse.NetworkError -> ResponseState.Error()
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