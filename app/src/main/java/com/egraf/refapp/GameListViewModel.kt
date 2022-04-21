package com.egraf.refapp

import androidx.lifecycle.ViewModel
import com.egraf.refapp.database.entities.Game
import com.egraf.refapp.database.entities.GameWithAttributes
import com.egraf.refapp.database.entities.Stadium

class GameListViewModel: ViewModel() {
    private val gameRepository = GameRepository.get()
    val gamesListLiveData = gameRepository.getGames()
    val countGamesLiveData = gameRepository.countGames()

    fun addGame(game: Game) {
        gameRepository.addGame(game)
    }
}