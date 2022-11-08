package com.example.material3test.model.games

data class Gchat(
    val item_protos: List<ItemProto>,
    val messages: List<Message>,
    val status: Status,
    val users: List<User>
)