package com.example.material3test.model.friends

import com.example.material3test.model.Badge
import com.example.material3test.model.Rank

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