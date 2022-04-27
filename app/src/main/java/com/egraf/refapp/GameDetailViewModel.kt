package com.egraf.refapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.egraf.refapp.database.entities.Game
import com.egraf.refapp.database.entities.GameWithAttributes
import com.egraf.refapp.database.entities.Stadium
import java.util.*

class GameDetailViewModel : ViewModel() {
    private val gameRepository = GameRepository.get()
    private val gameIdLiveData = MutableLiveData<UUID>()
    private val stadiumIdLiveData = MutableLiveData<UUID>()

    val gameLiveData: LiveData<GameWithAttributes?> = Transformations.switchMap(gameIdLiveData) { gameId ->
        gameRepository.getGame(gameId)
    }

    val stadiumListLiveData: LiveData<List<Stadium>> = Transformations.switchMap(gameIdLiveData) {
        gameRepository.getStadiums()
    }

    val stadiumLiveData: LiveData<Stadium?> = Transformations.switchMap(stadiumIdLiveData) {stadiumId ->
        gameRepository.getStadium(stadiumId)
    }

    fun loadGame(gameId: UUID) {
        gameIdLiveData.value = gameId
    }

    fun loadStadium(stadiumId: UUID) {
        stadiumIdLiveData.value = stadiumId
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
            // добавляем команду хозяев и обновляем id команды хозяев в игре
            gameWithAttributes.game.guestTeamId = gameWithAttributes.guestTeam!!.id
            gameRepository.addTeam(gameWithAttributes.guestTeam!!)
        } else {
            gameWithAttributes.game.guestTeamId = null
        }

//        обновляем игру
        gameRepository.updateGame(gameWithAttributes.game)
    }

    fun deleteGame(game: Game) {
        gameRepository.deleteGame(game)
    }
}