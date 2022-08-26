package com.egraf.refapp.dialogs.add_new_game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.egraf.refapp.GameRepository
import com.egraf.refapp.R
import com.egraf.refapp.database.entities.Game
import java.util.*

enum class AddGameDestination(val res: Int) {
    DATE_CHOOSE(0),
    TEAM_CHOOSE(R.id.action_choose_date_to_team),
    REFEREE_CHOSE(R.id.action_choose_team_to_referee),
    CLOSE(-1),
}

class AddNewGameViewModel: ViewModel() {
    private val gameRepository = GameRepository.get()
    var currentPosition = 0
        private set
    val createdGame = Game()
    val destination: LiveData<AddGameDestination?> get() = _destination
    private val _destination = MutableLiveData<AddGameDestination?>(null)

    fun showNextFragment() {
        currentPosition += 1
        _destination.value = AddGameDestination.values()[currentPosition]
    }

    fun addRandomGame() {
        gameRepository.addGame(Game())
    }
}