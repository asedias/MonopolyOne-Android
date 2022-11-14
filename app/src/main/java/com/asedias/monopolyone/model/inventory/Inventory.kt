package com.asedias.monopolyone.model.inventory

import com.asedias.monopolyone.model.basic.User

data class Inventory(
    val collections: List<Collection>,
    val count: Int,
    val items: List<Item>,
    val user: User
)