package com.asedias.monopolyone.api

class MonopolyRepository {
    suspend fun login(email: String, password: String) =
        RetrofitInstance.api.signIn(email, password)

    suspend fun refreshAuth() = RetrofitInstance.api.refreshAuth()

    suspend fun getFriends(user_id: Int = 0) = RetrofitInstance.api.getFriends(user_id)

    suspend fun getGamesInfo() = RetrofitInstance.api.getGamesInfo()

    suspend fun getAccountInfo() = RetrofitInstance.api.getAccountInfo()

    suspend fun getLastSellups(offset: Int, count: Int) =
        RetrofitInstance.api.getLastSellups(offset, count)

    suspend fun getUser(user_id: Int) = RetrofitInstance.api.getUser(user_id)
}