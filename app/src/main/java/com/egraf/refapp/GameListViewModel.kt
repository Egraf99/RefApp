package com.egraf.refapp

import androidx.lifecycle.ViewModel

class GameListViewModel: ViewModel() {

    private val gameRepository = GameRepository.get()
    val gamesListLiveData = gameRepository.getGames()

    fun addGame(game: Game) {gameRepository.addGame(game)}
}