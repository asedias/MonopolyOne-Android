package com.example.material3test.model.games

import com.example.material3test.model.Rank

data class UsersData(
    val avatar: String,
    val avatar_key: String,
    val domain: String,
    val gender: Int,
    val muted: Int,
    val nick: String,
    val online: Int,
    val rank: Rank,
    val user_id: Int,
    val vip: Int
)