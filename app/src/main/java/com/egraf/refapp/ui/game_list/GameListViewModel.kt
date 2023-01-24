package com.egraf.refapp.ui.game_list

import com.egraf.refapp.GameRepository
import com.egraf.refapp.database.entities.GameWithAttributes
import com.egraf.refapp.ui.ViewModelWithGameRepo
import kotlinx.coroutines.flow.Flow

class GameListViewModel: ViewModelWithGameRepo() {
    val flowGames: Flow<List<GameWithAttributes>> = GameRepository.get().getGames()
}