package com.asedias.monopolyone.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asedias.monopolyone.api.MonopolyWebSocket
import com.asedias.monopolyone.model.games.GamesResult
import com.asedias.monopolyone.model.games.Room
import com.asedias.monopolyone.model.websocket.EventMessage
import com.asedias.monopolyone.repository.MainRepository
import com.asedias.monopolyone.util.WSMessage
import com.haroldadmin.cnradapter.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GamesViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    private lateinit var data: GamesResult
    private val _games = MutableSharedFlow<GamesResult>()
    val games = _games.asSharedFlow()

    init {
        getGamesInfo()
    }

    fun getGamesInfo() = viewModelScope.launch {
        when (val games = repository.getGamesInfo()) {
            is NetworkResponse.Success -> {
                data = games.body.result
                _games.emit(data)
                retrieveEvents()
            }
            is NetworkResponse.ServerError -> {
                //error.postValue(response.body?.code ?: 0)
            }
            is NetworkResponse.NetworkError -> {
                //error.postValue(0)
            }
            is NetworkResponse.UnknownError -> {
                //error.postValue(99)
            }
        }
    }

    private suspend fun retrieveEvents() {
        MonopolyWebSocket.channel.collect() { wsMessage ->
            when (wsMessage) {
                is WSMessage.Event -> {
                    wsMessage.data.users_data?.let {
                        data.rooms.users_data.addAll(it)
                    }
                    handleEvent(wsMessage.data)
                }
                is WSMessage.Auth -> {}
                is WSMessage.Status -> {}
                is WSMessage.Error -> {}
            }
        }
    }

    private suspend fun handleEvent(message: EventMessage) {
        message.events.forEach { event ->
            when (event.type) {
                "room.set" -> message.rooms!!.forEach { addRoom(it) }
                "room.delete" -> deleteRoom(event.room_id)
                "room.patch" -> updateRoom(event.room_id, event.patches!!)
            }
        }
        _games.emit(data)
    }

    private fun addRoom(room: Room) = data.rooms.rooms.add(room)

    private fun findRoom(room_id: String) =
        data.rooms.rooms.find { room -> room.room_id == room_id }

    private fun deleteRoom(room_id: String): Boolean {
        val room = findRoom(room_id)
        if (room != null) {
            return data.rooms.rooms.remove(room)
        }
        return false
    }

    private fun addPlayer(room: Room, team: Int, user_id: Int) = room.players[team].add(user_id)

    private fun removePlayer(room: Room, team: Int, user_id: Int) =
        room.players[team].remove(user_id)

    private fun updateRoom(room_id: String, patches: List<List<Any>>) {
        findRoom(room_id)?.let { room ->
            patches.forEach { patch ->
                val code = (patch[0] as Double).toInt()
                when (patch[1] as String) {
                    "players.0" -> {
                        val user_id = (patch[2] as Double).toInt()
                        if (code == 0) removePlayer(room, 0, user_id)
                        else addPlayer(room, 0, user_id)
                    }
                    "players.1" -> {
                        val user_id = (patch[2] as Double).toInt()
                        if (code == 0) removePlayer(room, 1, user_id)
                        else addPlayer(room, 1, user_id)
                    }
                }
            }
        }
    }
}