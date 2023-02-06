package com.egraf.refapp.database.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.egraf.refapp.database.local.entities.Game
import com.egraf.refapp.database.local.entities.GameWithAttributes
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface GameDao {
    @Query("SELECT * FROM Game ORDER BY date DESC")
    fun getGames(): Flow<List<GameWithAttributes>>

    @Query("SELECT * FROM Game WHERE id=(:id)")
    fun getGame(id: UUID): Flow<GameWithAttributes?>

    @Update
    fun updateGame(game: Game)

    @Query("UPDATE Game SET homeTeamId=:teamId WHERE id=:gameId")
    fun updateHomeTeam(gameId: UUID, teamId: UUID)

    @Insert
    fun addGame(game: Game)

    @Delete
    fun deleteGame(game: Game)

    @Query("DELETE FROM Game WHERE id=:gameId")
    fun deleteGame(gameId: UUID)

    @Query("SELECT COUNT(*) FROM Game")
    fun countGames(): LiveData<Int>
}