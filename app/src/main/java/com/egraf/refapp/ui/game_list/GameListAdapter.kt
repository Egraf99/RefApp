package com.egraf.refapp.ui.game_list

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.egraf.refapp.R
import com.egraf.refapp.database.entities.GameDate
import com.egraf.refapp.database.entities.GameWithAttributes
import com.egraf.refapp.databinding.DateListItemBinding
import com.egraf.refapp.databinding.GameListItemBinding
import java.time.LocalDate


interface ClickGameItemListener {
    fun onClick(gwa: GameWithAttributes)
}

class GameAdapter(private val context: Context, private val listener: ClickGameItemListener) :
    RecyclerView.Adapter<GameListHolder>() {
    private var gamesList = emptyList<GameListViewItem>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameListHolder {
        return when (viewType) {
            R.layout.date_list_item -> GameListHolder.DateViewHolder(
                context,
                DateListItemBinding.inflate(LayoutInflater.from(context), parent, false)
            )
            R.layout.game_list_item -> GameListHolder.GameViewHolder(
                listener,
                GameListItemBinding.inflate(LayoutInflater.from(context), parent, false)
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
        private val listener: ClickGameItemListener,
        private val gameBinding: GameListItemBinding
    ) : GameListHolder(gameBinding) {
        fun bind(gameItem: GameListViewItem.Game) {
            itemView.setOnClickListener { listener.onClick(gameItem.gwa) }
            gameBinding.stadiumTextview.text = gameItem.gwa.stadium?.name
            gameBinding.timeTextview.text = gameItem.gwa.game.dateTime.time.title

            if (gameItem.gwa.game.isPassed)
            //TODO: мутнее для проведенных игр
                gameBinding.weatherIcon.setImageResource(R.drawable.ic_sun)
            else
                gameBinding.weatherIcon.setImageResource(R.drawable.ic_flag)
        }
    }
}


