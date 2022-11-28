package com.asedias.monopolyone.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asedias.monopolyone.data.remote.WebSocketClient
import com.asedias.monopolyone.domain.model.ResponseState
import com.asedias.monopolyone.data.repository.MainPageRepositoryImpl
import com.asedias.monopolyone.domain.model.main_page.MainPageData
import com.asedias.monopolyone.domain.model.main_page.Room
import com.asedias.monopolyone.domain.model.websocket.EventMessage
import com.asedias.monopolyone.domain.websocket.WebSocketMessage
import com.asedias.monopolyone.ui.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainFragmentViewModel @Inject constructor(
    private val repository: MainPageRepositoryImpl,
    private val webSocketClient: WebSocketClient,
) :
    ViewModel() {

    private val _state = MutableLiveData<UIState<MainPageData>>(UIState.Loading())
    val state: LiveData<UIState<MainPageData>>
        get() = _state

    private lateinit var data: MainPageData

    init {
        getMainData()
    }

    fun getMainData() = viewModelScope.launch {
        when (val result = repository.getMainPageData()) {
            is ResponseState.Success -> {
                data = result.data
                _state.postValue(UIState.Show(data))
                retrieveEvents()
            }
            is ResponseState.Error -> {
                _state.postValue(UIState.Error(result.code))
            }
            is ResponseState.Nothing -> Unit
        }
    }

    private suspend fun retrieveEvents() {
        webSocketClient.getMessages()
            .filter { it is WebSocketMessage.Event }
            .collect { message ->
                val event = message as WebSocketMessage.Event
                event.data.users_data?.let { data.rooms.users_data.addAll(it) }
                handleEvent(event.data)
                _state.postValue(UIState.Update(data))
            }
    }

    private fun handleEvent(message: EventMessage) {
        message.events.forEach { event ->
            when (event.type) {
                "room.set" -> message.rooms!!.forEach { addRoom(it) }
                "room.delete" -> deleteRoom(event.room_id)
                "room.patch" -> updateRoom(event.room_id, event.patches!!, event.v)
                "gchat.add" -> Unit
            }
        }
    }

    private fun addRoom(room: Room) =
        data.rooms.rooms.add(room)

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

    private fun updateRoom(room_id: String, patches: List<List<Any>>, v: Int) {
        findRoom(room_id)?.let { room ->
            if (room.v >= v) return Unit
            room.v = v
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