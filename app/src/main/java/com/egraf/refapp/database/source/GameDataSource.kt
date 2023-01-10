package com.egraf.refapp.database.source

import androidx.lifecycle.LiveData
import com.egraf.refapp.database.entities.*
import kotlinx.coroutines.flow.Flow
import java.util.*

interface GameDataSource {
    //    game block
    fun getGames(): LiveData<List<GameWithAttributes>>
    fun countGames(): LiveData<Int>
    fun getGame(id: UUID): LiveData<GameWithAttributes?>
    fun updateGame(game: Game)
    fun addGame(game: Game)
    fun deleteGame(game: Game)

    //    stadium block
    fun addStadium(stadium: Stadium)
    fun getStadiums(): List<Stadium>
    fun getStadium(id: UUID): Flow<Stadium?>
    fun updateStadium(stadium: Stadium)
    fun deleteStadium(stadium: Stadium)

    //    league block
    fun getLeague(id: UUID): Flow<League?>
    fun getLeagues(): List<League>
    fun addLeague(league: League)
    fun updateLeague(league: League)
    fun deleteLeague(stadium: League)

    //    team block
    fun getTeam(id: UUID): Flow<Team?>
    fun getTeams(): List<Team>
    fun deleteTeam(team: Team)
    fun addTeam(team: Team)

    //    referee block
    fun getReferee(id: UUID): Flow<Referee?>
    fun getReferees(): List<Referee>
    fun addReferee(referee: Referee)
    fun deleteReferee(stadium: Referee)
}