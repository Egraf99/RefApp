package com.egraf.refapp

import androidx.lifecycle.LiveData
import com.egraf.refapp.database.entities.*
import com.egraf.refapp.database.source.GameDataSource
import java.util.*


class GameRepository private constructor(private val dataSource: GameDataSource) {
    //    game block
    fun getGames(): LiveData<List<GameWithAttributes>> = dataSource.getGames()
    fun countGames(): LiveData<Int> = dataSource.countGames()
    fun getGame(id: UUID): LiveData<GameWithAttributes?> = dataSource.getGame(id)
    fun updateGame(game: Game) = dataSource.updateGame(game)
    fun addGame(game: Game) = dataSource.addGame(game)
    fun deleteGame(game: Game) = dataSource.deleteGame(game)

    //    stadium block
    fun addStadium(stadium: Stadium) = dataSource.addStadium(stadium)

    fun getStadiums(): LiveData<List<Stadium>> = dataSource.getStadiums()
    fun getStadium(id: UUID): LiveData<Stadium?> = dataSource.getStadium(id)
    fun updateStadium(stadium: Stadium) = dataSource.updateStadium(stadium)

    //    league block
    fun getLeagues(): LiveData<List<League>> = dataSource.getLeagues()
    fun addLeague(league: League) = dataSource.addLeague(league)
    fun updateLeague(league: League) = dataSource.updateLeague(league)

    //    team block
    fun getTeams(): LiveData<List<Team>> = dataSource.getTeams()
    fun addTeam(team: Team) = dataSource.addTeam(team)

    //    referee block
    fun getReferees(): LiveData<List<Referee>> = dataSource.getReferees()
    fun addReferee(referee: Referee) = dataSource.addReferee(referee)

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