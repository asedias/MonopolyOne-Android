package com.asedias.monopolyone.model.games

data class Mission(
    val counter: Int,
    val name: String,
    val reward: Int,
    val reward_type: String,
    val target: Int,
    val ts_expire: Int
)