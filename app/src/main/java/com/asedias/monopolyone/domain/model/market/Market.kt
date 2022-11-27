package com.asedias.monopolyone.domain.model.market

data class Market(
    val collections: List<Collection>,
    val qualities: List<Quality>,
    val thing_types: List<ThingType>,
    val things: List<Thing>
)

data class Collection(
    val id: Int,
    val title: String
)

data class Drop(
    val form: Int,
    val hidden: Boolean,
    val kind: Int,
    val rare: Boolean,
    val thing_prototype_id: Int
)

data class Quality(
    val coeff_rent: Double,
    val color: String,
    val id: Int,
    val title: String
)

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

data class ThingType(
    val id: Int,
    val title: String
)
