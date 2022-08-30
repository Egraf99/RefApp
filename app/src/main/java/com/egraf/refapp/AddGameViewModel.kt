package com.egraf.refapp

import androidx.databinding.BaseObservable

private const val TAG = "AddGameViewModel"

class AddGameViewModel(private val callbacks: Callbacks): BaseObservable() {
    interface Callbacks {
        fun addNewGame()
    }

    fun addGame() {
        callbacks.addNewGame()
    }
}