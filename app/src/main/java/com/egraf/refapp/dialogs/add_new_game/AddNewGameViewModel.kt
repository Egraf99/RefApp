package com.egraf.refapp.dialogs.add_new_game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.egraf.refapp.R

enum class AddGameDestination(val res: Int) {
    DATE_CHOOSE(0),
    TEAM_CHOOSE(R.id.action_choose_date_to_team)
}

class AddNewGameViewModel: ViewModel() {
    val destination: LiveData<AddGameDestination?> get() = _destination
    private val _destination = MutableLiveData<AddGameDestination?>(null)

    fun showNextFragment() {
        _destination.value = AddGameDestination.TEAM_CHOOSE
    }
}