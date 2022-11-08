package com.example.material3test.model.inventory

import com.example.material3test.model.User

data class Inventory(
    val collections: List<Collection>,
    val count: Int,
    val items: List<Item>,
    val user: User
)