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

    @Query("UPDATE Game SET guestTeamId=:teamId WHERE id=:gameId")
    fun updateGuestTeam(gameId: UUID, teamId: UUID)

    @Query("UPDATE Game SET stadiumId=:stadiumId WHERE id=:gameId")
    fun updateStadium(gameId: UUID, stadiumId: UUID)

    @Query("UPDATE Game SET leagueId=:leagueId WHERE id=:gameId")
    fun updateLeague(gameId: UUID, leagueId: UUID)

    @Query("UPDATE Game SET chiefRefereeId=:refereeId WHERE id=:gameId")
    fun updateChiefReferee(gameId: UUID, refereeId: UUID)

    @Query("UPDATE Game SET firstRefereeId=:refereeId WHERE id=:gameId")
    fun updateFirstAssistant(gameId: UUID, refereeId: UUID)

    @Query("UPDATE Game SET secondRefereeId=:refereeId WHERE id=:gameId")
    fun updateSecondAssistant(gameId: UUID, refereeId: UUID)

    @Query("UPDATE Game SET reserveRefereeId=:refereeId WHERE id=:gameId")
    fun updateReserveReferee(gameId: UUID, refereeId: UUID)

    @Query("UPDATE Game SET inspectorId=:refereeId WHERE id=:gameId")
    fun updateInspector(gameId: UUID, refereeId: UUID)

    @Insert
    fun addGame(game: Game)

    @Delete
    fun deleteGame(game: Game)

    @Query("DELETE FROM Game WHERE id=:gameId")
    fun deleteGame(gameId: UUID)

    @Query("SELECT COUNT(*) FROM Game")
    fun countGames(): LiveData<Int>
}