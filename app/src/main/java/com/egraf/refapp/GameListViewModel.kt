package com.egraf.refapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.egraf.refapp.database.entities.Game

class GameListViewModel: ViewModel() {
    private val gameRepository = GameRepository.get()
    val gamesListLiveData = gameRepository.getGames()
    val countGamesLiveData = gameRepository.countGames()

    fun addGame(game: Game) {
        gameRepository.addGame(game)
    }
}

class GameListViewModelFactory(): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(GameListViewModel::class.java)){
            return GameListViewModel() as T
        }
        throw IllegalArgumentException("UnknownViewModel")
    }
}
