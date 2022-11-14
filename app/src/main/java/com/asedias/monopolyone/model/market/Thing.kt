package com.asedias.monopolyone.model.market

data class Thing(
    val can_be_upgraded: Int,
    val collection: Int,
    val count: Int,
    val delete_price: Double,
    val description: String,
    val drop: List<Drop>,
    val group: Int,
    val image: String,
    val moneybox: Boolean,
    val price: Double,
    val quality: Int,
    val thing_id: Any,
    val thing_prototype_id: Int,
    val thing_prototype_status: Int,
    val thing_type: Int,
    val title: String,
    val twin_thing_prototype_ids: List<Int>
)