package com.asedias.monopolyone.model.friends

import com.asedias.monopolyone.model.basic.User

data class FriendsData(
    val count: Int,
    val friends: List<Friend>,
    val user: User
)