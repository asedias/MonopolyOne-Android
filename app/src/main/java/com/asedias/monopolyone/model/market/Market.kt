package com.asedias.monopolyone.model.market

data class Market(
    val collections: List<Collection>,
    val qualities: List<Quality>,
    val thing_types: List<ThingType>,
    val things: List<Thing>
)