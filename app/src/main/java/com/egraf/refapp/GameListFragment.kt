package com.egraf.refapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GameListFragment : Fragment() {
    private lateinit var gameRecyclerView: RecyclerView
    private var adapter: GameAdapter? = null
    private val gameViewModel: GameListViewModel by lazy {
        ViewModelProvider(this).get(GameListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_game_list, container, false)
        gameRecyclerView = view.findViewById(R.id.game_recycle_view) as RecyclerView
        gameRecyclerView.layoutManager = LinearLayoutManager(context)

        updateUI()

        return view
    }

    private fun updateUI() {
        val games = gameViewModel.games
        adapter = GameAdapter(games)
        gameRecyclerView.adapter = adapter
    }

    private inner class GameHolder(view: View) : RecyclerView.ViewHolder(view) {
        val homeTeamTextVIew: TextView = itemView.findViewById(R.id.team_home_textview)
        val guestTeamTextView: TextView = itemView.findViewById(R.id.team_guest_textview)
        val stadiumTextView: TextView = itemView.findViewById(R.id.stadium_textview)
        val leagueTextView: TextView = itemView.findViewById(R.id.league_textview)
        val dateButton: Button = itemView.findViewById(R.id.date)
        val imgIsPaid: ImageView = itemView.findViewById(R.id.img_check_isPaid)

    }

    private inner class GameAdapter(var games: List<Game>) : RecyclerView.Adapter<GameHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameHolder {
            val view = layoutInflater.inflate(R.layout.list_item_game, parent, false)
            return GameHolder(view)
        }

        override fun onBindViewHolder(holder: GameHolder, position: Int) {
            val game = games[position]
            holder.apply {
                homeTeamTextVIew.text = game.homeTeam
                guestTeamTextView.text = game.guestTeam
                stadiumTextView.text = game.stadium
                leagueTextView.text = game.league
                dateButton.apply {
                    text = game.date.toString()
                    isEnabled = false
                }
                imgIsPaid.visibility = if (game.isPaid) View.VISIBLE else View.GONE

            }
        }

        override fun getItemCount() = games.size
    }

    companion object {
        fun newInstance(): GameListFragment {
            return GameListFragment()
        }
    }
}