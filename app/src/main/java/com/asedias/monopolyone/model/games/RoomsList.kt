package com.asedias.monopolyone.model.games

data class RoomsList(
    val rooms: MutableList<Room>,
    val users_data: MutableList<UsersData>
)