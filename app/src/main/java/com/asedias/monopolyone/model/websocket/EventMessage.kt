package com.asedias.monopolyone.model.websocket

import com.asedias.monopolyone.model.games.Message
import com.asedias.monopolyone.model.games.Room
import com.asedias.monopolyone.model.games.UsersData

data class EventMessage(
    val events: List<Event>,
    val id_last_given: Int,
    val messages: List<Message>?,
    val rooms: List<Room>?,
    val users_data: List<UsersData>?
)

data class Event(
    val id: String,
    val id_last: Long,
    val msg_id: String,
    val patches: List<List<Any>>?, //??
    val room_id: String,
    val type: String,
    val v: Int
)