package com.egraf.refapp.ui.dialogs.add_new_game

import com.egraf.refapp.R
import com.egraf.refapp.ui.ViewModelWithEntitiesAndGame
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

enum class AddGameDestination(val res: Int = 0) {
    // фрагменты (индекс последнего фрагмента = AddGameDestination.size-1 - countDestinations (минус действия)
    STADIUM_CHOOSE,
    TEAM_CHOOSE(R.id.action_choose_date_to_team),
    REFEREE_CHOSE(R.id.action_choose_team_to_referee),

    // действия
    CANCEL, PREVIOUS, CREATE;

    companion object {
        const val countDestinations = 3
    }
}

class AddNewGameViewModel: ViewModelWithEntitiesAndGame() {
    var currentPosition = 0
        private set
    val destination: StateFlow<AddGameDestination?> get() = _destination
    private val _destination = MutableStateFlow<AddGameDestination?>(null)

    fun setPosition(position: Int) {
        currentPosition = position
    }

    fun showPreviousFragment() {
        currentPosition -= 1
        if (currentPosition < 0)
            _destination.value = AddGameDestination.CANCEL
        else
            _destination.value = AddGameDestination.PREVIOUS
    }

    fun showNextFragment() {
        currentPosition += 1
        if (currentPosition > AddGameDestination.values().size - 1 - AddGameDestination.countDestinations)
            _destination.value = AddGameDestination.CREATE
        else
            _destination.value = AddGameDestination.values()[currentPosition]
    }
}