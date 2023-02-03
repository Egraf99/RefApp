package com.egraf.refapp.ui.game_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egraf.refapp.GameRepository
import com.egraf.refapp.database.local.entities.GameDate
import com.egraf.refapp.database.remote.model.Weather
import com.egraf.refapp.database.remote.model.open_weather_pojo.WeatherResponse
import com.egraf.refapp.utils.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import kotlin.math.abs

class GameListViewModel: ViewModel() {
    fun flowMapGamesWithDate(): Flow<List<GameListViewItem>> {
        class Sender(
            val date: GameDate, // Для передачи даты от предыдущего значения к следующему
            val gamesInDay: List<GameListViewItem>, // Для отделения группы игр с одинаковой датой (и упорядочивания их внутри группы)
            val acc: List<GameListViewItem>, // Аккумулятор для всех игр
        ) {
            fun appendRestGamesInDay(): Sender = if (gamesInDay.isEmpty()) this else Sender(
                date,
                listOf(),
                acc + gamesInDay
            )
        }

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
            }.appendRestGamesInDay().acc
        }
    }

    private val weatherFlow: MutableStateFlow<Map<Long, Weather>> =
        MutableStateFlow(mapOf())

    @ExperimentalCoroutinesApi
    val getWeather: (Long) -> StateFlow<Resource<Weather>> = { dt ->
        weatherFlow.mapLatest {
            // начальное значение устанавливаем с максимальным числом
            var resultWeather: Pair<Long, Resource<Weather>> =
                Pair(Long.MAX_VALUE, Resource.error("Don't find weather"))

            it.forEach { weatherWithDt ->
                // ищем наименьшую разницу между данной датой и доступными датами с прогнозом погоды
                val difference = abs(dt - weatherWithDt.key)
                val millisIn3h = 10_800_000

                // разница должна быть меньше 3х часов
                if (difference < millisIn3h && difference < resultWeather.first) {
                    resultWeather = Pair(difference, Resource.success(weatherWithDt.value))
                }
            }
            resultWeather.second
        }.stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5000),
            initialValue = Resource.loading()
        )
    }

    init {
        GameRepository.get().getWeatherBy3h().enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                response.body()?.let {
                    weatherFlow.value =
                        it.list.fold(mapOf()) { acc, w ->
                            acc + Pair(
                                // добавляем 000, так как в API возвращается дата без секунд и милисекунд
                                (w.dt.toString() + "000").toLong(),
                                Weather(temp = w.main.temp, w.weather[0].icon)
                            )
                        }
                }
            }
            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {}
        })
    }
}
