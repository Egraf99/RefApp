package com.egraf.refapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GameListFragment : Fragment() {
    private lateinit var gameRecyclerView: RecyclerView
    private var adapter: GameAdapter? = GameAdapter(emptyList())
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

        gameRecyclerView.adapter = adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gameViewModel.gamesListLiveData.observe(
            viewLifecycleOwner
        ) { games ->
            games?.let {
                updateUI(games)
            }
        }
    }

    private fun updateUI(games: List<Game>) {
        adapter = GameAdapter(games)
        gameRecyclerView.adapter = adapter
    }

    private inner class GameHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {
        private lateinit var game: Game

        val homeTeamTextVIew: TextView = itemView.findViewById(R.id.team_home_textview)
        val guestTeamTextView: TextView = itemView.findViewById(R.id.team_guest_textview)
        val stadiumTextView: TextView = itemView.findViewById(R.id.stadium_textview)
        val leagueTextView: TextView = itemView.findViewById(R.id.league_textview)
        val dateButton: Button = itemView.findViewById(R.id.date)
        val imgIsPaid: ImageView = itemView.findViewById(R.id.img_check_isPaid)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(game: Game) {
            this.game = game
            homeTeamTextVIew.text = this.game.homeTeam
            guestTeamTextView.text = this.game.guestTeam
            stadiumTextView.text = this.game.stadium
            leagueTextView.text = this.game.league
            dateButton.text = this.game.date.toString()
            dateButton.isEnabled = false
            imgIsPaid.visibility = if (this.game.isPaid) View.VISIBLE else View.GONE
        }

        override fun onClick(p0: View?) {
            Toast.makeText(
                context,
                "${game.homeTeam} vs. ${game.guestTeam} clicked!",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    private inner class GameAdapter(var games: List<Game>) : RecyclerView.Adapter<GameHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameHolder {
            val view = layoutInflater.inflate(R.layout.list_item_game, parent, false)
            return GameHolder(view)
        }

        override fun onBindViewHolder(holder: GameHolder, position: Int) {
            val game = games[position]
            holder.bind(game)
        }

        override fun getItemCount() = games.size
    }

    companion object {
        fun newInstance(): GameListFragment {
            return GameListFragment()
        }
    }
}