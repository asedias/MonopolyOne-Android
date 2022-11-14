package com.asedias.monopolyone.model.games

import com.asedias.monopolyone.model.basic.Rank

data class User(
    val avatar: String,
    val avatar_key: String,
    val bot: Int,
    val bot_owner: Int,
    val current_game: CurrentGame,
    val domain: String,
    val gender: Int,
    val nick: String,
    val online: Int,
    val rank: Rank,
    val user_id: Int,
    val vip: Int
)