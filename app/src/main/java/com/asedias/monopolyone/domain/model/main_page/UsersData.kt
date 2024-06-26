package com.asedias.monopolyone.domain.model.main_page

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.asedias.monopolyone.domain.model.basic.Rank

@Entity(tableName = "users")
data class UsersData(
    @PrimaryKey(autoGenerate = true) val tid: Int,
    val avatar: String,
    val avatar_key: String,
    val domain: String,
    val gender: Int,
    val muted: Int,
    val nick: String,
    val online: Int,
    val rank: Rank,
    val user_id: Int,
    val vip: Int
)