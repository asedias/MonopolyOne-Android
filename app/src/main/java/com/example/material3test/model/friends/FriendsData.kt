package com.example.material3test.model.friends

import com.example.material3test.model.User

data class FriendsData(
    val count: Int,
    val friends: List<Friend>,
    val user: User
)