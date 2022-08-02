package com.egraf.refapp.ui.game_list

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.egraf.refapp.GameRepository
import com.egraf.refapp.R
import com.egraf.refapp.database.entities.Game
import com.egraf.refapp.ui.game_details.GameFragment

open class GameListViewModel: ViewModel() {
    private val gameRepository = GameRepository.get()
    val gamesListLiveData = gameRepository.getGames()
    val countGamesLiveData = gameRepository.countGames()

    fun addGame(game: Game) {
        gameRepository.addGame(game)
    }

    fun createNewGame(): Bundle {
        return GameFragment.putGameId(Game().id)
    }
}
