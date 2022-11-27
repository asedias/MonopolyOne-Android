package com.asedias.monopolyone.domain.repository

import kotlinx.coroutines.flow.first

interface AwaitSessionRepository {
    suspend fun <Type> awaitSessionCall(authRepository: AuthRepository, call: suspend () -> Type): Type {
        authRepository.loadFromLocal().first()
        return call()
    }
}