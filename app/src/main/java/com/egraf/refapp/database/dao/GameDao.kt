package com.egraf.refapp.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.egraf.refapp.database.entities.Game
import com.egraf.refapp.database.entities.GameWithAttributes
import com.egraf.refapp.database.entities.Stadium
import java.util.*

@Dao
interface GameDao {
    @Query("SELECT * FROM Game")
    fun getGames(): LiveData<List<GameWithAttributes>>

    @Query("SELECT * FROM Game WHERE id=(:id)")
    fun getGame(id: UUID): LiveData<GameWithAttributes?>

    @Update
    fun updateGame(game: Game)

    @Transaction
    fun addGameWithAttributes(gameWithAttributes: GameWithAttributes) {
        addGame(gameWithAttributes.game)
        if (gameWithAttributes.stadium != null) {
            addStadium(gameWithAttributes.stadium!!)
        }
    }

    @Insert
    fun addGame(game: Game)

    @Insert
    fun addStadium(stadium: Stadium)

    @Delete
    fun deleteGame(game: Game)

    @Query("SELECT COUNT(*) FROM Game")
    fun countGames(): LiveData<Int>
}