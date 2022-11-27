package com.asedias.monopolyone.domain.model.main_page

import com.asedias.monopolyone.domain.model.basic.Rank

data class Gchat(
    val item_protos: List<ItemProto>,
    val messages: List<Message>,
    val status: Status,
    val users: List<ChatUser>
)

data class ChatUser(
    val avatar: String,
    val avatar_key: String,
    val bot: Int,
    val bot_owner: Int,
    val current_game: CurrentGame,
    val domain: String,
    val gender: Int,
    val nick: String,
    val online: Int,
    val rank: Rank,
    val user_id: Int,
    val vip: Int
)

data class CurrentGame(
    val gs_game_id: String,
    val gs_id: String
)

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

data class ItemProto(
    val description: String,
    val image: String,
    val item_proto_id: Int,
    val moneybox: Int,
    val quality_id: Int,
    val title: String,
    val type: Int
)

data class Status(
    val closed: Boolean,
    val emoteonly: Boolean,
    val slowmode: Boolean
)