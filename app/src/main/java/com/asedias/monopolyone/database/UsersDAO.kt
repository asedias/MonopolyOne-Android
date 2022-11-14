package com.asedias.monopolyone.database

import androidx.room.*
import com.asedias.monopolyone.model.games.UsersData

@Dao
interface UsersDAO {
    @Insert
    fun insertUser(users: UsersData)

    @Query("Select * from users")
    fun gelAllUsers(): List<UsersData>

    @Update
    fun updateUser(users: UsersData)

    @Delete
    fun deleteUser(users: UsersData)

}