package com.egraf.refapp.ui.game_list

import com.egraf.refapp.database.entities.Team
import com.egraf.refapp.ui.ViewModelWithGameRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GameListViewModel @Inject constructor(): ViewModelWithGameRepo() {
    val gamesListLiveData = gameRepository.getGames()
    val countGamesLiveData = gameRepository.countGames()

    // только для проверки теста
    val teamsListLiveData = gameRepository.getTeams()
    fun addTeam(team: Team) {
        gameRepository.addTeam(team)
    }

    val getFive: () -> Int = {5}
}