package com.egraf.refapp.dialogs.add_new_game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.egraf.refapp.R
import com.egraf.refapp.ui.ViewModelWithEntitiesAndGame

enum class AddGameDestination(val res: Int) {
    DATE_CHOOSE(0),
    TEAM_CHOOSE(R.id.action_choose_date_to_team),
    REFEREE_CHOSE(R.id.action_choose_team_to_referee),
    CLOSE(-1),
    // PREVIOUS должен быть последним в списке
    PREVIOUS(-2)
}

class AddNewGameViewModel: ViewModelWithEntitiesAndGame() {
    var currentPosition = 0
        private set
    val destination: LiveData<AddGameDestination?> get() = _destination
    private val _destination = MutableLiveData<AddGameDestination?>(null)

    fun setPosition(position: Int) {
        currentPosition = position
    }

    fun showPreviousFragment() {
        currentPosition -= 1
        _destination.value = AddGameDestination.values().last() // PREVIOUS destination
    }

    fun showNextFragment() {
        currentPosition += 1
        _destination.value = AddGameDestination.values()[currentPosition]
    }
}