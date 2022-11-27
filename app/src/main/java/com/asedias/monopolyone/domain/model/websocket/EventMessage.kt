package com.asedias.monopolyone.domain.model.websocket

import com.asedias.monopolyone.domain.model.main_page.Message
import com.asedias.monopolyone.domain.model.main_page.Room
import com.asedias.monopolyone.domain.model.main_page.UsersData

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