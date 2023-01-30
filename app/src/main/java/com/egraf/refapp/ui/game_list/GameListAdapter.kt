package com.egraf.refapp.ui.game_list

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.egraf.refapp.R
import com.egraf.refapp.database.local.entities.GameDate
import com.egraf.refapp.database.local.entities.GameWithAttributes
import com.egraf.refapp.database.remote.model.Weather
import com.egraf.refapp.database.remote.model.WeatherType
import com.egraf.refapp.databinding.DateListItemBinding
import com.egraf.refapp.databinding.GameListItemBinding
import com.egraf.refapp.utils.Resource
import com.egraf.refapp.utils.Status
import com.egraf.refapp.utils.dp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import kotlin.math.roundToInt


interface ClickGameItemListener {
    fun onClick(gwa: GameWithAttributes)
}

class GameAdapter(
    private val fragment: Fragment,
    private val listener: ClickGameItemListener,
    private val getWeather: (Long) -> Flow<Resource<Weather>>
) :
    RecyclerView.Adapter<GameListHolder>() {
    private var gamesList = emptyList<GameListViewItem>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameListHolder {
        return when (viewType) {
            R.layout.date_list_item -> GameListHolder.DateViewHolder(
                fragment.requireContext(),
                DateListItemBinding.inflate(
                    LayoutInflater.from(fragment.requireContext()),
                    parent,
                    false
                )
            )
            R.layout.game_list_item -> GameListHolder.GameViewHolder(
                fragment,
                listener,
                getWeather,
                GameListItemBinding.inflate(
                    LayoutInflater.from(fragment.requireContext()),
                    parent,
                    false
                )
            )
            else -> throw IllegalArgumentException("Invalid ViewType Provided")
        }
    }

    override fun onBindViewHolder(holder: GameListHolder, position: Int) {
        when (holder) {
            is GameListHolder.DateViewHolder -> holder.bind(gamesList[position] as GameListViewItem.Date)
            is GameListHolder.GameViewHolder -> holder.bind(gamesList[position] as GameListViewItem.Game)
        }
    }

    override fun getItemCount() = gamesList.size

    override fun getItemViewType(position: Int): Int {
        return when (gamesList[position]) {
            is GameListViewItem.Date -> R.layout.date_list_item
            is GameListViewItem.Game -> R.layout.game_list_item
        }
    }

    fun setGames(gamesList: List<GameListViewItem>) {
        this.gamesList = gamesList
    }

}

sealed class GameListViewItem {
    data class Date(val date: GameDate) : GameListViewItem()
    data class Game(val gwa: GameWithAttributes) : GameListViewItem()
}

sealed class GameListHolder(binding: ViewBinding) :
    RecyclerView.ViewHolder(binding.root) {
    class DateViewHolder(
        private val context: Context,
        private val dateBinding: DateListItemBinding,
    ) : GameListHolder(dateBinding) {
        fun bind(dateItem: GameListViewItem.Date) {
            val now = LocalDate.now()
            val gameDate = dateItem.date.value
            dateBinding.dateTextview.text =
                if (gameDate == now)
                    context.getString(R.string.today)
                else if (gameDate.minusDays(1) == now)
                    context.getString(R.string.tomorrow)
                else if (gameDate.plusDays(1) == now)
                    context.getString(R.string.yesterday)
                else
                    dateItem.date.toString()
        }
    }

    class GameViewHolder(
        private val fragment: Fragment,
        private val listener: ClickGameItemListener,
        private val getWeather: (Long) -> Flow<Resource<Weather>>,
        private val gameBinding: GameListItemBinding
    ) : GameListHolder(gameBinding) {
        fun bind(gameItem: GameListViewItem.Game) {
            itemView.setOnClickListener { listener.onClick(gameItem.gwa) }
            gameBinding.stadiumTextview.text = gameItem.gwa.stadium?.name
            gameBinding.timeTextview.text = gameItem.gwa.game.dateTime.time.title

            if (gameItem.gwa.game.isPassed)
                gameBinding.dim(fragment.requireContext())
            else {
                gameBinding.bright(fragment.requireContext())

                // запрашиваем погоду
                fragment.viewLifecycleOwner.lifecycleScope.launch {
                    fragment.viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                        val gameDate = gameItem.gwa.game.dateTime.toMillis(zone = ZoneId.of("UTC"))
                            ?: Long.MAX_VALUE
                        if (gameDate < System.currentTimeMillis()) return@launchWhenStarted // не узнаем погоду для игр в прошлом
                        getWeather(gameDate).collect() {
                            when (it.status) {
                                Status.LOADING -> {}
                                Status.SUCCESS -> {
                                    it.data?.let { weather -> gameBinding.setWeather(weather) }
                                }
                                Status.ERROR -> {}
                            }
                        }
                    }

                }
            }
        }

        private fun GameListItemBinding.dim(context: Context) {
            ViewCompat.setElevation(this.layout, dp(context, 0).toFloat())
            this.layout.background =
                AppCompatResources.getDrawable(context, R.drawable.background_dim_game_item)
            this.timeFrame.background =
                AppCompatResources.getDrawable(context, R.drawable.background_time_dim_item)
            this.timeTextview.setTextColor(context.getColor(R.color.b))
            this.stadiumIcon.setImageResource(R.drawable.ic_stadium_dim)
            this.stadiumTextview.setTextColor(context.getColor(R.color.b))
            this.weatherIcon.visibility = View.INVISIBLE
        }

        private fun GameListItemBinding.bright(context: Context) {
            ViewCompat.setElevation(this.layout, dp(context, 8).toFloat())
            this.layout.background =
                AppCompatResources.getDrawable(context, R.drawable.background_bright_game_item)
            this.timeFrame.background =
                AppCompatResources.getDrawable(context, R.drawable.background_time_bright_item)
            this.stadiumIcon.setImageResource(R.drawable.ic_stadium)
            this.timeTextview.setTextColor(context.getColor(R.color.black))
            this.stadiumTextview.setTextColor(context.getColor(R.color.black))
        }

        private fun GameListItemBinding.setWeather(weather: Weather) {
            this.weatherText.apply {
                text = weather.temp.roundToInt().toString()
                visibility = View.VISIBLE
            }
            this.weatherIcon.apply {
                if (weather.type == WeatherType.UNKNOWN) return@apply
                setImageResource(weather.type.icon)
                visibility = View.VISIBLE

            }
        }
    }
}


