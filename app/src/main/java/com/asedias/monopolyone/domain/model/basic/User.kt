package com.asedias.monopolyone.domain.model.basic

data class User(
    val approved: Int,
    val avatar: String,
    val avatar_key: String,
    val badge: Badge,
    val domain: String,
    val games: Int,
    val games_wins: Int,
    val gender: Int,
    val nick: String,
    val nicks_old: List<String>,
    val profile_cover: String,
    val profile_cover_key: String,
    val rank: Rank,
    val user_id: Int,
    val vip: Int,
    val xp: Int,
    val xp_level: Int
)