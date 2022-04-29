package com.egraf.refapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
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

//        обновляем игру
        gameRepository.updateGame(gameWithAttributes.game)
    }

    fun deleteGame(game: Game) {
        gameRepository.deleteGame(game)
    }
}