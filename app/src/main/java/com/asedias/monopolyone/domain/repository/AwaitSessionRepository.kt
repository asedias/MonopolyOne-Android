package com.asedias.monopolyone.domain.repository

import com.asedias.monopolyone.domain.model.ResponseState
import com.asedias.monopolyone.domain.model.auth.LoginData
import kotlinx.coroutines.flow.first

interface AwaitSessionRepository {
    suspend fun <Type : ResponseState<*>> awaitSessionCall(authRepository: AuthRepository, call: suspend () -> Type): Type {
        authRepository.loadFromLocal().first()
        val result = call()
        if(result is ResponseState.Error<*> && result.code == 1) {
           //if(authRepository.refresh() is LoginData.Success)
                //return ResponseState.Nothing<Any>()
            refreshAuth(authRepository)
            TODO("Попытаться обновить данные, если плохие то сбросить их и повторить запрос с новыми параметрами")
        }
        return call()
    }

    suspend fun refreshAuth(authRepository: AuthRepository) : LoginData =
        authRepository.refresh()
}