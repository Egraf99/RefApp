package com.egraf.refapp.database.local.source

import androidx.lifecycle.LiveData
import com.egraf.refapp.database.local.entities.*
import com.egraf.refapp.database.remote.model.open_weather_pojo.WeatherResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import java.util.*

interface GameDataSource {
    //    game block
    fun getGames(): Flow<List<GameWithAttributes>>
    fun countGames(): LiveData<Int>
    fun getGame(id: UUID): Flow<GameWithAttributes?>
    fun updateGame(game: Game)
    fun updateHomeTeamInGame(gameId: UUID, teamId: UUID)
    fun updateGuestTeamInGame(gameId: UUID, teamId: UUID)
    fun updateStadiumInGame(gameId: UUID, stadiumId: UUID)
    fun updateLeagueInGame(gameId: UUID, leagueId: UUID)
    fun updateChiefRefereeInGame(gameId: UUID, refereeId: UUID)
    fun updateFirstAssistantInGame(gameId: UUID, refereeId: UUID)
    fun updateSecondAssistantInGame(gameId: UUID, refereeId: UUID)
    fun updateReserveRefereeInGame(gameId: UUID, refereeId: UUID)
    fun updateInspectorInGame(gameId: UUID, refereeId: UUID)
    fun updateDateTimeInGame(gameId: UUID, dt: GameDateTime)
    fun updatePassedGame(gameId: UUID, isPassed: Boolean)
    fun updatePaidGame(gameId: UUID, isPaid: Boolean)
    fun addGame(game: Game)
    fun deleteGame(game: Game)
    fun deleteGame(gameId: UUID)

    //    stadium block
    fun addStadium(stadium: Stadium)
    fun getStadiums(): List<Stadium>
    fun getStadium(id: UUID): Flow<Stadium?>
    fun updateStadium(stadium: Stadium)
    fun updateStadiumTitle(stadiumId: UUID, title: String)
    fun deleteStadium(stadium: Stadium)

    //    league block
    fun getLeague(id: UUID): Flow<League?>
    fun getLeagues(): List<League>
    fun addLeague(league: League)
    fun updateLeague(league: League)
    fun updateLeagueTitle(leagueId: UUID, title: String)
    fun deleteLeague(league: League)

    //    team block
    fun getTeam(id: UUID): Flow<Team?>
    fun getTeams(): List<Team>
    fun deleteTeam(team: Team)
    fun updateTeamName(teamId: UUID, name: String)
    fun addTeam(team: Team)

    //    referee block
    fun getReferee(id: UUID): Flow<Referee?>
    fun getReferees(): List<Referee>
    fun addReferee(referee: Referee)
    fun deleteReferee(referee: Referee)
    fun updateRefereeFirstName(refereeId: UUID, firstName: String)
    fun updateRefereeMiddleName(refereeId: UUID, middleName: String)
    fun updateRefereeLastName(refereeId: UUID, lastName: String)

    // weather
    fun getWeathersList(): Call<WeatherResponse>
}