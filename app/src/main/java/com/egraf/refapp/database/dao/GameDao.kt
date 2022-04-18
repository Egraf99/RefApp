package com.egraf.refapp.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.egraf.refapp.database.entities.Game
import com.egraf.refapp.database.entities.GameWithStadium
import com.egraf.refapp.database.entities.Stadium
import java.util.*

@Dao
interface GameDao {
    @Query("SELECT * FROM Game")
    fun getGames(): LiveData<List<GameWithStadium>>

    @Query("SELECT * FROM Game WHERE id=(:id)")
    fun getGame(id: UUID): LiveData<GameWithStadium?>

    @Update
    fun updateGame(game: Game)

    @Insert
    fun addGame(game: Game)

    @Insert
    fun addStadium(stadium: Stadium)

    @Delete
    fun deleteGame(game: Game)

    @Query("SELECT COUNT(*) FROM Game")
    fun countGames(): LiveData<Int>
}