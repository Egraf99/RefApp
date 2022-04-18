package com.egraf.refapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.egraf.refapp.database.entities.Game
import com.egraf.refapp.database.entities.GameWithStadium
import com.egraf.refapp.database.entities.Stadium
import java.util.*

class GameDetailViewModel : ViewModel() {
    private val gameRepository = GameRepository.get()
    private val gameIdLiveData = MutableLiveData<UUID>()

    val gameLiveData: LiveData<GameWithStadium?> = Transformations.switchMap(gameIdLiveData) { gameId ->
        gameRepository.getGame(gameId)
    }

    fun loadGame(gameId: UUID) {
        gameIdLiveData.value = gameId
    }

    fun saveGame(game: Game, stadium: Stadium) {
        gameRepository.updateGame(game)
        gameRepository.updateStadium(stadium)
    }

    fun deleteGame(game: Game) {
        gameRepository.deleteGame(game)
    }
}