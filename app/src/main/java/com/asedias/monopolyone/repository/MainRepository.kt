package com.asedias.monopolyone.repository

import com.asedias.monopolyone.api.RetrofitInstance

class MainRepository : Blank {
    //suspend fun getGamesInfo() = RetrofitInstance.api.getGamesInfo()
    suspend fun getAccountInfo() = RetrofitInstance.api.getAccountInfo()
}