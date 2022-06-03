package com.egraf.refapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.egraf.refapp.GameRepository
import com.egraf.refapp.database.entities.*
import java.util.*

class GameDetailViewModel : ViewModel() {
    private val gameRepository = GameRepository.get()
    private val gameIdLiveData = MutableLiveData<UUID>()

    val gameLiveData: LiveData<GameWithAttributes?> = Transformations.switchMap(gameIdLiveData) { gameId ->
        gameRepository.getGame(gameId)
    }
    val stadiumListLiveData: LiveData<List<Stadium>> = Transformations.switchMap(gameIdLiveData) {
        gameRepository.getStadiums()
    }
    val leagueListLiveData: LiveData<List<League>> = Transformations.switchMap(gameIdLiveData) {
        gameRepository.getLeagues()
    }
    val teamListLiveData: LiveData<List<Team>> = Transformations.switchMap(gameIdLiveData) {
        gameRepository.getTeams()
    }
    val refereeListLiveData: LiveData<List<Referee>> = Transformations.switchMap(gameIdLiveData) {
        gameRepository.getReferees()
    }

    fun loadGame(gameId: UUID) {
        gameIdLiveData.value = gameId
    }

    fun saveGame(gameWithAttributes: GameWithAttributes) {
        if (gameWithAttributes.stadium != null) {
            // добавляем стадион и обновляем id стадиона в игре
            gameWithAttributes.game.stadiumId = gameWithAttributes.stadium!!.id
            gameRepository.addStadium(gameWithAttributes.stadium!!)
        } else {
            gameWithAttributes.game.stadiumId = null
        }

        if (gameWithAttributes.league != null) {
            // добавляем лигу и обновляем id лиги в игре
            gameWithAttributes.game.leagueId = gameWithAttributes.league!!.id
            gameRepository.addLeague(gameWithAttributes.league!!)
        } else {
            gameWithAttributes.game.leagueId = null
        }

        if (gameWithAttributes.homeTeam != null) {
            // добавляем команду хозяев и обновляем id команды хозяев в игре
            gameWithAttributes.game.homeTeamId = gameWithAttributes.homeTeam!!.id
            gameRepository.addTeam(gameWithAttributes.homeTeam!!)
        } else {
            gameWithAttributes.game.homeTeamId = null
        }

        if (gameWithAttributes.guestTeam != null) {
            // добавляем команду гостей и обновляем id команды гостей в игре
            gameWithAttributes.game.guestTeamId = gameWithAttributes.guestTeam!!.id
            gameRepository.addTeam(gameWithAttributes.guestTeam!!)
        } else {
            gameWithAttributes.game.guestTeamId = null
        }

        if (gameWithAttributes.chiefReferee != null) {
            // добавляем главного судью и обновляем id главного судьи в игре
            gameWithAttributes.game.chiefRefereeId = gameWithAttributes.chiefReferee!!.id
            gameRepository.addReferee(gameWithAttributes.chiefReferee!!)
        } else {
            gameWithAttributes.game.chiefRefereeId = null
        }

        if (gameWithAttributes.firstReferee != null) {
            // добавляем главного судью и обновляем id главного судьи в игре
            gameWithAttributes.game.firstRefereeId = gameWithAttributes.firstReferee!!.id
            gameRepository.addReferee(gameWithAttributes.firstReferee!!)
        } else {
            gameWithAttributes.game.firstRefereeId = null
        }

        if (gameWithAttributes.secondReferee != null) {
            // добавляем главного судью и обновляем id главного судьи в игре
            gameWithAttributes.game.secondRefereeId = gameWithAttributes.secondReferee!!.id
            gameRepository.addReferee(gameWithAttributes.secondReferee!!)
        } else {
            gameWithAttributes.game.secondRefereeId = null
        }

        if (gameWithAttributes.reserveReferee != null) {
            // добавляем главного судью и обновляем id главного судьи в игре
            gameWithAttributes.game.reserveRefereeId = gameWithAttributes.reserveReferee!!.id
            gameRepository.addReferee(gameWithAttributes.reserveReferee!!)
        } else {
            gameWithAttributes.game.reserveRefereeId = null
        }

        if (gameWithAttributes.inspector != null) {
            // добавляем главного судью и обновляем id главного судьи в игре
            gameWithAttributes.game.inspectorId = gameWithAttributes.inspector!!.id
            gameRepository.addReferee(gameWithAttributes.inspector!!)
        } else {
            gameWithAttributes.game.inspectorId = null
        }

//        обновляем игру
        gameRepository.updateGame(gameWithAttributes.game)
    }

    fun saveStadium(gameWithAttributes: GameWithAttributes, stadium: Stadium) {
        gameRepository.addStadium(stadium)
        gameWithAttributes.game.stadiumId = stadium.id
        gameRepository.updateGame(gameWithAttributes.game)
    }

    fun saveLeague(gameWithAttributes: GameWithAttributes, league: League) {
        gameRepository.addLeague(league)
        gameWithAttributes.game.leagueId = league.id
        gameRepository.updateGame(gameWithAttributes.game)
    }

    fun saveHomeTeam(gameWithAttributes: GameWithAttributes, team: Team) {
        gameRepository.addTeam(team)
        gameWithAttributes.game.homeTeamId = team.id
        gameRepository.updateGame(gameWithAttributes.game)
    }

    fun saveGuestTeam(gameWithAttributes: GameWithAttributes, team: Team) {
        gameRepository.addTeam(team)
        gameWithAttributes.game.guestTeamId = team.id
        gameRepository.updateGame(gameWithAttributes.game)
    }

    fun saveChiefReferee(gameWithAttributes: GameWithAttributes, referee: Referee) {
        gameRepository.addReferee(referee)
        gameWithAttributes.game.chiefRefereeId = referee.id
        gameRepository.updateGame(gameWithAttributes.game)
    }

    fun saveFirstReferee(gameWithAttributes: GameWithAttributes, referee: Referee) {
        gameRepository.addReferee(referee)
        gameWithAttributes.game.firstRefereeId = referee.id
        gameRepository.updateGame(gameWithAttributes.game)
    }

    fun saveSecondReferee(gameWithAttributes: GameWithAttributes, referee: Referee) {
        gameRepository.addReferee(referee)
        gameWithAttributes.game.secondRefereeId = referee.id
        gameRepository.updateGame(gameWithAttributes.game)
    }

    fun saveReserveReferee(gameWithAttributes: GameWithAttributes, referee: Referee) {
        gameRepository.addReferee(referee)
        gameWithAttributes.game.reserveRefereeId = referee.id
        gameRepository.updateGame(gameWithAttributes.game)
    }

    fun saveInspector(gameWithAttributes: GameWithAttributes, referee: Referee) {
        gameRepository.addReferee(referee)
        gameWithAttributes.game.inspectorId = referee.id
        gameRepository.updateGame(gameWithAttributes.game)
    }

    fun deleteGame(game: Game) {
        gameRepository.deleteGame(game)
    }
}