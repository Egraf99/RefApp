package com.egraf.refapp.dialogs.add_new_game

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.egraf.refapp.R
import com.egraf.refapp.ui.ViewModelWithEntitiesAndGame

enum class AddGameDestination(val res: Int = 0) {
    // фрагменты (индекс последнего фрагмента = AddGameDestination.size-1 - 3 (минус действия)
    DATE_CHOOSE,
    TEAM_CHOOSE(R.id.action_choose_date_to_team),
    REFEREE_CHOSE(R.id.action_choose_team_to_referee),
    // действия
    CANCEL, PREVIOUS, CREATE;
}

class AddNewGameViewModel: ViewModelWithEntitiesAndGame() {
    var currentPosition = 0
        private set
    val destination: LiveData<out AddGameDestination?> get() = _destination
    private val _destination = MutableLiveData<AddGameDestination?>(null)

    fun setPosition(position: Int) {
        currentPosition = position
    }

    fun showPreviousFragment() {
        Log.d("AddNewGameDialog", "showPreviousFragment")
        currentPosition -= 1
        if (currentPosition < 0)
            _destination.value = AddGameDestination.CANCEL
        else
            _destination.value = AddGameDestination.PREVIOUS
    }

    fun showNextFragment() {
        currentPosition += 1
        if (currentPosition >= AddGameDestination.values().size-1 - 3)
            _destination.value = AddGameDestination.CREATE
        else
            _destination.value = AddGameDestination.values()[currentPosition]
    }
}