package com.egraf.refapp

import android.content.Context
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.egraf.refapp.database.entities.Game
import com.egraf.refapp.database.entities.GameWithAttributes
import com.egraf.refapp.databinding.FragmentGameListBinding
import com.egraf.refapp.databinding.ListItemGameBinding
import java.util.*

private const val TAG = "GameListFragment"
private const val DATE_FORMAT = "dd.MM.yyyy (EE) HH:mm"

class GameListFragment : Fragment(), AddGameViewModel.Callbacks {
    interface Callbacks {
        fun onGameSelected(gameId: UUID)
    }

    private var callbacks: Callbacks? = null
    private var _binding: FragmentGameListBinding? = null
    private val binding get() = _binding!!
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
        Log.d(TAG, "onCreate() called")
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameListBinding.inflate(inflater, container, false)
        binding.gameRecycleView.layoutManager = LinearLayoutManager(context)
        binding.gameRecycleView.adapter = adapter
        binding.viewModel = AddGameViewModel(this)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
        Log.d(TAG, "updateUI() called with: games = $games")
        adapter?.submitList(games)
    }

    private fun showEmptyListRepresent(count: Int) {
        if (count > 0) {
            binding.emptyListTextview.visibility = View.GONE
            binding.newGameButton.visibility = View.GONE
        } else {
            binding.emptyListTextview.visibility = View.VISIBLE
            binding.newGameButton.visibility = View.VISIBLE
            binding.newGameButton.setOnClickListener { addNewGame() }
        }
    }

    private fun addNewGame() {
        val game = Game()
        Log.d(TAG, "addNewGame() called")
        gameListViewModel.addGame(game)
        callbacks?.onGameSelected(game.id)
    }

    override fun onGameSelected(gameId: UUID) {
        callbacks?.onGameSelected(gameId)
    }

    private inner class GameHolder(val binding: ListItemGameBinding) :
        RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {
        private lateinit var gameWithAttributes: GameWithAttributes

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(game: GameWithAttributes) {
            gameWithAttributes = game
            Log.d(TAG, "bind() called with: game = $game")
            binding.stadiumTextview.text = gameWithAttributes.stadium?.name
            binding.leagueTextview.text = gameWithAttributes.league?.name
            binding.dateTextview.text =
                DateFormat.format(DATE_FORMAT, gameWithAttributes.game.date).toString()

            val resGamePaid =
                if (gameWithAttributes.game.isPaid) R.drawable.ic_payment_done else R.drawable.ic_payment_wait
            binding.imgCheckGamePaid.setBackgroundResource(resGamePaid)

            val resGamePassed =
                if (gameWithAttributes.game.isPassed) R.drawable.ic_calendar_green else R.drawable.ic_calendar_yelow
            binding.imgCheckGamePass.setBackgroundResource(resGamePassed)
        }

        override fun onClick(v: View?) {
            callbacks?.onGameSelected(gameWithAttributes.game.id)
        }

    }

    private inner class GameAdapter :
        ListAdapter<GameWithAttributes, GameHolder>(GameDiffUtilCallback) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameHolder {
            return GameHolder(
                ListItemGameBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: GameHolder, position: Int) {
            val game = currentList[position]
            holder.bind(game)
        }

        override fun getItemCount() = currentList.size

    }

    object GameDiffUtilCallback : DiffUtil.ItemCallback<GameWithAttributes>() {

        override fun areItemsTheSame(
            oldGame: GameWithAttributes,
            newGame: GameWithAttributes
        ): Boolean {
            return oldGame.game.id == newGame.game.id
        }

        override fun areContentsTheSame(
            oldGame: GameWithAttributes,
            newGame: GameWithAttributes
        ): Boolean {
            return oldGame.stadium?.name == newGame.stadium?.name &&
                    oldGame.league?.name == newGame.league?.name &&
                    oldGame.game.date == newGame.game.date &&
                    oldGame.game.isPaid == newGame.game.isPaid &&
                    oldGame.game.isPassed == newGame.game.isPassed
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
