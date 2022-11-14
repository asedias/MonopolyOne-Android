package com.asedias.monopolyone.model.websocket

import com.asedias.monopolyone.model.basic.RankX

data class AuthMessage(
    val counters: Counters,
    val status: Int,
    val user_data: UserData
)

data class Counters(
    val balance: Int,
    val email_verified: Int,
    val friends_requests: Int,
    val invites: Int,
    val messages_new: Int,
    val trades_new: Int,
    val vip_expires: Int
)

data class UserData(
    val admin_rights: Int,
    val avatar: String,
    val games: Int,
    val games_wins: Int,
    val gender: Int,
    val nick: String,
    val nicks_old: List<String>,
    val online: Int,
    //val penalties: Penalties,
    val rank: RankX,
    val social_vk: Int,
    val user_id: Int,
    val xp: Int,
    val xp_level: Int
)