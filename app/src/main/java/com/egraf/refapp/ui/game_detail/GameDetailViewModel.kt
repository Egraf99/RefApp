package com.egraf.refapp.ui.game_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.egraf.refapp.GameRepository
import com.egraf.refapp.database.entities.*
import com.egraf.refapp.interface_viewmodel.all.LeagueInterface
import com.egraf.refapp.interface_viewmodel.all.RefereeInterface
import com.egraf.refapp.interface_viewmodel.all.StadiumInterface
import com.egraf.refapp.interface_viewmodel.all.TeamInterface
import java.util.*

class GameDetailViewModel : ViewModel(),
TeamInterface, LeagueInterface, StadiumInterface, RefereeInterface {
    private val nullId = UUID.randomUUID()

    var gameWithAttributes = GameWithAttributes(Game())
        private set
    private val gameRepository = GameRepository.get()
    private val gameIdLiveData = MutableLiveData<UUID>()

    val gameLiveData: LiveData<GameWithAttributes?> =
        Transformations.switchMap(gameIdLiveData) { gameId ->
            gameRepository.getGame(gameId)
        }

    fun loadGame(gameId: UUID) {
        gameIdLiveData.value = gameId
    }

    fun updateGame() {
        gameRepository.updateGame(gameWithAttributes.game)
    }

    fun setGameWithAttributes(game: GameWithAttributes) {
        gameWithAttributes = game
    }

    fun setHomeTeam(team: Team?) {
        gameWithAttributes.homeTeam = team
        gameWithAttributes.game.homeTeamId = team?.id ?: nullId
    }

    fun setGuestTeam(team: Team?) {
        gameWithAttributes.guestTeam = team
        gameWithAttributes.game.guestTeamId = team?.id ?: nullId
    }

    fun setStadium(stadium: Stadium?) {
        gameWithAttributes.stadium = stadium
        gameWithAttributes.game.stadiumId = stadium?.id ?: nullId
    }

    fun setLeague(league: League?) {
        gameWithAttributes.league = league
        gameWithAttributes.game.leagueId = league?.id ?: nullId
    }

    fun setChiefReferee(referee: Referee?) {
        gameWithAttributes.chiefReferee = referee
        gameWithAttributes.game.chiefRefereeId = referee?.id ?: nullId
    }

    fun setFirstReferee(referee: Referee?) {
        gameWithAttributes.firstReferee = referee
        gameWithAttributes.game.firstRefereeId = referee?.id ?: nullId
    }

    fun setSecondReferee(referee: Referee?) {
        gameWithAttributes.secondReferee = referee
        gameWithAttributes.game.secondRefereeId = referee?.id ?: nullId
    }

    fun setReserveReferee(referee: Referee?) {
        gameWithAttributes.reserveReferee = referee
        gameWithAttributes.game.reserveRefereeId = referee?.id ?: nullId
    }

    fun setInspector(referee: Referee?) {
        gameWithAttributes.inspector = referee
        gameWithAttributes.game.inspectorId = referee?.id ?: nullId
    }

    fun deleteGame(game: Game) {
        gameRepository.deleteGame(game)
    }

    override fun addTeamToDB(team: Team) {
        gameRepository.addTeam(team)
    }

    override fun getTeamsFromDB(): LiveData<List<Team>> {
        return gameRepository.getTeams()
    }

    override fun addLeagueToDB(league: League) {
        gameRepository.addLeague(league)
    }

    override fun addStadiumToDB(stadium: Stadium) {
        gameRepository.addStadium(stadium)
    }

    override fun getLeagueFromDB(): LiveData<List<League>> {
        return gameRepository.getLeagues()
    }

    override fun getStadiumsFromDB(): LiveData<List<Stadium>> {
        return gameRepository.getStadiums()
    }

    override fun addRefereeToDB(referee: Referee) {
        gameRepository.addReferee(referee)
    }

    override fun getRefereeFromDB(): LiveData<List<Referee>> {
        return gameRepository.getReferees()
    }
}