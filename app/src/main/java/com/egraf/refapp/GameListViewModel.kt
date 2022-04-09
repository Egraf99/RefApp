package com.egraf.refapp

import androidx.lifecycle.ViewModel

class GameListViewModel: ViewModel() {
    val games = mutableListOf<Game>()

    init {
        for (i in 0..100) {
            val game = Game()
            game.homeTeam = "Home Team #$i"
            game.guestTeam = "Guest Team #$i"
            game.league = "League #$i"
            game.stadium = "Stadium #$i"
            game.isPaid = i % 3 == 0
            games += game
        }
    }
}