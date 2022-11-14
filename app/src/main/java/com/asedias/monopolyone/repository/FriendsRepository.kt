package com.asedias.monopolyone.repository

import com.asedias.monopolyone.api.RetrofitInstance

class FriendsRepository : Blank {
    suspend fun getFriends() = RetrofitInstance.api.getFriends(user_id = 0)
}