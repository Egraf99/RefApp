package com.egraf.refapp.ui.game_list

import com.egraf.refapp.GameRepository
import com.egraf.refapp.database.entities.GameDate
import com.egraf.refapp.database.entities.GameWithAttributes
import com.egraf.refapp.ui.ViewModelWithGameRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GameListViewModel: ViewModelWithGameRepo() {
    val flowMapGamesWithDate: Flow<Map<GameDate, List<GameWithAttributes>>> = GameRepository.get().getGames().map { list ->
        // объединяем игры по дате проведения (без учета времени). Ключ - дата, значение - список игр с этой датой
        list.fold(mapOf()) { map, gwa ->
            val date = gwa.game.dateTime.date
            map + Pair(date, map.getOrDefault(date, listOf()) + gwa)
        }
    }
}