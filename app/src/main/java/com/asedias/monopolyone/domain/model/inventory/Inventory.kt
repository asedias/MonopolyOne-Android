package com.asedias.monopolyone.domain.model.inventory

data class Inventory(
    val collections: List<Collection>,
    val count: Int,
    val items: List<Item>,
    val user: com.asedias.monopolyone.domain.model.basic.User
)

data class Collection(
    val collection_id: Int,
    val title: String
)

data class Drop(
    val item_proto_id: Int
)

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

data class Prices(
    val buy: Int
)