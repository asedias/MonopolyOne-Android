package com.asedias.monopolyone.domain.model.friends

import com.asedias.monopolyone.domain.model.basic.Badge
import com.asedias.monopolyone.domain.model.basic.Rank
import com.asedias.monopolyone.domain.model.basic.User

data class Friends(
    val count: Int,
    val friends: List<Friend>,
    val user: User
)

data class Friend(
    val avatar: String,
    val badge: Badge,
    val domain: String,
    val friendship: Int,
    val games: Int,
    val games_wins: Int,
    val gender: Int,
    val nick: String,
    val nicks_old: List<String>,
    val rank: Rank,
    val social_vk: Int,
    val user_id: Int,
    val xp: Int,
    val xp_level: Int
)