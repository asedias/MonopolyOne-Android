package com.asedias.monopolyone.data.repository

import com.asedias.monopolyone.data.MonopolyAPI
import com.asedias.monopolyone.domain.model.Response
import com.asedias.monopolyone.domain.model.main_page.MainPageData
import com.asedias.monopolyone.domain.repository.MainPageRepository
import com.haroldadmin.cnradapter.NetworkResponse
import javax.inject.Inject

class MainPageRepositoryImpl @Inject constructor(
    val api: MonopolyAPI,
    val authRepositoryImpl: AuthRepositoryImpl
) : MainPageRepository {

    override suspend fun getMainPageData(): Response<MainPageData> {
        return awaitSessionCall(authRepositoryImpl) {
            return@awaitSessionCall when (val result = api.getMainPageData(generateParams())) {
                is NetworkResponse.Success -> Response.Success(result.body.result)
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

    private fun generateParams() = mapOf(
        "logged_in" to if (authRepositoryImpl.currentSession.user_id > 0) "1" else "0",
        "access_token" to authRepositoryImpl.currentSession.access_token
    ).filterValues {
        it.isNotEmpty()
    }
}