package com.asedias.monopolyone.model.websocket

data class StatusMessage(
    val status: Data
)

data class Data(
    val emotes_restricted: String,
    val online: Int,
    val sct: Long,
    val streams: Streams,
    val time: Int
)

data class Streams(
    val count: Int
)