package com.asedias.monopolyone.model.market

data class Drop(
    val form: Int,
    val hidden: Boolean,
    val kind: Int,
    val rare: Boolean,
    val thing_prototype_id: Int
)