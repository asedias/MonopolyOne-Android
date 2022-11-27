package com.asedias.monopolyone.domain.model.main_page

import com.asedias.monopolyone.domain.model.friends.Friends

data class MainPageResponse(
    val result: MainPageData
)

data class MainPageData(
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