package com.egraf.refapp.ui.game_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.egraf.refapp.R
import com.egraf.refapp.database.entities.GameWithAttributes
import com.egraf.refapp.databinding.GameListFragmentBinding
import com.egraf.refapp.ui.FragmentWithToolbar
import com.egraf.refapp.ui.game_detail.GameDetailFragment
import kotlinx.coroutines.launch

private const val TAG = "GameListFragment"

class GameListFragment : FragmentWithToolbar(), ClickGameItemListener {
    private var _binding: GameListFragmentBinding? = null
    private val binding get() = _binding!!
    private var adapter = lazy { GameAdapter(requireContext(), this) }
    private val gameListViewModel: GameListViewModel by lazy {
        ViewModelProvider(this)[GameListViewModel::class.java]
    }

    override fun onClick(gwa: GameWithAttributes) {
        findNavController().navigate(
            R.id.action_gameListFragment_to_gameFragment,
            GameDetailFragment.putGameId(gwa.game.id)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = GameListFragmentBinding.inflate(inflater, container, false)
        binding.gameRecycleView.layoutManager = LinearLayoutManager(context)
        binding.gameRecycleView.adapter = adapter.value
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
                gameListViewModel.flowMapGamesWithDate.collect {
                    updateRecycleView(it)
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

    private fun updateRecycleView(data: List<GameListViewItem>) {
        if (data.isEmpty()) showText()
        else {
            adapter.value.setGames(data)
            binding.gameRecycleView.adapter = adapter.value
            showRecycleView()
        }
    }

}
