package com.asedias.monopolyone.model.basic

data class Badge(
    val can_be_upgraded: Int,
    val collection: Int,
    val description: String,
    val group: Any,
    val image: String,
    val owned_time: Int,
    val quality: Int,
    val thing_id: Int,
    val thing_prototype_id: Int,
    val thing_prototype_status: Int,
    val thing_type: Int,
    val title: String,
    val user_id: Int
)