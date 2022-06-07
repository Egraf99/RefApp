package com.egraf.refapp

import androidx.lifecycle.ViewModel
import com.egraf.refapp.database.entities.Game

class GameListViewModel: ViewModel() {
    private val gameRepository = GameRepository.get()
    val gamesListLiveData = gameRepository.getGames()
    val countGamesLiveData = gameRepository.countGames()

    fun addGame(game: Game) {
        gameRepository.addGame(game)
    }
}
