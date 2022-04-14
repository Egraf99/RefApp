package com.egraf.refapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.egraf.refapp.Game
import java.util.*

@Dao
interface GameDao {
    @Query("SELECT * FROM Game")
    fun getGames(): LiveData<List<Game>>

    @Query("SELECT * FROM Game WHERE id=(:id)")
    fun getGame(id: UUID): LiveData<Game?>

    @Update
    fun updateGame(game: Game)

    @Insert
    fun addGame(game: Game)

    @Delete
    fun deleteGame(game: Game)

    @Query("SELECT COUNT(*) FROM Game")
    fun countGames(): LiveData<Int>
}