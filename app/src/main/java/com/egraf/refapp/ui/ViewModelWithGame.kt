package com.egraf.refapp.ui

import android.util.Log
import com.egraf.refapp.database.entities.*

private const val TAG = "AddNewGame"

open class ViewModelWithGame: ViewModelWithGameRepo() {
    var gameWithAttributes = GameWithAttributes(Game())
        private set

    fun updateGame() {
        gameRepository.updateGame(gameWithAttributes.game)
    }

    fun addGameToDB() {
        Log.d(TAG, "addGameToDB: ${gameWithAttributes.game}")
        gameRepository.addGame(gameWithAttributes.game) }

    fun setGameWithAttributes(game: GameWithAttributes) {
        gameWithAttributes = game
    }

    fun setHomeTeam(team: Team?) {
        gameWithAttributes.homeTeam = team
        gameWithAttributes.game.homeTeamId = team?.id
    }

    fun setGuestTeam(team: Team?) {
        gameWithAttributes.guestTeam = team
        gameWithAttributes.game.guestTeamId = team?.id
    }

    fun setStadium(stadium: Stadium?) {
        gameWithAttributes.stadium = stadium
        gameWithAttributes.game.stadiumId = stadium?.id
    }

    fun setLeague(league: League?) {
        gameWithAttributes.league = league
        gameWithAttributes.game.leagueId = league?.id
    }

    fun setChiefReferee(referee: Referee?) {
        gameWithAttributes.chiefReferee = referee
        gameWithAttributes.game.chiefRefereeId = referee?.id
    }

    fun setFirstReferee(referee: Referee?) {
        gameWithAttributes.firstReferee = referee
        gameWithAttributes.game.firstRefereeId = referee?.id
    }

    fun setSecondReferee(referee: Referee?) {
        gameWithAttributes.secondReferee = referee
        gameWithAttributes.game.secondRefereeId = referee?.id
    }

    fun setReserveReferee(referee: Referee?) {
        gameWithAttributes.reserveReferee = referee
        gameWithAttributes.game.reserveRefereeId = referee?.id
    }

    fun setInspector(referee: Referee?) {
        gameWithAttributes.inspector = referee
        gameWithAttributes.game.inspectorId = referee?.id
    }
}