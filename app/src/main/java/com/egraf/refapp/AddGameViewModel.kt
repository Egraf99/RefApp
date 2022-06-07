package com.egraf.refapp

import androidx.databinding.BaseObservable
import com.egraf.refapp.database.entities.Game
import java.util.*

private const val TAG = "AddGameViewModel"

class AddGameViewModel(private val callbacks: Callbacks): BaseObservable() {
    interface Callbacks {
        fun onGameSelected(gameId: UUID)
    }
    private val gameRepository = GameRepository.get()

    fun addGame() {
        val game = Game()
        gameRepository.addGame(game)
        callbacks.onGameSelected(game.id)
    }
}