package com.egraf.refapp.ui.game_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egraf.refapp.GameRepository
import com.egraf.refapp.database.local.entities.GameDate
import com.egraf.refapp.database.remote.Common
import com.egraf.refapp.database.remote.model.WeatherResponse
import com.egraf.refapp.database.remote.service.WeatherApi
import com.egraf.refapp.ui.ViewModelWithGameRepo
import com.egraf.refapp.utils.Resource
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

class GameListViewModel: ViewModel() {
    fun flowMapGamesWithDate(): Flow<List<GameListViewItem>> {
        class Sender(
            val date: GameDate, // Для передачи даты от предыдущего значения к следующему
            val gamesInDay: List<GameListViewItem>, // Для отделения группы игр с одинаковой датой (и упорядочивания их внутри группы)
            val acc: List<GameListViewItem> // Аккумулятор для всех игр
        )

        return GameRepository.get().getGames().map { list ->
            // объединяем игры по дате проведения (без учета времени)
            list.fold(
                Sender(GameDate(LocalDate.MIN), listOf(), listOf())

            ) { sender, gwa ->
                if (sender.date != gwa.game.dateTime.date) {
                    // если дата не совпадает с предыдущей датой -
                    // то обновляем дату на новую, добавляем к аккумулятору полученную группу игр
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

    val weatherFlow: MutableStateFlow<Resource<WeatherResponse>> =
        MutableStateFlow(Resource.loading(null))

    init {
        GameRepository.get().getWeatherBy3h().enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                Log.d("12345", "onResponse: ${response.body()}")
                response.body()?.let { weatherFlow.value = Resource.success(it) }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {}
        })
        viewModelScope.launch {
            weatherFlow.collect() {
                Log.d("123456", "t: ${it.status} ${it.data}")
            }
        }
    }
}
