package com.egraf.refapp

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.format.DateFormat
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.ViewModelProvider
import com.egraf.refapp.database.entities.*
import com.google.android.material.textfield.TextInputLayout
import java.util.*

private const val TAG = "GameFragment"

private const val ARG_GAME_ID = "game_id"
private const val REQUEST_DATE = "DialogDate"
private const val REQUEST_TIME = "DialogTime"
private const val REQUEST_DELETE = "DialogDelete"
private const val DATE_FORMAT = "EEE dd.MM.yyyy"
private const val TIME_FORMAT = "HH:mm"

class GameFragment : Fragment(), FragmentResultListener {
    interface Callbacks {
        fun remoteGameDetail()
    }

    private var stadiumsList: List<Stadium> = emptyList()
    private var leaguesList: List<League> = emptyList()
    private var teamsList: List<Team> = emptyList()
    private var refereeList: List<Referee> = emptyList()

    private var callbacks: Callbacks? = null
    private lateinit var gameWithAttributes: GameWithAttributes
    private lateinit var stadiumLayout: TextInputLayout
    private lateinit var stadiumAutoCompleteTextView: AutoCompleteTextView
    private lateinit var leagueLayout: TextInputLayout
    private lateinit var leagueAutoCompleteTextView: AutoCompleteTextView
    private lateinit var homeTeamLayout: TextInputLayout
    private lateinit var homeTeamAutoCompleteTextView: AutoCompleteTextView
    private lateinit var guestTeamLayout: TextInputLayout
    private lateinit var guestTeamAutoCompleteTextView: AutoCompleteTextView
    private lateinit var chiefRefereeLayout: TextInputLayout
    private lateinit var chiefRefereeAutoCompleteTextView: AutoCompleteTextView
    private lateinit var firstRefereeLayout: TextInputLayout
    private lateinit var firstRefereeAutoCompleteTextView: AutoCompleteTextView
    private lateinit var secondRefereeLayout: TextInputLayout
    private lateinit var secondRefereeAutoCompleteTextView: AutoCompleteTextView
    private lateinit var reserveRefereeLayout: TextInputLayout
    private lateinit var reserveRefereeAutoCompleteTextView: AutoCompleteTextView
    private lateinit var dateButton: Button
    private lateinit var timeButton: Button
    private lateinit var gamePaidCheckBox: CheckBox
    private lateinit var gamePassedCheckBox: CheckBox
    private lateinit var buttonDelete: Button
    private val gameDetailViewModel: GameDetailViewModel by lazy {
        ViewModelProvider(this).get(GameDetailViewModel::class.java)
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
        val game = Game()
        gameWithAttributes = GameWithAttributes(game)
        Log.d(TAG, "___________ GameFragment onCreate ____________ $gameWithAttributes")

        val gameId = arguments?.getSerializable(ARG_GAME_ID) as UUID
        gameDetailViewModel.loadGame(gameId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_game, container, false)

        // Init views
        homeTeamLayout = view.findViewById(R.id.team_home_layout) as TextInputLayout
        homeTeamAutoCompleteTextView =
            view.findViewById(R.id.team_home_autocomplete) as AutoCompleteTextView
        guestTeamLayout = view.findViewById(R.id.team_guest_layout) as TextInputLayout
        guestTeamAutoCompleteTextView =
            view.findViewById(R.id.team_guest_autocomplete) as AutoCompleteTextView
        stadiumLayout = view.findViewById(R.id.stadium_layout) as TextInputLayout
        stadiumAutoCompleteTextView =
            view.findViewById(R.id.stadium_autocomplete) as AutoCompleteTextView
        leagueLayout = view.findViewById(R.id.league_layout) as TextInputLayout
        leagueAutoCompleteTextView =
            view.findViewById(R.id.league_autocomplete) as AutoCompleteTextView
        chiefRefereeLayout = view.findViewById(R.id.chief_referee_layout) as TextInputLayout
        chiefRefereeAutoCompleteTextView =
            view.findViewById(R.id.chief_referee_autocomplete) as AutoCompleteTextView
        firstRefereeLayout = view.findViewById(R.id.first_referee_layout) as TextInputLayout
        firstRefereeAutoCompleteTextView =
            view.findViewById(R.id.first_referee_autocomplete) as AutoCompleteTextView
        secondRefereeLayout = view.findViewById(R.id.second_referee_layout) as TextInputLayout
        secondRefereeAutoCompleteTextView =
            view.findViewById(R.id.second_referee_autocomplete) as AutoCompleteTextView
        reserveRefereeLayout = view.findViewById(R.id.reserve_referee_layout) as TextInputLayout
        reserveRefereeAutoCompleteTextView =
            view.findViewById(R.id.reserve_referee_autocomplete) as AutoCompleteTextView
        dateButton = view.findViewById(R.id.game_date) as Button
        timeButton = view.findViewById(R.id.game_time) as Button
        buttonDelete = view.findViewById(R.id.button_delete) as Button
        gamePaidCheckBox = view.findViewById(R.id.game_paid) as CheckBox
        gamePassedCheckBox = view.findViewById(R.id.game_passed) as CheckBox

        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gameDetailViewModel.gameLiveData.observe(viewLifecycleOwner) { game ->
            game?.let {
                this.gameWithAttributes = game
                updateUI()
            }
        }
        gameDetailViewModel.stadiumListLiveData.observe(viewLifecycleOwner) { stadiums ->
            stadiumsList = stadiums
            updateAdapter(stadiumAutoCompleteTextView, stadiumsList.map { it.name })
        }
        gameDetailViewModel.leagueListLiveData.observe(viewLifecycleOwner) { leagues ->
            leaguesList = leagues
            updateAdapter(leagueAutoCompleteTextView, leagues.map { it.name })
        }
        gameDetailViewModel.teamListLiveData.observe(viewLifecycleOwner) { teams ->
            teamsList = teams
            val teamNameList = teams.map { it.name }
            updateAdapter(homeTeamAutoCompleteTextView, teamNameList)
            updateAdapter(guestTeamAutoCompleteTextView, teamNameList)
        }
        gameDetailViewModel.refereeListLiveData.observe(viewLifecycleOwner) { referee ->
            refereeList = referee
            val refereeNameList = referee.map { it.secondName + " " + it.firstName }
            updateAdapter(chiefRefereeAutoCompleteTextView, refereeNameList)
            updateAdapter(firstRefereeAutoCompleteTextView, refereeNameList)
            updateAdapter(secondRefereeAutoCompleteTextView, refereeNameList)
            updateAdapter(reserveRefereeAutoCompleteTextView, refereeNameList)
        }

        parentFragmentManager.setFragmentResultListener(REQUEST_DATE, viewLifecycleOwner, this)
        parentFragmentManager.setFragmentResultListener(REQUEST_TIME, viewLifecycleOwner, this)
        parentFragmentManager.setFragmentResultListener(REQUEST_DELETE, viewLifecycleOwner, this)
    }

