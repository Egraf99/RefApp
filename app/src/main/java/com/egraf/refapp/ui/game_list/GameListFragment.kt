package com.egraf.refapp.ui.game_list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.egraf.refapp.R
import com.egraf.refapp.database.entities.GameWithAttributes
import com.egraf.refapp.databinding.GameListFragmentBinding
import com.egraf.refapp.databinding.ListItemGameBinding
import com.egraf.refapp.ui.FragmentWithToolbar
import com.egraf.refapp.ui.game_detail.GameDetailFragment
import kotlinx.coroutines.launch
import java.time.LocalDateTime

private const val TAG = "GameListFragment"

class GameListFragment: FragmentWithToolbar() {
    private var _binding: GameListFragmentBinding? = null
    private val binding get() = _binding!!
    private var adapter: GameAdapter = GameAdapter()
    private val gameListViewModel: GameListViewModel by lazy {
        ViewModelProvider(this)[GameListViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = GameListFragmentBinding.inflate(inflater, container, false)
        binding.gameRecycleView.layoutManager = LinearLayoutManager(context)
        binding.gameRecycleView.adapter = adapter
        binding.addNewGameButton.setOnClickListener {
            findNavController().navigate(R.id.action_gameListFragment_to_addNewGame)
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                gameListViewModel.flowMapGamesWithDate.collect() { map ->
                    Log.d("12345", "onViewCreated: $map")
                    updateRecycleView(map.values.toList().flatten())
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        showLoading()
    }

    private fun showLoading() {
        binding.loading.visibility = View.VISIBLE
        binding.gameRecycleView.visibility = View.INVISIBLE
        binding.hintTextview.visibility = View.INVISIBLE
    }

    private fun showText(text: String = getString(R.string.empty_list)) {
        binding.loading.visibility = View.INVISIBLE
        binding.gameRecycleView.visibility = View.INVISIBLE

        binding.hintTextview.text = text
        binding.hintTextview.visibility = View.VISIBLE
    }

    private fun showRecycleView() {
        binding.loading.visibility = View.INVISIBLE
        binding.gameRecycleView.visibility = View.VISIBLE
        binding.hintTextview.visibility = View.INVISIBLE
    }

    private fun updateRecycleView(data: List<GameWithAttributes>) {
        if (data.isEmpty()) showText()
        else {
            adapter.submitList(data)
            showRecycleView()
        }
    }

    public inner class GameHolder(val binding: ListItemGameBinding) :
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
            binding.timeTextview.text = gameWithAttributes.game.dateTime.time.title

            binding.weatherIcon.setImageResource(R.drawable.ic_sun)
        }

        override fun onClick(v: View?) {
            val bundle = GameDetailFragment.putGameId(gameWithAttributes.game.id)
            findNavController().navigate(R.id.action_gameListFragment_to_gameFragment, bundle)
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
                    oldGame.game.dateTime == newGame.game.dateTime &&
                    oldGame.game.isPaid == newGame.game.isPaid &&
                    oldGame.game.isPassed == newGame.game.isPassed
        }

    }
}
