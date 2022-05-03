package com.egraf.refapp

import android.content.Context
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
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
import com.egraf.refapp.database.entities.Game
import com.egraf.refapp.database.entities.GameWithAttributes
import java.util.*

private const val TAG = "GameListFragment"
private const val DATE_FORMAT = "dd.MM.yyyy (EEE) HH:mm"

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
    private lateinit var emptyListTextView: TextView
    private lateinit var newGameButton: Button


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
        Log.d(TAG, "++++++++++ GameListFragment onCreate ++++++++++")
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

        emptyListTextView = view.findViewById(R.id.empty_list_textview)
        newGameButton = view.findViewById(R.id.new_game_button)

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

        gameListViewModel.countGamesLiveData.observe(
            viewLifecycleOwner
        ) { count ->
            showEmptyListRepresent(count)
        }
    }

    private fun updateUI(games: List<GameWithAttributes>) {
        Log.d(TAG, "+++++++++++ GameListFragment updateUI ++++++++++ $games")
        adapter?.submitList(games)
    }

    private fun showEmptyListRepresent(count: Int) {
        if (count > 0) {
            emptyListTextView.visibility = View.GONE
            newGameButton.visibility = View.GONE
        } else {
            emptyListTextView.visibility = View.VISIBLE
            newGameButton.visibility = View.VISIBLE
            newGameButton.setOnClickListener { addNewGame() }
        }
    }

    private fun addNewGame() {
        val game = Game()
        Log.d(TAG, "+++++++++++ GameFragment addGame +++++++++++ $game")
        gameListViewModel.addGame(game)
        callbacks?.onGameSelected(game.id)
    }

    private inner class GameHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {
        private lateinit var gameWithAttributes: GameWithAttributes

        val homeTeamTextVIew: TextView = itemView.findViewById(R.id.team_home_textview)
        val guestTeamTextView: TextView = itemView.findViewById(R.id.team_guest_textview)
        val stadiumTextView: TextView = itemView.findViewById(R.id.stadium_textview)
        val leagueTextView: TextView = itemView.findViewById(R.id.league_textview)
        val dateTextView: TextView = itemView.findViewById(R.id.date_textview)
        val imgGamePaid: ImageView = itemView.findViewById(R.id.img_check_isPaid)
        val imgGameDone: ImageView = itemView.findViewById(R.id.img_check_gamePass)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(game: GameWithAttributes) {
            gameWithAttributes = game
            Log.d(TAG, "+++++++++++ GameHolder bind ++++++++++++ $game")
            homeTeamTextVIew.text = gameWithAttributes.homeTeam?.name
            guestTeamTextView.text = gameWithAttributes.guestTeam?.name
            stadiumTextView.text = gameWithAttributes.stadium?.name
            leagueTextView.text = gameWithAttributes.league?.name
            dateTextView.text =
                DateFormat.format(DATE_FORMAT, gameWithAttributes.game.date).toString()

            val resGamePaid =
                if (gameWithAttributes.game.isPaid) R.drawable.ic_paiment_done else R.drawable.ic_paiment_wait
            imgGamePaid.setBackgroundResource(resGamePaid)

            val resGameGone =
                if (gameWithAttributes.game.isPassed) R.drawable.ic_calendar_green else R.drawable.ic_calendar_yelow
            imgGameDone.setBackgroundResource(resGameGone)
        }

        override fun onClick(v: View?) {
            callbacks?.onGameSelected(gameWithAttributes.game.id)
        }

    }

    private inner class GameAdapter :
        ListAdapter<GameWithAttributes, GameHolder>(GameDiffUtilCallback) {
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

    object GameDiffUtilCallback : DiffUtil.ItemCallback<GameWithAttributes>() {

        override fun areItemsTheSame(oldGame: GameWithAttributes, newGame: GameWithAttributes): Boolean {
            return oldGame.game.id == newGame.game.id
        }

        override fun areContentsTheSame(oldGame: GameWithAttributes, newGame: GameWithAttributes): Boolean {
            return oldGame.homeTeam?.name == newGame.homeTeam?.name &&
                    oldGame.guestTeam?.name == newGame.guestTeam?.name &&
                    oldGame.stadium?.name == newGame.stadium?.name &&
                    oldGame.league?.name == newGame.league?.name &&
                    oldGame.game.date == newGame.game.date &&
                    oldGame.game.isPaid == newGame.game.isPaid
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_game_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_game -> {
                addNewGame()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
