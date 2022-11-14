package com.asedias.monopolyone.model.games

data class ItemProto(
    val description: String,
    val image: String,
    val item_proto_id: Int,
    val moneybox: Int,
    val quality_id: Int,
    val title: String,
    val type: Int
)