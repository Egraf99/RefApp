package com.egraf.refapp

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class GameListFragment : Fragment() {
    interface Callbacks {
        fun onGameSelected(gameId: UUID)
    }

    private var callbacks: Callbacks? = null
    private lateinit var gameRecyclerView: RecyclerView
    private var adapter: GameAdapter? = GameAdapter()
    private val gameListViewModel: GameListViewModel by lazy {
        ViewModelProvider(this).get(GameListViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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
        gameListViewModel.gamesListLiveData.observe(
            viewLifecycleOwner
        ) { games ->
            games?.let {
                updateUI(games)
            }
        }
    }

    private fun updateUI(games: List<Game>) {
        adapter?.submitList(games)
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

        override fun onClick(v: View?) {
            callbacks?.onGameSelected(game.id)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_game_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_game -> {
                val game = Game()
                gameListViewModel.addGame(game)
                callbacks?.onGameSelected(game.id)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private inner class GameAdapter :
        ListAdapter<Game, GameHolder>(GameDiffUtilCallback) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameHolder {
            val view = layoutInflater.inflate(R.layout.list_item_game, parent, false)
            return GameHolder(view)
        }

        override fun onBindViewHolder(holder: GameHolder, position: Int) {
            val game = currentList[position]
            holder.bind(game)
        }

        override fun getItemCount() = currentList.size

    }

    object GameDiffUtilCallback : DiffUtil.ItemCallback<Game>() {
        override fun areItemsTheSame(oldGame: Game, newGame: Game): Boolean {
            return oldGame.id == newGame.id
        }

        override fun areContentsTheSame(oldGame: Game, newGame: Game): Boolean {
            return oldGame.homeTeam == newGame.homeTeam &&
                    oldGame.guestTeam == newGame.guestTeam &&
                    oldGame.stadium == newGame.stadium &&
                    oldGame.league == newGame.league &&
                    oldGame.date == newGame.date &&
                    oldGame.isPaid == newGame.isPaid
        }
    }

    companion object {
        fun newInstance(): GameListFragment {
            return GameListFragment()
        }
    }
}