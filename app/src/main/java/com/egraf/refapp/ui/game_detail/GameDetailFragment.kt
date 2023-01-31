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
import com.egraf.refapp.GameRepository
import com.egraf.refapp.R
import com.egraf.refapp.database.local.entities.*
import com.egraf.refapp.databinding.FragmentGameBinding
import com.egraf.refapp.ui.dialogs.DatePickerFragment
import com.egraf.refapp.ui.dialogs.DeleteDialog
import com.egraf.refapp.ui.dialogs.TimePickerFragment
import com.egraf.refapp.ui.FragmentWithToolbar
import com.egraf.refapp.views.custom_views.GameComponent
import com.egraf.refapp.views.textInput.RefereeETI
import com.egraf.refapp.views.textInput.TeamETI
import kotlinx.coroutines.launch
import java.util.*

private const val TAG = "GameFragment"

private const val ARG_GAME_ID = "game_id"
private const val REQUEST_DATE = "DialogDate"
private const val REQUEST_TIME = "DialogTime"
private const val REQUEST_DELETE = "DialogDelete"
private const val DATE_FORMAT = "EEE dd.MM.yyyy"
private const val TIME_FORMAT = "HH:mm"

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

        // при совпадении игры по id инициилизируем GameWithAttribute
        gameDetailViewModel.gameLiveData.observe(viewLifecycleOwner) { game ->
            game?.let {
                gameDetailViewModel.setGameWithAttributes(game)
                updateUI(it)
            }
        }

        parentFragmentManager.setFragmentResultListener(REQUEST_DATE, viewLifecycleOwner, this)
        parentFragmentManager.setFragmentResultListener(REQUEST_TIME, viewLifecycleOwner, this)
        parentFragmentManager.setFragmentResultListener(REQUEST_DELETE, viewLifecycleOwner, this)
    }

    override fun onStart() {
        super.onStart()
        binding.homeTeamView.bind(
            this.parentFragmentManager, viewLifecycleOwner, viewLifecycleOwner.lifecycleScope
        )
        binding.guestTeamView.bind(
            this.parentFragmentManager, viewLifecycleOwner, viewLifecycleOwner.lifecycleScope
        )
        binding.stadiumComponentView.bind(
            this.parentFragmentManager, viewLifecycleOwner, viewLifecycleOwner.lifecycleScope
        )
        binding.leagueComponentView.bind(
            this.parentFragmentManager, viewLifecycleOwner, viewLifecycleOwner.lifecycleScope
        )
        binding.chiefRefereeComponentView.bind(
            this.parentFragmentManager, viewLifecycleOwner, viewLifecycleOwner.lifecycleScope
        )
        binding.firstAssistantComponentView.bind(
            this.parentFragmentManager, viewLifecycleOwner, viewLifecycleOwner.lifecycleScope
        )
        binding.secondAssistantComponentView.bind(
            this.parentFragmentManager, viewLifecycleOwner, viewLifecycleOwner.lifecycleScope
        )
        binding.reserveRefereeComponentView.bind(
            this.parentFragmentManager, viewLifecycleOwner, viewLifecycleOwner.lifecycleScope
        )
        binding.inspectorComponentView.bind(
            this.parentFragmentManager, viewLifecycleOwner, viewLifecycleOwner.lifecycleScope
        )
        binding.dateInput.bind(
            this.parentFragmentManager, viewLifecycleOwner, viewLifecycleOwner.lifecycleScope
        )
        binding.timeInput.bind(
            this.parentFragmentManager, viewLifecycleOwner, viewLifecycleOwner.lifecycleScope
        )
        binding.gamePaidCheckBox.setOnCheckedChangeListener { _, isPaid ->
            gameDetailViewModel.gameWithAttributes.game.isPaid = isPaid
        }
        binding.gamePassedCheckBox.setOnCheckedChangeListener { _, isPassed ->
            gameDetailViewModel.gameWithAttributes.game.isPassed = isPassed
        }

        binding.deleteButton.setOnClickListener {
            DeleteDialog
                .newInstance(REQUEST_DELETE)
                .show(parentFragmentManager, REQUEST_DELETE)
        }
    }

    override fun onStop() {
        super.onStop()
        gameDetailViewModel.updateGame()
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
            isChecked = gameDetailViewModel.gameWithAttributes.game.isPaid
            jumpDrawablesToCurrentState()
        }
        binding.gamePassedCheckBox.apply {
            isChecked = gameDetailViewModel.gameWithAttributes.game.isPassed
            jumpDrawablesToCurrentState()
        }
    }

    /**
     * Устанавливает дату на кнопке выбора даты
     */
    private fun updateDate() {
//        binding.gameDateButton.text =
//            DateFormat.format(DATE_FORMAT, gameDetailViewModel.gameWithAttributes.game.dateTime)
//                .toString()
    }

    /**
     * Устанавливает время на кнопке выбора времени
     */
    private fun updateTime() {
//        binding.gameTimeButton.text =
//            DateFormat.format(TIME_FORMAT, gameDetailViewModel.gameWithAttributes.game.dateTime)
//                .toString()
    }

    override fun onFragmentResult(requestKey: String, result: Bundle) {
        when (requestKey) {
            REQUEST_DATE -> {
//                gameDetailViewModel.gameWithAttributes.game.dateTime =
//                    DatePickerFragment.getSelectedDate(result)
//                updateDate()
            }
            REQUEST_TIME -> {
//                gameDetailViewModel.gameWithAttributes.game.dateTime =
//                    TimePickerFragment.getSelectedTime(result)
//                updateTime()
            }
            REQUEST_DELETE -> {
                when (DeleteDialog.getDeleteAnswer(result)) {
                    AlertDialog.BUTTON_NEGATIVE -> {
                        gameDetailViewModel.deleteGame(gameDetailViewModel.gameWithAttributes.game)
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