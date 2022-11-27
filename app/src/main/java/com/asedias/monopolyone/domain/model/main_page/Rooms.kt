package com.asedias.monopolyone.domain.model.main_page

data class Rooms(
    val rooms: MutableList<Room>,
    val users_data: MutableList<UsersData>
)

data class Room(
    val admin: Int,
    val bans: List<Int>,
    val flags: Flags,
    val game_2x2: Int,
    val game_mode: Int,
    val game_submode: Int,
    val gsm_vote_variants: List<Any>,
    /*val gsm_votes: GsmVotes,*/
    val invites: List<List<Int>>,
    val op_ts_end: Int,
    var players: List<MutableList<Int>>,
    /*val players_joined: PlayersJoined,*/
    val room_id: String,
    val settings: Settings,
    val status: Int,
    var v: Int
)

data class Flags(
    val disposition_mode: Int,
    val is_tournament: Int,
    val ts_created: Int,
    val vip: Int
)

data class Settings(
    val autostart: Int,
    val br_corner: Int,
    val contract_protests: Int,
    val game_timers: Int,
    val maxplayers: Int,
    val pm_allowed: Int,
    val `private`: Int,
    val restarts: Int
)
