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

    //    league block
    fun getLeagues(): List<League>
    fun addLeague(league: League)
    fun updateLeague(league: League)

    //    team block
    fun getTeams(): List<Team>
    fun deleteTeam(team: Team)
    fun addTeam(team: Team)

    //    referee block
    fun getReferees(): LiveData<List<Referee>>
    fun addReferee(referee: Referee)
}