package com.egraf.refapp.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.egraf.refapp.database.entities.Game
import com.egraf.refapp.database.entities.GameWithAttributes
import com.egraf.refapp.database.entities.League
import com.egraf.refapp.database.entities.Stadium
import java.util.*

@Dao
interface GameDao {
    @Query("SELECT * FROM Game ORDER BY date DESC")
    fun getGames(): LiveData<List<GameWithAttributes>>

    @Query("SELECT * FROM Game WHERE id=(:id)")
    fun getGame(id: UUID): LiveData<GameWithAttributes?>

    @Update
    fun updateGame(game: Game)

    @Insert
    fun addGame(game: Game)

    @Delete
    fun deleteGame(game: Game)

    @Query("SELECT COUNT(*) FROM Game")
    fun countGames(): LiveData<Int>
}