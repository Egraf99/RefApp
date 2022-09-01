package com.egraf.refapp.ui.game_list

import com.egraf.refapp.ui.ViewModelWithGameRepo

class GameListViewModel: ViewModelWithGameRepo() {
    val gamesListLiveData = gameRepository.getGames()
    val countGamesLiveData = gameRepository.countGames()
}