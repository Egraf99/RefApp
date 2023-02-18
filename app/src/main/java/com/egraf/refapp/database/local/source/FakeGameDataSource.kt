package com.egraf.refapp.database.local.source

import androidx.lifecycle.LiveData
import com.egraf.refapp.database.local.entities.*
import com.egraf.refapp.database.remote.model.open_weather_pojo.WeatherResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import java.util.*

class FakeGameDataSource() : GameDataSource {
    override fun getGames(): Flow<List<GameWithAttributes>> {
        TODO("Not yet implemented")
    }

    override fun countGames(): LiveData<Int> {
        TODO("Not yet implemented")
    }

    override fun getGame(id: UUID): Flow<GameWithAttributes?> {
        TODO("Not yet implemented")
    }

    override fun updateGame(game: Game) {
        TODO("Not yet implemented")
    }

    override fun updateHomeTeamInGame(gameId: UUID, teamId: UUID) {
        TODO("Not yet implemented")
    }

    override fun updateGuestTeamInGame(gameId: UUID, teamId: UUID) {
        TODO("Not yet implemented")
    }

    override fun updateStadiumInGame(gameId: UUID, stadiumId: UUID) {
        TODO("Not yet implemented")
    }

    override fun updateLeagueInGame(gameId: UUID, leagueId: UUID) {
        TODO("Not yet implemented")
    }

    override fun updateChiefRefereeInGame(gameId: UUID, refereeId: UUID) {
        TODO("Not yet implemented")
    }

    override fun updateFirstAssistantInGame(gameId: UUID, refereeId: UUID) {
        TODO("Not yet implemented")
    }

    override fun updateSecondAssistantInGame(gameId: UUID, refereeId: UUID) {
        TODO("Not yet implemented")
    }

    override fun updateReserveRefereeInGame(gameId: UUID, refereeId: UUID) {
        TODO("Not yet implemented")
    }

    override fun updateInspectorInGame(gameId: UUID, refereeId: UUID) {
        TODO("Not yet implemented")
    }

    override fun updateDateTimeInGame(gameId: UUID, dt: GameDateTime) {
        TODO("Not yet implemented")
    }

    override fun updatePassedGame(gameId: UUID, isPassed: Boolean) {
        TODO("Not yet implemented")
    }

    override fun updatePaidGame(gameId: UUID, isPaid: Boolean) {
        TODO("Not yet implemented")
    }

    override fun addGame(game: Game) {
        TODO("Not yet implemented")
    }

    override fun deleteGame(game: Game) {
        TODO("Not yet implemented")
    }

    override fun deleteGame(gameId: UUID) {
        TODO("Not yet implemented")
    }

    override fun addStadium(stadium: Stadium) {
        TODO("Not yet implemented")
    }

    override fun getStadiums(): List<Stadium> {
        TODO("Not yet implemented")
    }

    override fun getStadium(id: UUID): Flow<Stadium?> {
        TODO("Not yet implemented")
    }

    override fun updateStadium(stadium: Stadium) {
        TODO("Not yet implemented")
    }

    override fun updateStadiumTitle(stadiumId: UUID, title: String) {
        TODO("Not yet implemented")
    }

    override fun deleteStadium(stadium: Stadium) {
        TODO("Not yet implemented")
    }

    override fun getLeague(id: UUID): Flow<League?> {
        TODO("Not yet implemented")
    }

    override fun getLeagues(): List<League> {
        TODO("Not yet implemented")
    }

    override fun addLeague(league: League) {
        TODO("Not yet implemented")
    }

    override fun updateLeague(league: League) {
        TODO("Not yet implemented")
    }

    override fun updateLeagueTitle(leagueId: UUID, title: String) {
        TODO("Not yet implemented")
    }

    override fun deleteLeague(league: League) {
        TODO("Not yet implemented")
    }

    override fun getTeam(id: UUID): Flow<Team?> {
        TODO("Not yet implemented")
    }

    override fun getTeams(): List<Team> {
        TODO("Not yet implemented")
    }

    override fun deleteTeam(team: Team) {
        TODO("Not yet implemented")
    }

    override fun updateTeamName(teamId: UUID, name: String) {
        TODO("Not yet implemented")
    }

    override fun addTeam(team: Team) {
        TODO("Not yet implemented")
    }

    override fun getReferee(id: UUID): Flow<Referee?> {
        TODO("Not yet implemented")
    }

    override fun getReferees(): List<Referee> {
        TODO("Not yet implemented")
    }

    override fun addReferee(referee: Referee) {
        TODO("Not yet implemented")
    }

    override fun deleteReferee(referee: Referee) {
        TODO("Not yet implemented")
    }

    override fun updateRefereeFirstName(refereeId: UUID, firstName: String) {
        TODO("Not yet implemented")
    }

    override fun updateRefereeMiddleName(refereeId: UUID, middleName: String) {
        TODO("Not yet implemented")
    }

    override fun updateRefereeLastName(refereeId: UUID, lastName: String) {
        TODO("Not yet implemented")
    }

    override fun getWeathersList(): Call<WeatherResponse> {
        TODO("Not yet implemented")
    }
}