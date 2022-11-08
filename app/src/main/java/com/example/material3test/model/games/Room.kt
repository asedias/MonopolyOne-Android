package com.example.material3test.model.games

data class Room(
    val admin: Int,
    val bans: List<Int>,
    val flags: Flags,
    val game_2x2: Int,
    val game_mode: Int,
    val game_submode: Int,
    val gsm_vote_variants: List<Any>,
    val gsm_votes: GsmVotes,
    val invites: List<List<Int>>,
    val op_ts_end: Int,
    val players: List<List<Int>>,
    val players_joined: PlayersJoined,
    val room_id: String,
    val settings: Settings,
    val status: Int,
    val v: Int
)