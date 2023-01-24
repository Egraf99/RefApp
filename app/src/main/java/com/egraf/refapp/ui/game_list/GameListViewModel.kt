package com.egraf.refapp.ui.game_list

import com.egraf.refapp.GameRepository
import com.egraf.refapp.database.entities.GameDate
import com.egraf.refapp.database.entities.GameWithAttributes
import com.egraf.refapp.ui.ViewModelWithGameRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class GameListViewModel: ViewModelWithGameRepo() {
    val flowMapGamesWithDate: Flow<List<GameListViewItem>> =
        GameRepository.get().getGames().map { list ->
            // объединяем игры по дате проведения (без учета времени). Ключ - дата, значение - список игр с этой датой
//        list.fold(mapOf()) { map, gwa ->
//            val date = gwa.game.dateTime.date
//            map + Pair(date, map.getOrDefault(date, listOf()) + gwa)
//        }
            list.fold(
                // Triple:
                // первое значение для передачи даты от предыдущего значения к следующему
                // второе значение для отделения группы игр с одинаковой датой (и упорядочивания их внутри группы)
                // третье значение аккумулирующее для всех итемов
                Triple<GameDate, List<GameListViewItem>, List<GameListViewItem>>(
                    GameDate(LocalDate.MIN),
                    listOf(),
                    listOf()
                )
            ) { triple, gwa ->
                if (triple.first != gwa.game.dateTime.date) {
                    // если дата не совпадает с предыдущей датой -
                    // то обновляем дату на новую и добавляем к аккумулятору
                    // и создаем новую группу для текущей даты с одним элементом игры
                    Triple(
                        gwa.game.dateTime.date,
                        listOf(GameListViewItem.Game(gwa)),
                        triple.third + triple.second + listOf(
                            GameListViewItem.Date(
                                gwa.game.dateTime.date
                            )
                        )
                    )
                } else {
                    // если дата совпадает с предыдущей -
                    // то оставляем предыдущую дату,
                    // добавляем к группе игр с одинаковой датой итем с игрой
                    //      (сначала новую игру, потом остальной список, чтобы игры шли по убыванию даты, но возрастанию времени),
                    // к аккумулятору ничего не добавляем
                    Triple(
                        triple.first,
                        listOf(GameListViewItem.Game(gwa)) + triple.second,
                        triple.third
                    )
                }
            }.third
        }
}