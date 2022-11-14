package com.asedias.monopolyone.model.games

data class GamesResponse(
    val result: GamesResult
)

data class GamesResult(
    val blog: Blog,
    val friends: Friends,
    val gchat: Gchat,
    val gm_shuffle: GmShuffle,
    val is_vip_free_allowed: Int,
    val missions: List<Mission>,
    val rooms: RoomsList,
    val seasonpass: Int,
    val streams: List<Any>,
    val top_week: TopWeek
)