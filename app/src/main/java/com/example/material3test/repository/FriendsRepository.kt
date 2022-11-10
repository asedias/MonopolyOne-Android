package com.example.material3test.repository

import com.example.material3test.api.RetrofitInstance

class FriendsRepository : Repository {
    suspend fun getFriends() = RetrofitInstance.api.getFriends(user_id = 0)
}