    override fun onStart() {
        super.onStart()

        homeTeamAutoCompleteTextView.threshold = 1
        updateAdapter(homeTeamAutoCompleteTextView, teamsList.map { it.name })
        homeTeamAutoCompleteTextView.doAfterTextChanged { text ->
            if (text.isNullOrEmpty()) {
                gameWithAttributes.homeTeam = null
                showEndButton(homeTeamLayout, false)
                return@doAfterTextChanged
            }

//            индекс стадиона из списка, название которого совпало с текстом
            val indexStadiumCoincidence = teamsList.map { it.name }.indexOf(text.toString())
            if (indexStadiumCoincidence < 0) {
                // показываем кнопку добавелния стадиона
                showEndButton(homeTeamLayout, true)
            } else {
                // скрывам кнопку добавления стадиона
                showEndButton(homeTeamLayout, false)
                // обновляем стадион игры
                gameWithAttributes.homeTeam = teamsList[indexStadiumCoincidence]
            }
        }

        homeTeamLayout.setEndIconOnClickListener {
            val newHomeTeamName = homeTeamAutoCompleteTextView.text.toString()
            if (newHomeTeamName == "")
                return@setEndIconOnClickListener
            gameWithAttributes.homeTeam = Team(name = newHomeTeamName)
            showEndButton(homeTeamLayout, false)
            Toast.makeText(requireContext(), "Home Team $newHomeTeamName added", Toast.LENGTH_SHORT)
                .show()
        }

        guestTeamAutoCompleteTextView.threshold = 1
        updateAdapter(guestTeamAutoCompleteTextView, teamsList.map { it.name })
        guestTeamAutoCompleteTextView.doAfterTextChanged { text ->
            if (text.isNullOrEmpty()) {
                gameWithAttributes.guestTeam = null
                showEndButton(guestTeamLayout, false)
                return@doAfterTextChanged
            }

//            индекс стадиона из списка, название которого совпало с текстом
            val indexStadiumCoincidence = teamsList.map { it.name }.indexOf(text.toString())
            if (indexStadiumCoincidence < 0) {
                // показываем кнопку добавелния стадиона
                showEndButton(guestTeamLayout, true)
            } else {
                // скрывам кнопку добавления стадиона
                showEndButton(guestTeamLayout, false)
                // обновляем стадион игры
                gameWithAttributes.guestTeam = teamsList[indexStadiumCoincidence]
            }
        }

        guestTeamLayout.setEndIconOnClickListener {
            val newGuestTeamName = guestTeamAutoCompleteTextView.text.toString()
            if (newGuestTeamName == "")
                return@setEndIconOnClickListener
            gameWithAttributes.guestTeam = Team(name = newGuestTeamName)
            showEndButton(guestTeamLayout, false)
            Toast.makeText(requireContext(), "Guest Team $newGuestTeamName added", Toast.LENGTH_SHORT)
                .show()
        }

        stadiumAutoCompleteTextView.threshold = 1
        updateAdapter(stadiumAutoCompleteTextView, stadiumsList.map { it.name })
        stadiumAutoCompleteTextView.doAfterTextChanged { text ->
            if (text.isNullOrEmpty()) {
                gameWithAttributes.stadium = null
                showEndButton(stadiumLayout, false)
                return@doAfterTextChanged
            }

//            индекс стадиона из списка, название которого совпало с текстом
            val indexStadiumCoincidence = stadiumsList.map { it.name }.indexOf(text.toString())
            if (indexStadiumCoincidence < 0) {
                // показываем кнопку добавелния стадиона
                showEndButton(stadiumLayout, true)
            } else {
                // скрывам кнопку добавления стадиона
                showEndButton(stadiumLayout, false)
                // обновляем стадион игры
                gameWithAttributes.stadium = stadiumsList[indexStadiumCoincidence]
            }
        }

        stadiumLayout.setEndIconOnClickListener {
            val newStadiumName = stadiumAutoCompleteTextView.text.toString()
            if (newStadiumName == "")
                return@setEndIconOnClickListener
            gameWithAttributes.stadium = Stadium(name = newStadiumName)
            showEndButton(stadiumLayout, false)
            Toast.makeText(requireContext(), "Stadium $newStadiumName added", Toast.LENGTH_SHORT)
                .show()
        }

        leagueAutoCompleteTextView.threshold = 1
        updateAdapter(leagueAutoCompleteTextView, leaguesList.map { it.name })
        leagueAutoCompleteTextView.doAfterTextChanged { text ->
            if (text.isNullOrEmpty()) {
                gameWithAttributes.league = null
                showEndButton(leagueLayout, false)
                return@doAfterTextChanged
            }

//            индекс стадиона из списка, название которого совпало с текстом
            val indexStadiumCoincidence = leaguesList.map { it.name }.indexOf(text.toString())
            if (indexStadiumCoincidence < 0) {
                // показываем кнопку добавелния стадиона
                showEndButton(leagueLayout, true)
            } else {
                // скрывам кнопку добавления стадиона
                showEndButton(leagueLayout, false)
                // обновляем стадион игры
                gameWithAttributes.league = leaguesList[indexStadiumCoincidence]
            }
        }

        leagueLayout.setEndIconOnClickListener {
            val newLeagueName = leagueAutoCompleteTextView.text.toString()
            if (newLeagueName == "")
                return@setEndIconOnClickListener
            gameWithAttributes.league = League(name = newLeagueName)
            showEndButton(leagueLayout, false)
            Toast.makeText(requireContext(), "League $newLeagueName added", Toast.LENGTH_SHORT)
                .show()
        }

        chiefRefereeAutoCompleteTextView.threshold = 1
        updateAdapter(
            chiefRefereeAutoCompleteTextView,
            refereeList.map { it.secondName + " " + it.firstName })
        chiefRefereeAutoCompleteTextView.doAfterTextChanged { text ->
            if (text.isNullOrEmpty()) {
                gameWithAttributes.chiefReferee = null
                showEndButton(chiefRefereeLayout, false)
                return@doAfterTextChanged
            }

//            индекс стадиона из списка, название которого совпало с текстом
            val indexStadiumCoincidence =
                refereeList.map { it.secondName + " " + it.firstName }.indexOf(text.toString())
            if (indexStadiumCoincidence < 0) {
                // показываем кнопку добавелния стадиона
                showEndButton(chiefRefereeLayout, true)
            } else {
                // скрывам кнопку добавления стадиона
                showEndButton(chiefRefereeLayout, false)
                // обновляем стадион игры
                gameWithAttributes.chiefReferee = refereeList[indexStadiumCoincidence]
            }
        }

        chiefRefereeLayout.setEndIconOnClickListener {
            val newRefereeName = chiefRefereeAutoCompleteTextView.text.toString().split(" ")
            val firstName = newRefereeName[0]
            var secondName = ""
            var thirdName = ""
            if (firstName == "")
                return@setEndIconOnClickListener
            if (newRefereeName.size > 1)
                secondName = newRefereeName[1]
            if (newRefereeName.size > 2)
                thirdName = newRefereeName[2]
            gameWithAttributes.chiefReferee = Referee(
                firstName = firstName,
                secondName = secondName,
                thirdName = thirdName
            )
            showEndButton(chiefRefereeLayout, false)
            Toast.makeText(
                requireContext(),
                "Referee $firstName $secondName added",
                Toast.LENGTH_SHORT
            )
                .show()
        }


        gamePaidCheckBox.apply {
            setOnCheckedChangeListener { _, isPaid -> gameWithAttributes.game.isPaid = isPaid }
        }

        gamePassedCheckBox.apply {
            setOnCheckedChangeListener { _, isPassed ->
                gameWithAttributes.game.isPassed = isPassed
            }
        }

        dateButton.setOnClickListener {
            DatePickerFragment
                .newInstance(gameWithAttributes.game.date, REQUEST_DATE)
                .show(parentFragmentManager, REQUEST_DATE)
        }

        timeButton.setOnClickListener {
            TimePickerFragment
                .newInstance(gameWithAttributes.game.date, REQUEST_TIME)
                .show(parentFragmentManager, REQUEST_TIME)
        }

        buttonDelete.setOnClickListener {
            DeleteDialog
                .newInstance(REQUEST_DELETE)
                .show(parentFragmentManager, REQUEST_DELETE)
        }
    }
    private fun showEndButton(layout: TextInputLayout, isShow: Boolean) {
        Log.d(TAG, "showEndButton $isShow")
        layout.setEndIconActivated(isShow)
        layout.isEndIconVisible = isShow
    }

