package com.egraf.refapp.ui.game_detail

import android.app.AlertDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.egraf.refapp.R
import com.egraf.refapp.database.entities.League
import com.egraf.refapp.database.entities.Referee
import com.egraf.refapp.database.entities.Stadium
import com.egraf.refapp.database.entities.Team
import com.egraf.refapp.databinding.FragmentGameBinding
import com.egraf.refapp.ui.dialogs.DatePickerFragment
import com.egraf.refapp.ui.dialogs.DeleteDialog
import com.egraf.refapp.ui.dialogs.TimePickerFragment
import com.egraf.refapp.ui.FragmentWithToolbar
import com.egraf.refapp.views.textInput.RefereeETI
import com.egraf.refapp.views.textInput.TeamETI
import dagger.hilt.android.AndroidEntryPoint
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
                updateUI()
            }
        }

        parentFragmentManager.setFragmentResultListener(REQUEST_DATE, viewLifecycleOwner, this)
        parentFragmentManager.setFragmentResultListener(REQUEST_TIME, viewLifecycleOwner, this)
        parentFragmentManager.setFragmentResultListener(REQUEST_DELETE, viewLifecycleOwner, this)
    }

    override fun onStart() {
        super.onStart()
        binding.teamHomeLayout
            .init(this, gameDetailViewModel, TeamETI.TypeTeam.HOME_TEAM)
            .whatDoWhenTextMatchedEntity { team ->
                gameDetailViewModel.setHomeTeam(team as Team?)
            }
            .whatDoWhenTextIsBlank {
                gameDetailViewModel.setHomeTeam(null)
            }

        binding.teamGuestLayout
            .init(this, gameDetailViewModel, TeamETI.TypeTeam.GUEST_TEAM)
            .whatDoWhenTextMatchedEntity { team ->
                gameDetailViewModel.setGuestTeam(team as Team?)
            }
            .whatDoWhenTextIsBlank {
                gameDetailViewModel.setGuestTeam(null)
            }

        binding.stadiumLayout
            .init(this, gameDetailViewModel)
            .whatDoWhenTextMatchedEntity { stadium ->
                gameDetailViewModel.setStadium(stadium as Stadium?)
            }
            .whatDoWhenTextIsBlank {
                gameDetailViewModel.setStadium(null)
            }

        binding.leagueLayout
            .init(this, gameDetailViewModel)
            .whatDoWhenTextMatchedEntity { league ->
                gameDetailViewModel.setLeague(league as League?)
            }
            .whatDoWhenTextIsBlank {
                gameDetailViewModel.setLeague(null)
            }

        binding.chiefRefereeLayout
            .init(
                this,
                gameDetailViewModel,
                RefereeETI.TypeReferee.CHIEF_REFEREE
            ).whatDoWhenTextMatchedEntity { referee ->
                gameDetailViewModel.setChiefReferee(referee as Referee?)
            }
            .whatDoWhenTextIsBlank {
                gameDetailViewModel.setChiefReferee(null)
            }
        binding.firstRefereeLayout
            .init(
                this,
                gameDetailViewModel,
                RefereeETI.TypeReferee.FIRST_REFEREE
            ).whatDoWhenTextMatchedEntity { referee ->
                gameDetailViewModel.setFirstReferee(referee as Referee?)
            }
            .whatDoWhenTextIsBlank {
                gameDetailViewModel.setFirstReferee(null)
            }
        binding.secondRefereeLayout.init(
            this,
            gameDetailViewModel,
            RefereeETI.TypeReferee.SECOND_REFEREE
        ).whatDoWhenTextMatchedEntity { referee ->
            gameDetailViewModel.setSecondReferee(referee as Referee?)
        }
            .whatDoWhenTextIsBlank {
                gameDetailViewModel.setSecondReferee(null)
            }
        binding.reserveRefereeLayout.init(
            this,
            gameDetailViewModel,
            RefereeETI.TypeReferee.RESERVE_REFEREE
        ).whatDoWhenTextMatchedEntity { referee ->
            gameDetailViewModel.setReserveReferee(referee as Referee?)
        }
            .whatDoWhenTextIsBlank {
                gameDetailViewModel.setReserveReferee(null)
            }
        binding.inspectorLayout
            .init(this, gameDetailViewModel, RefereeETI.TypeReferee.INSPECTOR)
            .whatDoWhenTextMatchedEntity { referee ->
                gameDetailViewModel.setInspector(referee as Referee?)
            }
            .whatDoWhenTextIsBlank {
                gameDetailViewModel.setInspector(null)
            }

        binding.gamePaidCheckBox.setOnCheckedChangeListener { _, isPaid ->
            gameDetailViewModel.gameWithAttributes.game.isPaid = isPaid
        }
        binding.gamePassedCheckBox.setOnCheckedChangeListener { _, isPassed ->
            gameDetailViewModel.gameWithAttributes.game.isPassed = isPassed
        }

        binding.gameDateButton.setOnClickListener {
            DatePickerFragment
                .newInstance(gameDetailViewModel.gameWithAttributes.game.date.value, REQUEST_DATE)
                .show(parentFragmentManager, REQUEST_DATE)
        }

        binding.gameTimeButton.setOnClickListener {
            TimePickerFragment
                .newInstance(gameDetailViewModel.gameWithAttributes.game.date.value, REQUEST_TIME)
                .show(parentFragmentManager, REQUEST_TIME)
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

    private fun updateUI() {
        val textInputs = listOf(
            binding.teamHomeLayout,
            binding.teamGuestLayout,
            binding.stadiumLayout,
            binding.leagueLayout,
            binding.chiefRefereeLayout,
            binding.firstRefereeLayout,
            binding.secondRefereeLayout,
            binding.reserveRefereeLayout,
            binding.inspectorLayout
        )
        val attributesList = listOf(
            gameDetailViewModel.gameWithAttributes.homeTeam,
            gameDetailViewModel.gameWithAttributes.guestTeam,
            gameDetailViewModel.gameWithAttributes.stadium,
            gameDetailViewModel.gameWithAttributes.league,
            gameDetailViewModel.gameWithAttributes.chiefReferee,
            gameDetailViewModel.gameWithAttributes.firstReferee,
            gameDetailViewModel.gameWithAttributes.secondReferee,
            gameDetailViewModel.gameWithAttributes.reserveReferee,
            gameDetailViewModel.gameWithAttributes.inspector
        )

        for (pair in textInputs.zip(attributesList)) {
            val textInput = pair.first
            val attribute = pair.second
            if (textInput.getText().isBlank())
                textInput.setText(attribute?.shortName ?: "")
        }
        updateDate()
        updateTime()

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
        binding.gameDateButton.text =
            DateFormat.format(DATE_FORMAT, gameDetailViewModel.gameWithAttributes.game.date.value)
                .toString()
    }

    /**
     * Устанавливает время на кнопке выбора времени
     */
    private fun updateTime() {
        binding.gameTimeButton.text =
            DateFormat.format(TIME_FORMAT, gameDetailViewModel.gameWithAttributes.game.date.value)
                .toString()
    }

    override fun onFragmentResult(requestKey: String, result: Bundle) {
        when (requestKey) {
            REQUEST_DATE -> {
//                gameDetailViewModel.gameWithAttributes.game.date =
//                    DatePickerFragment.getSelectedDate(result)
//                updateDate()
            }
            REQUEST_TIME -> {
//                gameDetailViewModel.gameWithAttributes.game.date =
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