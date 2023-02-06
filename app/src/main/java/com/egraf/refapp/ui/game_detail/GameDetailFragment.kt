package com.egraf.refapp.ui.game_detail

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.egraf.refapp.R
import com.egraf.refapp.database.local.entities.GameDate
import com.egraf.refapp.database.local.entities.GameDateTime
import com.egraf.refapp.database.local.entities.GameTime
import com.egraf.refapp.database.local.entities.GameWithAttributes
import com.egraf.refapp.databinding.FragmentGameBinding
import com.egraf.refapp.ui.FragmentWithToolbar
import com.egraf.refapp.ui.dialogs.DeleteDialog
import com.egraf.refapp.utils.Status
import com.egraf.refapp.views.custom_views.GameComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*

private const val TAG = "GameFragment"

private const val ARG_GAME_ID = "game_id"
private const val REQUEST_DELETE = "DialogDelete"

@ExperimentalCoroutinesApi
class GameDetailFragment : FragmentWithToolbar(), FragmentResultListener {

    private val binding get() = _binding!!
    private var _binding: FragmentGameBinding? = null
    private val gameDetailViewModel: GameDetailViewModel by lazy {
        ViewModelProvider(this)[GameDetailViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gameId = arguments?.getSerializable(ARG_GAME_ID) as UUID
        gameDetailViewModel.loadGame(gameId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // настройка тулбара
        setDisplayHomeAsUpEnabled(true)
        setActionBarTitle(requireContext().getString(R.string.back))

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            gameDetailViewModel.flowGameWithAttributes.collect() { gameResource ->
                if (gameResource.status == Status.SUCCESS && gameResource.data() != null)
                    updateUI(gameResource.data()!!)
            }
        }

        parentFragmentManager.setFragmentResultListener(REQUEST_DELETE, viewLifecycleOwner, this)
    }

    override fun onStart() {
        super.onStart()
        binding.homeTeamView.bind(
            this.parentFragmentManager, viewLifecycleOwner, viewLifecycleOwner.lifecycleScope,
            onUpdateItem = { gameDetailViewModel.updateHomeTeam(it) }
        )
        binding.guestTeamView.bind(
            this.parentFragmentManager, viewLifecycleOwner, viewLifecycleOwner.lifecycleScope,
            onUpdateItem = { gameDetailViewModel.updateGuestTeam(it) }
        )
        binding.stadiumComponentView.bind(
            this.parentFragmentManager, viewLifecycleOwner, viewLifecycleOwner.lifecycleScope,
            onUpdateItem = { gameDetailViewModel.updateStadium(it) }
        )
        binding.leagueComponentView.bind(
            this.parentFragmentManager, viewLifecycleOwner, viewLifecycleOwner.lifecycleScope,
            onUpdateItem = { gameDetailViewModel.updateLeague(it) }
        )
        binding.chiefRefereeComponentView.bind(
            this.parentFragmentManager, viewLifecycleOwner, viewLifecycleOwner.lifecycleScope,
            onUpdateItem = { gameDetailViewModel.updateChiefReferee(it) }
        )
        binding.firstAssistantComponentView.bind(
            this.parentFragmentManager, viewLifecycleOwner, viewLifecycleOwner.lifecycleScope,
            onUpdateItem = { gameDetailViewModel.updateFirstAssistant(it) }
        )
        binding.secondAssistantComponentView.bind(
            this.parentFragmentManager, viewLifecycleOwner, viewLifecycleOwner.lifecycleScope,
            onUpdateItem = { gameDetailViewModel.updateSecondAssistant(it) }
        )
        binding.reserveRefereeComponentView.bind(
            this.parentFragmentManager, viewLifecycleOwner, viewLifecycleOwner.lifecycleScope,
            onUpdateItem = { gameDetailViewModel.updateReserveReferee(it) }
        )
        binding.inspectorComponentView.bind(
            this.parentFragmentManager, viewLifecycleOwner, viewLifecycleOwner.lifecycleScope,
            onUpdateItem = { gameDetailViewModel.updateInspector(it) }
        )
        binding.dateInput.bind(
            this.parentFragmentManager, viewLifecycleOwner, viewLifecycleOwner.lifecycleScope,
            onUpdateDate = {
                gameDetailViewModel.updateDateTime(
                    GameDateTime(it, binding.timeInput.item.getOrElse { GameTime() })
                )
            }
        )
        binding.timeInput.bind(
            this.parentFragmentManager, viewLifecycleOwner, viewLifecycleOwner.lifecycleScope,
            onUpdateTime = {
                gameDetailViewModel.updateDateTime(
                    GameDateTime(binding.dateInput.item.getOrElse { GameDate() }, it)
                )
            }
        )

        binding.gamePassedCheckBox.setOnCheckedChangeListener { _, isChecked ->
            gameDetailViewModel.updatePassedGame(isChecked)
        }
        binding.gamePaidCheckBox.setOnCheckedChangeListener { _, isChecked ->
            gameDetailViewModel.updatePaidGame(isChecked)
        }

        binding.deleteButton.setOnClickListener {
            DeleteDialog
                .newInstance(REQUEST_DELETE)
                .show(parentFragmentManager, REQUEST_DELETE)
        }
    }

    private fun updateUI(gameWithAttributes: GameWithAttributes) {
        binding.homeTeamView.item = GameComponent(gameWithAttributes.homeTeam)
        binding.guestTeamView.item = GameComponent(gameWithAttributes.guestTeam)
        binding.stadiumComponentView.item = GameComponent(gameWithAttributes.stadium)
        binding.leagueComponentView.item = GameComponent(gameWithAttributes.league)
        binding.chiefRefereeComponentView.item = GameComponent(gameWithAttributes.chiefReferee)
        binding.firstAssistantComponentView.item = GameComponent(gameWithAttributes.firstReferee)
        binding.secondAssistantComponentView.item = GameComponent(gameWithAttributes.secondReferee)
        binding.reserveRefereeComponentView.item = GameComponent(gameWithAttributes.reserveReferee)
        binding.inspectorComponentView.item = GameComponent(gameWithAttributes.inspector)
        binding.dateInput.item = GameComponent(gameWithAttributes.game.dateTime.date)
        binding.timeInput.item = GameComponent(gameWithAttributes.game.dateTime.time)

        binding.gamePaidCheckBox.apply {
            isChecked = gameWithAttributes.game.isPaid
            jumpDrawablesToCurrentState()
        }
        binding.gamePassedCheckBox.apply {
            isChecked = gameWithAttributes.game.isPassed
            jumpDrawablesToCurrentState()
        }
    }

    override fun onFragmentResult(requestKey: String, result: Bundle) {
        when (requestKey) {
            REQUEST_DELETE -> {
                when (DeleteDialog.getDeleteAnswer(result)) {
                    AlertDialog.BUTTON_NEGATIVE -> {
                        gameDetailViewModel.deleteGame()
                        findNavController().popBackStack()
                    }
                }
            }
        }
    }

    companion object {
        /**
         * Возвращает bundle с вложенным значением gameId
         */
        fun putGameId(gameId: UUID): Bundle {
            return Bundle().apply { putSerializable(ARG_GAME_ID, gameId) }
        }
    }
}