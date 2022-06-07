package com.egraf.refapp

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.egraf.refapp.GameDetailViewModel
import com.egraf.refapp.R
import com.egraf.refapp.database.entities.*
import com.egraf.refapp.databinding.FragmentGameBinding
import com.egraf.refapp.databinding.FragmentGameListBinding
import com.egraf.refapp.dialogs.DatePickerFragment
import com.egraf.refapp.dialogs.DeleteDialog
import com.egraf.refapp.dialogs.TimePickerFragment
import com.egraf.refapp.views.textInput.EntityTextInput
import java.util.*

private const val TAG = "GameFragment"

private const val ARG_GAME_ID = "game_id"
private const val REQUEST_DATE = "DialogDate"
private const val REQUEST_TIME = "DialogTime"
private const val REQUEST_DELETE = "DialogDelete"
private const val DATE_FORMAT = "EEE dd.MM.yyyy"
private const val TIME_FORMAT = "HH:mm"

class GameFragment : Fragment(), FragmentResultListener {

    private val binding get() = _binding!!
    private var _binding: FragmentGameBinding? = null
    private lateinit var gameWithAttributes: GameWithAttributes
    private val gameDetailViewModel: GameDetailViewModel by lazy {
        ViewModelProvider(this, GameDetailViewModelFactory()).get(GameDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val game = Game()
        gameWithAttributes = GameWithAttributes(game)
        Log.d(TAG, "onCreate: $gameWithAttributes")

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
        gameDetailViewModel.gameLiveData.observe(viewLifecycleOwner) { game ->
            game?.let {
                gameWithAttributes = game
                updateUI()
            }
        }
        gameDetailViewModel.stadiumListLiveData.observe(viewLifecycleOwner) { stadiums ->
            binding.stadiumLayout.setEntities(stadiums)
        }
        gameDetailViewModel.leagueListLiveData.observe(viewLifecycleOwner) { leagues ->
            binding.leagueLayout.setEntities(leagues)
        }
        gameDetailViewModel.teamListLiveData.observe(viewLifecycleOwner) { teams ->
            for (textInput in listOf(
                binding.teamHomeLayout,
                binding.teamGuestLayout
            )) {
                textInput.setEntities(teams)
            }

        }
        gameDetailViewModel.refereeListLiveData.observe(viewLifecycleOwner) { referee ->
            for (textInput in listOf(
                binding.chiefRefereeLayout,
                binding.firstRefereeLayout,
                binding.secondRefereeLayout,
                binding.reserveRefereeLayout,
                binding.inspectorLayout
            )) {
                textInput.setEntities(referee)
            }
        }

        for (request in listOf(REQUEST_DATE, REQUEST_TIME, REQUEST_DELETE))
            parentFragmentManager.setFragmentResultListener(request, viewLifecycleOwner, this)
    }

    override fun onStart() {
        super.onStart()
        binding.teamHomeLayout
            .whatDoWhenTextIsBlank { setHomeTeamNull() }
            .whatDoWhenTextMatchedEntity { team -> saveHomeTeam(team as Team) }
            .whatDoWhenAddClicked { text ->
                run {
                    // создаем новый стадион
                    val team = Team().setEntityName(text)
                    // сохраняем стадион
                    saveHomeTeam(team)
                    // показываем сообщение
                    Toast.makeText(
                        context,
                        getString(R.string.team_add_message, team.fullName),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .whatDoWhenInfoClicked { team ->
                // показываем сообщение с полным именем стадиона
                Toast.makeText(
                    context,
                    team.fullName,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

        binding.teamGuestLayout
            .whatDoWhenTextIsBlank { setGuestTeamNull() }
            .whatDoWhenTextMatchedEntity { team -> saveGuestTeam(team as Team) }
            .whatDoWhenAddClicked { text ->
                run {
                    // создаем новый стадион
                    val team = Team().setEntityName(text)
                    // сохраняем стадион
                    saveGuestTeam(team)
                    // показываем сообщение
                    Toast.makeText(
                        context,
                        getString(R.string.team_add_message, team.fullName),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .whatDoWhenInfoClicked { team ->
                // показываем сообщение с полным именем стадиона
                Toast.makeText(
                    context,
                    team.fullName,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

        binding.stadiumLayout
            .whatDoWhenTextIsBlank { setStadiumNull() }
            .whatDoWhenTextMatchedEntity { stadium -> saveStadium(stadium as Stadium) }
            .whatDoWhenAddClicked { text ->
                run {
                    // создаем новый стадион
                    val stadium = Stadium().setEntityName(text)
                    // сохраняем стадион
                    saveStadium(stadium)
                    // показываем сообщение
                    Toast.makeText(
                        context,
                        getString(R.string.stadium_add_message, stadium.fullName),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .whatDoWhenInfoClicked { stadium ->
                // показываем сообщение с полным именем стадиона
                Toast.makeText(
                    context,
                    stadium.fullName,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

        binding.leagueLayout
            .whatDoWhenTextIsBlank { setLeagueNull() }
            .whatDoWhenTextMatchedEntity { league -> saveLeague(league as League) }
            .whatDoWhenAddClicked { text ->
                run {
                    // создаем новый лигу
                    val league = League().setEntityName(text)
                    // сохраняем стадион
                    saveLeague(league)
                    // показываем сообщение
                    Toast.makeText(
                        context,
                        getString(R.string.league_add_message, league.fullName),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .whatDoWhenInfoClicked { league ->
                // показываем сообщение с полным именем стадиона
                Toast.makeText(
                    context,
                    league.fullName,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

        binding.chiefRefereeLayout
            .whatDoWhenTextIsBlank { setChiefRefereeNull() }
            .whatDoWhenTextMatchedEntity { referee -> saveChiefReferee(referee as Referee) }
            .whatDoWhenAddClicked { text ->
                run {
                    // создаем новый стадион
                    val referee = Referee().setEntityName(text)
                    // сохраняем стадион
                    saveChiefReferee(referee)
                    // показываем сообщение
                    Toast.makeText(
                        context,
                        getString(R.string.referee_add_message, referee.fullName),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .whatDoWhenInfoClicked { referee ->
                // показываем сообщение с полным именем стадиона
                Toast.makeText(
                    context,
                    referee.fullName,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

        binding.firstRefereeLayout
            .whatDoWhenTextIsBlank { setFirstRefereeNull() }
            .whatDoWhenTextMatchedEntity { referee -> saveFirstReferee(referee as Referee) }
            .whatDoWhenAddClicked { text ->
                run {
                    // создаем новый стадион
                    val referee = Referee().setEntityName(text)
                    // сохраняем стадион
                    saveFirstReferee(referee)
                    // показываем сообщение
                    Toast.makeText(
                        context,
                        getString(R.string.referee_add_message, referee.fullName),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .whatDoWhenInfoClicked { referee ->
                // показываем сообщение с полным именем стадиона
                Toast.makeText(
                    context,
                    referee.fullName,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

        binding.secondRefereeLayout
            .whatDoWhenTextIsBlank { setSecondRefereeNull() }
            .whatDoWhenTextMatchedEntity { referee -> saveSecondReferee(referee as Referee) }
            .whatDoWhenAddClicked { text ->
                run {
                    // создаем новый стадион
                    val referee = Referee().setEntityName(text)
                    // сохраняем стадион
                    saveSecondReferee(referee)
                    // показываем сообщение
                    Toast.makeText(
                        context,
                        getString(R.string.referee_add_message, referee.fullName),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .whatDoWhenInfoClicked { referee ->
                // показываем сообщение с полным именем стадиона
                Toast.makeText(
                    context,
                    referee.fullName,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

        binding.reserveRefereeLayout
            .whatDoWhenTextIsBlank { setReserveRefereeNull() }
            .whatDoWhenTextMatchedEntity { referee -> saveReserveReferee(referee as Referee) }
            .whatDoWhenAddClicked { text ->
                run {
                    // создаем новый стадион
                    val referee = Referee().setEntityName(text)
                    // сохраняем стадион
                    saveReserveReferee(referee)
                    // показываем сообщение
                    Toast.makeText(
                        context,
                        getString(R.string.referee_add_message, referee.fullName),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .whatDoWhenInfoClicked { referee ->
                // показываем сообщение с полным именем стадиона
                Toast.makeText(
                    context,
                    referee.fullName,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

        binding.inspectorLayout
            .whatDoWhenTextIsBlank { setInspectorNull() }
            .whatDoWhenTextMatchedEntity { referee -> saveInspector(referee as Referee) }
            .whatDoWhenAddClicked { text ->
                run {
                    // создаем новый стадион
                    val referee = Referee().setEntityName(text)
                    // сохраняем стадион
                    saveInspector(referee)
                    // показываем сообщение
                    Toast.makeText(
                        context,
                        getString(R.string.referee_add_message, referee.fullName),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .whatDoWhenInfoClicked { referee ->
                // показываем сообщение с полным именем стадиона
                Toast.makeText(
                    context,
                    referee.fullName,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

        binding.gamePaidCheckBox.apply {
            setOnCheckedChangeListener { _, isPaid -> gameWithAttributes.game.isPaid = isPaid }
        }

        binding.gamePassedCheckBox.apply {
            setOnCheckedChangeListener { _, isPassed ->
                gameWithAttributes.game.isPassed = isPassed
            }
        }

        binding.gameDateButton.setOnClickListener {
            DatePickerFragment
                .newInstance(gameWithAttributes.game.date, REQUEST_DATE)
                .show(parentFragmentManager, REQUEST_DATE)
        }

        binding.gameTimeButton.setOnClickListener {
            TimePickerFragment
                .newInstance(gameWithAttributes.game.date, REQUEST_TIME)
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
        Log.d(TAG, "onStop() called")
        saveGame()
    }

    private fun saveGame() {
        gameDetailViewModel.saveGame(gameWithAttributes)
    }

    private fun saveHomeTeam(team: Team) {
        gameDetailViewModel.saveHomeTeam(gameWithAttributes, team)
    }

    private fun saveGuestTeam(team: Team) {
        gameDetailViewModel.saveGuestTeam(gameWithAttributes, team)
    }

    private fun saveLeague(league: League) {
        gameDetailViewModel.saveLeague(gameWithAttributes, league)
    }

    private fun saveStadium(stadium: Stadium) {
        gameDetailViewModel.saveStadium(gameWithAttributes, stadium)
    }

    private fun saveChiefReferee(referee: Referee) {
        gameDetailViewModel.saveChiefReferee(gameWithAttributes, referee)
    }

    private fun saveFirstReferee(referee: Referee) {
        gameDetailViewModel.saveFirstReferee(gameWithAttributes, referee)
    }

    private fun saveSecondReferee(referee: Referee) {
        gameDetailViewModel.saveSecondReferee(gameWithAttributes, referee)
    }

    private fun saveReserveReferee(referee: Referee) {
        gameDetailViewModel.saveReserveReferee(gameWithAttributes, referee)
    }

    private fun saveInspector(referee: Referee) {
        gameDetailViewModel.saveInspector(gameWithAttributes, referee)
    }

    private fun setHomeTeamNull() {
        gameWithAttributes.homeTeam = null
    }

    private fun setGuestTeamNull() {
        gameWithAttributes.guestTeam = null
    }

    private fun setLeagueNull() {
        gameWithAttributes.league = null
    }

    private fun setStadiumNull() {
        gameWithAttributes.stadium = null
    }

    private fun setChiefRefereeNull() {
        gameWithAttributes.chiefReferee = null
    }

    private fun setFirstRefereeNull() {
        gameWithAttributes.firstReferee = null
    }

    private fun setSecondRefereeNull() {
        gameWithAttributes.secondReferee = null
    }

    private fun setReserveRefereeNull() {
        gameWithAttributes.reserveReferee = null
    }

    private fun setInspectorNull() {
        gameWithAttributes.inspector = null
    }

    private fun updateUI() {
        Log.d(TAG, "updateUI() called")
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
            gameWithAttributes.homeTeam,
            gameWithAttributes.guestTeam,
            gameWithAttributes.stadium,
            gameWithAttributes.league,
            gameWithAttributes.chiefReferee,
            gameWithAttributes.firstReferee,
            gameWithAttributes.secondReferee,
            gameWithAttributes.reserveReferee,
            gameWithAttributes.inspector
        )

        for (pair in textInputs.zip(attributesList)) {
            val textInput = pair.first
            val attribute = pair.second
            if (textInput.currentText.isBlank())
                textInput.setText(attribute?.shortName ?: "")
        }
        updateDate()
        updateTime()

        binding.gamePaidCheckBox.apply {
            isChecked = gameWithAttributes.game.isPaid
            jumpDrawablesToCurrentState()
        }
        binding.gamePassedCheckBox.apply {
            isChecked = gameWithAttributes.game.isPassed
            jumpDrawablesToCurrentState()
        }
    }

    private fun updateDate() {
        binding.gameDateButton.text =
            DateFormat.format(DATE_FORMAT, gameWithAttributes.game.date).toString()
    }

    private fun updateTime() {
        binding.gameTimeButton.text =
            DateFormat.format(TIME_FORMAT, gameWithAttributes.game.date).toString()
    }

    override fun onFragmentResult(requestKey: String, result: Bundle) {
        when (requestKey) {
            REQUEST_DATE -> {
                gameWithAttributes.game.date = DatePickerFragment.getSelectedDate(result)
                updateDate()
            }
            REQUEST_TIME -> {
                gameWithAttributes.game.date = TimePickerFragment.getSelectedTime(result)
                updateTime()
            }

            REQUEST_DELETE -> {
                when (DeleteDialog.getDeleteAnswer(result)) {
                    AlertDialog.BUTTON_POSITIVE -> {
                        gameDetailViewModel.deleteGame(gameWithAttributes.game)
                        findNavController().popBackStack()
                    }
                }
            }
        }
    }

    companion object {
        fun putGameId(gameId: UUID): Bundle {
            return Bundle().apply { putSerializable(ARG_GAME_ID, gameId) }
        }
    }
}