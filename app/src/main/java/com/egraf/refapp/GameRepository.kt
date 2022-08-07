package com.egraf.refapp

import android.content.Context
import androidx.lifecycle.LiveData
import com.egraf.refapp.database.entities.*
import com.egraf.refapp.database.source.GameDataSource
import java.util.*

class GameRepository private constructor(private val gameDataSource: GameDataSource) {
    //    game block
    fun getGames(): LiveData<List<GameWithAttributes>> = gameDataSource.getGames()
    fun countGames(): LiveData<Int> = gameDataSource.countGames()
    fun getGame(id: UUID): LiveData<GameWithAttributes?> = gameDataSource.getGame(id)
    fun updateGame(game: Game) = gameDataSource.updateGame(game)
    fun addGame(game: Game) = gameDataSource.addGame(game)
    fun deleteGame(game: Game) = gameDataSource.deleteGame(game)

    //    stadium block
    fun addStadium(stadium: Stadium) = gameDataSource.addStadium(stadium)
    fun getStadiums(): LiveData<List<Stadium>> = gameDataSource.getStadiums()
    fun getStadium(id: UUID): LiveData<Stadium?> = gameDataSource.getStadium(id)
    fun updateStadium(stadium: Stadium) = gameDataSource.updateStadium(stadium)

    //    league block
    fun getLeagues(): LiveData<List<League>> = gameDataSource.getLeagues()
    fun addLeague(league: League) = gameDataSource.addLeague(league)

    fun updateLeague(league: League) = gameDataSource.updateLeague(league)

    //    team block
    fun getTeams(): LiveData<List<Team>> = gameDataSource.getTeams()
    fun deleteTeam(team: Team) = gameDataSource.deleteTeam(team)
    fun addTeam(team: Team) = gameDataSource.addTeam(team)

    //    referee block
    fun getReferees(): LiveData<List<Referee>> = gameDataSource.getReferees()
    fun addReferee(referee: Referee) = gameDataSource.addReferee(referee)

    companion object {
        private var INSTANCE: GameRepository? = null

        fun initialize(gameDataSource: GameDataSource) {
            if (INSTANCE == null) INSTANCE = GameRepository(gameDataSource)
        }

        fun get(): GameRepository {
            return INSTANCE ?: throw IllegalStateException("GameRepository must be initialized")
        }
    }
}