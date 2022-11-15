package com.asedias.monopolyone.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.get
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.decode.SvgDecoder
import coil.dispose
import coil.load
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.asedias.monopolyone.R
import com.asedias.monopolyone.databinding.GamesListItemBinding
import com.asedias.monopolyone.databinding.HeaderGamesBinding
import com.asedias.monopolyone.model.games.GamesResult
import com.asedias.monopolyone.model.games.Mission
import com.asedias.monopolyone.model.games.Room
import com.asedias.monopolyone.model.games.UsersData
import com.asedias.monopolyone.model.websocket.StatusMessage

class GamesAdapter : RecyclerView.Adapter<GamesAdapter.RoomViewHolder>() {

    private var games: GamesResult? = null

    private val differCallback = object : DiffUtil.ItemCallback<Room>() {
        override fun areItemsTheSame(oldItem: Room, newItem: Room): Boolean {
            return oldItem.room_id == newItem.room_id
        }

        override fun areContentsTheSame(oldItem: Room, newItem: Room): Boolean {
            return oldItem.v == newItem.v
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder =
        RoomViewHolder(LayoutInflater.from(parent.context), parent)

    override fun getItemViewType(position: Int): Int {
        return TYPE_ROOM
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        holder.bind(differ.currentList[position], games!!.rooms.users_data)
    }

    fun setData(data: GamesResult) {
        games = data
        differ.submitList(data.rooms.rooms.toList())
    }

    override fun getItemCount() = differ.currentList.size

    companion object {
        const val TYPE_HEADER = 0
        const val TYPE_ROOM = 1
    }

    class GamesHeaderViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        BindingViewHolder<HeaderGamesBinding>(
            HeaderGamesBinding.inflate(inflater, parent, false)
        ) {
        fun bind(status: StatusMessage?, missions: List<Mission>?) {
            missions?.let {
                binding.missionsButton.isEnabled = true
                binding.missionsButton.text = it.size.toString()
            }
            status?.let {
                binding.chatButton.isEnabled = true
                binding.chatButton.text = it.status.online.toString()
            }
        }
    }

    class RoomViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        BindingViewHolder<GamesListItemBinding>(
            GamesListItemBinding.inflate(inflater, parent, false)
        ) {
        fun bind(room: Room, users: List<UsersData>) {
            binding.gameTypeText.text = when (room.game_submode) {
                0 -> "Обычная игра"
                2 -> "Быстрая игра"
                3 -> "Русская рулетка"
                else -> "Неизвестно ${room.game_submode}"
            }
            binding.gameTypeIcon.setImageResource(
                when (room.game_submode) {
                    0 -> R.drawable.ic_games
                    2 -> R.drawable.ic_game_submode_2
                    3 -> R.drawable.ic_game_submode_3
                    else -> R.drawable.ic_temp
                }
            )
            binding.jackpotText.text = "${room.game_submode}"
            for (i in 0..4) {
                val item: ViewGroup = binding.playersContainer[i] as ViewGroup
                item.visibility = View.VISIBLE
                (item[1] as TextView).text = ""
                (item[2] as TextView).text = ""
                (item[0] as ImageView).dispose()
                (item[0] as ImageView).setImageResource(R.drawable.ic_enter)
            }
            if (room.game_2x2 == 1) {
                val item: ViewGroup = binding.playersContainer[2] as ViewGroup
                (item[1] as TextView).text = ""
                (item[2] as TextView).text = ""
                (item[0] as ImageView).dispose()
                (item[0] as ImageView).setImageResource(R.drawable.ic_versus)
            }
            val max = if (room.game_2x2 == 1) 5 else room.settings.maxplayers
            for (i in max..4) {
                val item: ViewGroup = binding.playersContainer[i] as ViewGroup
                item.visibility = View.INVISIBLE
            }
            var index = 0
            room.players[0].forEach { user_id ->
                val item: ViewGroup = binding.playersContainer[index] as ViewGroup
                users.find { it.user_id == user_id }?.let {
                    (item[1] as TextView).text = it.nick
                    (item[2] as TextView).text =
                        if (it.rank.hidden == 0) it.rank.pts.toString() else ""
                    (item[0] as ImageView).load(it.avatar) {
                        crossfade(true)
                        decoderFactory(SvgDecoder.Factory())
                        transformations(CircleCropTransformation())
                    }
                }
                index++
            }
            index = 3
            if (room.players.size > 1) room.players[1].forEach { user_id ->
                val item: ViewGroup = binding.playersContainer[index] as ViewGroup
                users.find { it.user_id == user_id }?.let {
                    (item[1] as TextView).text = it.nick
                    (item[2] as TextView).text =
                        if (it.rank.hidden == 0) it.rank.pts.toString() else ""
                    var req = ImageRequest.Builder(item.context)
                        .crossfade(true)
                        .decoderFactory(SvgDecoder.Factory())
                        .transformations(CircleCropTransformation())
                        .build()
                    (item[0] as ImageView).load(it.avatar) {
                        crossfade(true)
                        decoderFactory(SvgDecoder.Factory())
                        transformations(CircleCropTransformation())
                    }
                }
                index++
            }
        }
    }
}