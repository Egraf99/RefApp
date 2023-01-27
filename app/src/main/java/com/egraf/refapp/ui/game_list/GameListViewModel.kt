package com.egraf.refapp.ui.game_list

import com.egraf.refapp.GameRepository
import com.egraf.refapp.database.local.entities.GameDate
import com.egraf.refapp.ui.ViewModelWithGameRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class GameListViewModel: ViewModelWithGameRepo() {
    fun flowMapGamesWithDate(): Flow<List<GameListViewItem>> {
        class Sender(
            val date: GameDate, // Для передачи даты от предыдущего значения к следующему
            val gamesInDay: List<GameListViewItem>, // Для отделения группы игр с одинаковой датой (и упорядочивания их внутри группы)
            val acc: List<GameListViewItem> // Аккумулятор для всех игр
        )

        return GameRepository.get().getGames().map { list ->
            // объединяем игры по дате проведения (без учета времени). Ключ - дата, значение - список игр с этой датой
            list.fold(
                Sender(GameDate(LocalDate.MIN), listOf(), listOf())

            ) { sender, gwa ->
                if (sender.date != gwa.game.dateTime.date) {
                    // если дата не совпадает с предыдущей датой -
                    // то обновляем дату на новую и добавляем к аккумулятору
                    // и создаем новую группу для текущей даты с одним элементом игры
                    Sender(
                        gwa.game.dateTime.date,
                        listOf(GameListViewItem.Game(gwa)),
                        sender.acc + sender.gamesInDay + listOf(
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
                    Sender(
                        sender.date,
                        listOf(GameListViewItem.Game(gwa)) + sender.gamesInDay,
                        sender.acc
                    )
                }
            }.acc
        }
    }
}
