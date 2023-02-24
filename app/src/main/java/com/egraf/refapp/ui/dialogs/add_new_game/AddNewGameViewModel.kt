package com.egraf.refapp.ui.dialogs.add_new_game

import androidx.lifecycle.ViewModel
import com.egraf.refapp.GameRepository
import com.egraf.refapp.database.local.entities.Game

class AddNewGameViewModel: ViewModel() {
    var fragmentPosition = Position.FIRST
    var counterPosition = 1

    fun addGameToDB(game: Game) = GameRepository.get().addGame(game)
}