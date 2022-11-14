package com.asedias.monopolyone.model.account

import com.asedias.monopolyone.model.basic.RankX

data class Account(
    val admin_rights: Int,
    val avatar: String,
    val avatar_key: String,
    val balance: Int,
    val email: String,
    val email_verified: Int,
    val games: Int,
    val games_wins: Int,
    val gender: Int,
    val hasPassword: Int,
    val hasTotp: Int,
    val nick: String,
    val nicks_old: List<String>,
    val online: Int,
    //val penalties: Penalties,
    val rank: RankX,
    //val social: Social,
    val user_id: Int,
    val vip_free_allowed: Int,
    val xp: Int,
    val xp_level: Int
)