package com.egraf.refapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.egraf.refapp.Game
import java.util.*

@Dao
interface GameDao {
    @Query("SELECT * FROM Game")
    fun getGames(): LiveData<List<Game>>
    @Query("SELECT * FROM Game WHERE id=(:id)")
    fun getGame(id: UUID): LiveData<Game?>
}