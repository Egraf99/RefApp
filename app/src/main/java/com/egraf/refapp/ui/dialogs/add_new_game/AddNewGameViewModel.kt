package com.egraf.refapp.ui.dialogs.add_new_game

import android.util.Log
import com.egraf.refapp.R
import com.egraf.refapp.ui.ViewModelWithEntitiesAndGame
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AddNewGameViewModel: ViewModelWithEntitiesAndGame() {
    var currentPosition = Position.FIRST
//        private set
//    val destination: StateFlow<AddGameDestination?> get() = _destination
//    private val _destination = MutableStateFlow<AddGameDestination?>(null)
//
//    fun setPosition(position: Int) {
//        currentPosition = position
//    }
//
//    fun showPreviousFragment() {
//        currentPosition -= 1
//        if (currentPosition < 0)
//            _destination.value = AddGameDestination.CANCEL
//        else {
//            _destination.value = AddGameDestination.PREVIOUS
//            Log.d("AddNewGame", "showPreviousFragment: $currentPosition")
//        }
//    }
//
//    fun showNextFragment() {
//        currentPosition += 1
//        if (currentPosition > AddGameDestination.values().size - 1 - AddGameDestination.countDestinations)
//            _destination.value = AddGameDestination.CREATE
//        else
//            _destination.value = AddGameDestination.values()[currentPosition]
//    }
}