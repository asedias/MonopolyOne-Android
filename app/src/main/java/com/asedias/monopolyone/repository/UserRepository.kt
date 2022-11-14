package com.asedias.monopolyone.repository

import com.asedias.monopolyone.api.RetrofitInstance

class UserRepository : Blank {
    suspend fun getUser(user_id: Int) = RetrofitInstance.api.getUser(user_id)
}