package com.asedias.monopolyone.model.games

data class Message(
    val case_item_proto_id: Int,
    val drop_item_proto_id: Int,
    val is_public: Int,
    val msg_id: String,
    val text: String,
    val ts: Int,
    val type: Int,
    val user_id: Int,
    val user_ids_mentioned: List<Int>
)