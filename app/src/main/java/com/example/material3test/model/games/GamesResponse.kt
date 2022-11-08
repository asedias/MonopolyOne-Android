package com.example.material3test.model.games

data class GamesResponse(
    val result: Result
)

data class Result(
    val blog: Blog,
    val friends: Friends,
    val gchat: Gchat,
    val gm_shuffle: GmShuffle,
    val is_vip_free_allowed: Int,
    val missions: List<Mission>,
    val rooms: Rooms,
    val seasonpass: Int,
    val streams: List<Any>,
    val top_week: TopWeek
)