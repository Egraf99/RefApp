package com.egraf.refapp

import androidx.lifecycle.LiveData
import com.egraf.refapp.database.local.entities.*
import com.egraf.refapp.database.local.source.GameDataSource
import com.egraf.refapp.utils.getIfNotEmpty
import kotlinx.coroutines.flow.Flow
import java.util.*


class GameRepository private constructor(private val dataSource: GameDataSource) {
    //    game block
    fun getGames(): Flow<List<GameWithAttributes>> = dataSource.getGames()
    fun countGames(): LiveData<Int> = dataSource.countGames()
    fun getGame(id: UUID): Flow<GameWithAttributes?> = dataSource.getGame(id)
    fun updateGame(game: Game) = dataSource.updateGame(game)
    fun updateHomeTeamInGame(gameId: UUID, teamId: UUID) = dataSource.updateHomeTeamInGame(gameId, teamId)
    fun addGame(game: Game) = dataSource.addGame(game)

    fun deleteGame(game: Game) = dataSource.deleteGame(game)
    fun deleteGame(gameId: UUID) = dataSource.deleteGame(gameId)

    //    stadium block
    fun addStadium(stadium: Stadium) = dataSource.addStadium(stadium)

    fun getStadiums(): List<Stadium> = dataSource.getStadiums()
    fun getStadium(id: UUID): Flow<Stadium?> = dataSource.getStadium(id)
    fun updateStadium(stadium: Stadium) = dataSource.updateStadium(stadium)
    fun deleteStadium(stadium: Stadium) = dataSource.deleteStadium(stadium)

    //    league block
    fun getLeague(id: UUID): Flow<League?> = dataSource.getLeague(id)
    fun getLeagues(): List<League> = dataSource.getLeagues()
    fun addLeague(league: League) = dataSource.addLeague(league)
    fun updateLeague(league: League) = dataSource.updateLeague(league)
    fun deleteLeague(stadium: League) = dataSource.deleteLeague(stadium)

    //    team block
    fun getTeam(id: UUID): Flow<Team?> = dataSource.getTeam(id)
    fun getTeams(): List<Team> = dataSource.getTeams()
    fun addTeam(team: Team) = dataSource.addTeam(team)
    fun deleteTeam(team: Team) = dataSource.deleteTeam(team)

    //    referee block
    fun getReferee(id: UUID): Flow<Referee?> = dataSource.getReferee(id)
    fun getReferees(): List<Referee> = dataSource.getReferees()
    fun addReferee(referee: Referee) = dataSource.addReferee(referee)
    fun deleteReferee(stadium: Referee) = dataSource.deleteReferee(stadium)

    // weather block
    fun getWeatherBy3h() = dataSource.getWeathersList()

    companion object {
        private var INSTANCE: GameRepository? = null

        fun initialize(dataSource: GameDataSource) {
            if (INSTANCE == null) INSTANCE = GameRepository(dataSource)
        }

        fun get(): GameRepository {
            return INSTANCE ?: throw IllegalStateException("GameRepository must be initialized")
        }
    }
}