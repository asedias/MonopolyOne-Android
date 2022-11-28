package com.asedias.monopolyone.data.repository

import com.asedias.monopolyone.data.remote.MonopolyAPI
import com.asedias.monopolyone.domain.model.ResponseState
import com.asedias.monopolyone.domain.model.auth.LoginData
import com.asedias.monopolyone.domain.model.main_page.MainPageData
import com.asedias.monopolyone.domain.repository.MainPageRepository
import com.haroldadmin.cnradapter.NetworkResponse
import javax.inject.Inject

class MainPageRepositoryImpl @Inject constructor(
    val api: MonopolyAPI,
    val authRepositoryImpl: AuthRepositoryImpl
) : MainPageRepository {

    override suspend fun getMainPageData(): ResponseState<MainPageData> {
        return awaitSessionCall(authRepositoryImpl) {
            return@awaitSessionCall when (val result = api.getMainPageData(generateParams())) {
                is NetworkResponse.Success -> ResponseState.Success(result.body.result)
                is NetworkResponse.ServerError ->
                    if (result.body != null) {
                        if (result.body!!.code == 1) {
                            val login = authRepositoryImpl.refresh()
                            if(login is LoginData.Error) {
                                ResponseState.Error<MainPageData>(login.code)
                            }
                            ResponseState.Nothing<MainPageData>()
                            getMainPageData()
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

    private fun generateParams() = mapOf(
        "logged_in" to if (authRepositoryImpl.currentSession.user_id > 0) "1" else "0",
        "access_token" to authRepositoryImpl.currentSession.access_token
    ).filterValues {
        it.isNotEmpty()
    }
}