    private fun updateAdapter(textView: AutoCompleteTextView, list: List<String>) {
        textView.setAdapter(
            ArrayAdapter(
                requireContext(),
                android.R.layout.select_dialog_item,
                list)
        )
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "__________ GameFragment onStop ____________ $gameWithAttributes")
        gameDetailViewModel.saveGame(gameWithAttributes)
    }

    private fun updateUI() {
        Log.d(TAG, "_________ GameFragment updateUI __________ $gameWithAttributes")
        homeTeamAutoCompleteTextView.setText(gameWithAttributes.homeTeam?.name)
        guestTeamAutoCompleteTextView.setText(gameWithAttributes.guestTeam?.name)
        stadiumAutoCompleteTextView.setText(gameWithAttributes.stadium?.name)
        leagueAutoCompleteTextView.setText(gameWithAttributes.league?.name)
        dateButton.text = DateFormat.format(DATE_FORMAT, gameWithAttributes.game.date).toString()
        timeButton.text = DateFormat.format(TIME_FORMAT, gameWithAttributes.game.date).toString()
        chiefRefereeAutoCompleteTextView.setText(gameWithAttributes.chiefReferee?.secondName + " " + gameWithAttributes.chiefReferee?.firstName)
        gamePaidCheckBox.apply {
            isChecked = gameWithAttributes.game.isPaid
            jumpDrawablesToCurrentState()
        }
        gamePassedCheckBox.apply {
            isChecked = gameWithAttributes.game.isPassed
            jumpDrawablesToCurrentState()
        }
    }

    override fun onFragmentResult(requestKey: String, result: Bundle) {
        when(requestKey) {
            REQUEST_DATE -> {
                gameWithAttributes.game.date = DatePickerFragment.getSelectedDate(result)
                updateUI()
            }
            REQUEST_TIME -> {
                gameWithAttributes.game.date = TimePickerFragment.getSelectedTime(result)
                updateUI()
            }

            REQUEST_DELETE -> {
                when(DeleteDialog.getDeleteAnswer(result)) {
                    AlertDialog.BUTTON_POSITIVE -> {
                        gameDetailViewModel.deleteGame(gameWithAttributes.game)
                        callbacks?.remoteGameDetail()
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