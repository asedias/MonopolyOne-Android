package com.asedias.monopolyone.model.inventory

data class Item(
    val case_item_proto_ids: List<Int>,
    val collection_id: Int,
    val description: String,
    val drop: List<Drop>,
    val image: String,
    val item_id: Int,
    val item_proto_id: Int,
    val prices: Prices,
    val quality_id: Int,
    val title: String,
    val ts_owned: Int,
    val type: Int
)