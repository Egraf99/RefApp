package com.egraf.refapp.ui.game_list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.egraf.refapp.R
import com.egraf.refapp.database.local.entities.*
import com.egraf.refapp.databinding.GameListFragmentBinding
import com.egraf.refapp.ui.FragmentWithToolbar
import com.egraf.refapp.ui.dialogs.add_new_game.ChooserFragment
import com.egraf.refapp.ui.dialogs.choose_component_dialog.ChooseComponentDialogFragment
import com.egraf.refapp.ui.game_detail.GameDetailFragment
import com.egraf.refapp.utils.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import java.util.*
import kotlin.reflect.KClass

private const val TAG = "GameListFragment"

private const val CHOOSE_COMPONENT_KEY = "ChooseComponentKey"

@ExperimentalCoroutinesApi
class GameListFragment : FragmentWithToolbar(), ClickGameItemListener, LongClickGameItemListener {
    private var _binding: GameListFragmentBinding? = null
    private val binding get() = _binding!!
    private var adapter = lazy { GameAdapter(this, this, this, gameListViewModel.getWeather) }
    private val gameListViewModel: GameListViewModel by lazy {
        ViewModelProvider(this)[GameListViewModel::class.java]
    }

    override fun onClick(gwa: GameWithAttributes) {
        findNavController().navigate(
            R.id.action_gameListFragment_to_gameFragment,
            GameDetailFragment.putGameId(gwa.game.id)
        )
    }

    override fun onLongCLick(gwa: GameWithAttributes) {
        val chooseComponentListener = ChooseComponentListener(gwa) {
            addNewGame(it.putCurrentTime())
        }
        parentFragmentManager.setFragmentResultListener(
            CHOOSE_COMPONENT_KEY,
            viewLifecycleOwner,
            chooseComponentListener
        )
        ChooseComponentDialogFragment(CHOOSE_COMPONENT_KEY).show(parentFragmentManager, "")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = GameListFragmentBinding.inflate(inflater, container, false)
        binding.gameRecycleView.layoutManager = LinearLayoutManager(context)
        binding.gameRecycleView.adapter = adapter.value
        binding.addNewGameButton.setOnClickListener { addNewGame() }
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
                gameListViewModel.flowMapGamesWithDate().collect {
                    updateRecycleView(it)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        showLoading()
    }

    private fun addNewGame(bundle: Bundle = Bundle()) {
        findNavController().navigate(R.id.action_gameListFragment_to_addNewGame, bundle)
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

class ChooseComponentListener(private val gwa: GameWithAttributes, private val effect: (Bundle) -> Unit): FragmentResultListener {
    override fun onFragmentResult(requestKey: String, result: Bundle) {
        if (requestKey == CHOOSE_COMPONENT_KEY) {
            val filteringBundle = Bundle()
            if (ChooseComponentDialogFragment.isCheckedDate(result)) {
                filteringBundle.putDate(gwa.game.dateTime.date)
                filteringBundle.putPaid(gwa.game.isPaid)
                filteringBundle.putPassed(gwa.game.isPassed)
            }
            if(ChooseComponentDialogFragment.isCheckedTime(result))
                filteringBundle.putTime(gwa.game.dateTime.time)
            if (ChooseComponentDialogFragment.isCheckedStadium(result)) {
                filteringBundle.putStadium(gwa.stadium)
            }
            if (ChooseComponentDialogFragment.isCheckedTeam(result)) {
                filteringBundle.putHomeTeam(gwa.homeTeam)
                filteringBundle.putGuestTeam(gwa.guestTeam)
                filteringBundle.putLeague(gwa.league)
            }
            if (ChooseComponentDialogFragment.isCheckedReferee(result)) {
                filteringBundle.putChiefReferee(gwa.chiefReferee)
                filteringBundle.putFirstAssistant(gwa.firstReferee)
                filteringBundle.putSecondAssistant(gwa.secondReferee)
                filteringBundle.putReserveReferee(gwa.reserveReferee)
                filteringBundle.putInspector(gwa.inspector)
            }
            effect(filteringBundle)
        }
    }
}