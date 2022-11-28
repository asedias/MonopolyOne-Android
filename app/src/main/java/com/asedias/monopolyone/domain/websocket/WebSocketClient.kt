package com.asedias.monopolyone.domain.websocket

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow

interface WebSocketClient {
    suspend fun connect()
    fun disconnect()
    fun getMessages(): Flow<WebSocketMessage>
    fun getState(): Flow<WebSocketState>
    suspend fun sendMessage(message: String)
    suspend fun handleMessage(message: String) : WebSocketMessage
}