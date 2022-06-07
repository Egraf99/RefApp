package com.egraf.refapp

import androidx.databinding.BaseObservable
import androidx.navigation.Navigation
import com.egraf.refapp.database.entities.Game
import java.util.*

private const val TAG = "AddGameViewModel"

class AddGameViewModel(private val callbacks: Callbacks): BaseObservable() {
    interface Callbacks {
        fun addNewGame()
    }

    fun addGame() {
        callbacks.addNewGame()
    }
}