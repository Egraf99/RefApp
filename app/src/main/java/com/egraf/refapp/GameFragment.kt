package com.egraf.refapp

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.ViewModelProvider
import com.egraf.refapp.database.entities.*
import com.google.android.material.textfield.TextInputLayout
import java.lang.IllegalStateException
import java.util.*

private const val TAG = "GameFragment"

private const val ARG_GAME_ID = "game_id"
private const val REQUEST_DATE = "DialogDate"
private const val REQUEST_TIME = "DialogTime"
private const val REQUEST_DELETE = "DialogDelete"
private const val DATE_FORMAT = "EEE dd.MM.yyyy"
private const val TIME_FORMAT = "HH:mm"

class GameFragment : Fragment(), FragmentResultListener, TextInputLayoutWatcher.Callbacks {
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
    private lateinit var stadiumWatcher: TextInputLayoutWatcher
    private lateinit var leagueLayout: TextInputLayout
    private lateinit var leagueAutoCompleteTextView: AutoCompleteTextView
    private lateinit var leagueWatcher: TextInputLayoutWatcher
    private lateinit var homeTeamLayout: TextInputLayout
    private lateinit var homeTeamAutoCompleteTextView: AutoCompleteTextView
    private lateinit var homeTeamWatcher: TextInputLayoutWatcher
    private lateinit var guestTeamLayout: TextInputLayout
    private lateinit var guestTeamAutoCompleteTextView: AutoCompleteTextView
    private lateinit var guestTeamWatcher: TextInputLayoutWatcher
    private lateinit var chiefRefereeLayout: TextInputLayout
    private lateinit var chiefRefereeAutoCompleteTextView: AutoCompleteTextView
    private lateinit var chiefRefereeWatcher: TextInputLayoutWatcher
    private lateinit var firstRefereeLayout: TextInputLayout
    private lateinit var firstRefereeAutoCompleteTextView: AutoCompleteTextView
    private lateinit var firstRefereeWatcher: TextInputLayoutWatcher
    private lateinit var secondRefereeLayout: TextInputLayout
    private lateinit var secondRefereeAutoCompleteTextView: AutoCompleteTextView
    private lateinit var secondRefereeWatcher: TextInputLayoutWatcher
    private lateinit var reserveRefereeLayout: TextInputLayout
    private lateinit var reserveRefereeAutoCompleteTextView: AutoCompleteTextView
    private lateinit var reserveRefereeWatcher: TextInputLayoutWatcher
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
            updateAdapter(stadiumAutoCompleteTextView, stadiumWatcher, stadiumsList)
        }
        gameDetailViewModel.leagueListLiveData.observe(viewLifecycleOwner) { leagues ->
            leaguesList = leagues
            updateAdapter(leagueAutoCompleteTextView, leagueWatcher, leaguesList)
        }
        gameDetailViewModel.teamListLiveData.observe(viewLifecycleOwner) { teams ->
            teamsList = teams
            val listTextView = listOf(homeTeamAutoCompleteTextView, guestTeamAutoCompleteTextView)
            val listWatcher = listOf(homeTeamWatcher, guestTeamWatcher)
            for (pair in listTextView.zip(listWatcher))
                updateAdapter(pair.first, pair.second, teamsList)
        }
        gameDetailViewModel.refereeListLiveData.observe(viewLifecycleOwner) { referee ->
            refereeList = referee
            val listTextView = listOf(
                chiefRefereeAutoCompleteTextView,
                firstRefereeAutoCompleteTextView,
                secondRefereeAutoCompleteTextView,
                reserveRefereeAutoCompleteTextView
            )
            val listWatcher = listOf(
                chiefRefereeWatcher,
                firstRefereeWatcher,
                secondRefereeWatcher,
                reserveRefereeWatcher
            )
            for (pair in listTextView.zip(listWatcher))
                updateAdapter(pair.first, pair.second, refereeList)
        }

        parentFragmentManager.setFragmentResultListener(REQUEST_DATE, viewLifecycleOwner, this)
        parentFragmentManager.setFragmentResultListener(REQUEST_TIME, viewLifecycleOwner, this)
        parentFragmentManager.setFragmentResultListener(REQUEST_DELETE, viewLifecycleOwner, this)
    }

    override fun onStart() {
        super.onStart()
        val teamWatcher =
            WatcherFactory().setType(TypeTextInputWatcher.TEAM).setList(teamsList).setParent(this)

        homeTeamWatcher = teamWatcher.build().setType(TypeTextInputWatcher.HOME_TEAM)
            .setTextView(homeTeamAutoCompleteTextView).setLayout(homeTeamLayout).setParent(this)
        homeTeamAutoCompleteTextView.threshold = 1
        homeTeamAutoCompleteTextView.addTextChangedListener(
            homeTeamWatcher
        )

        guestTeamWatcher =
            teamWatcher.build().setType(TypeTextInputWatcher.GUEST_TEAM).setParent(this)
                .setTextView(guestTeamAutoCompleteTextView).setLayout(guestTeamLayout)
        guestTeamAutoCompleteTextView.threshold = 1
        guestTeamAutoCompleteTextView.addTextChangedListener(
            homeTeamWatcher
        )

        stadiumWatcher =
            WatcherFactory().setType(TypeTextInputWatcher.STADIUM).setList(stadiumsList).setParent(this)
                .build().setTextView(stadiumAutoCompleteTextView)
                .setLayout(stadiumLayout)
        stadiumAutoCompleteTextView.threshold = 1
        stadiumAutoCompleteTextView.addTextChangedListener(
            stadiumWatcher
        )
        stadiumAutoCompleteTextView.setOnFocusChangeListener { view, b ->
            Log.d(TAG, "onStart() called with: view = $view, b = $b")
        }

        leagueWatcher =
            WatcherFactory().setType(TypeTextInputWatcher.LEAGUE).setList(leaguesList).setParent(this)
                .build().setTextView(leagueAutoCompleteTextView)
                .setLayout(leagueLayout)
        leagueAutoCompleteTextView.threshold = 1
        leagueAutoCompleteTextView.addTextChangedListener(
            leagueWatcher
        )

        val refereeWatcher =
            WatcherFactory().setType(TypeTextInputWatcher.REFEREE).setList(refereeList).setParent(this)
        chiefRefereeWatcher = refereeWatcher.build().setType(TypeTextInputWatcher.CHIEF_REFEREE)
            .setTextView(chiefRefereeAutoCompleteTextView).setLayout(chiefRefereeLayout)
        chiefRefereeAutoCompleteTextView.threshold = 1
        chiefRefereeAutoCompleteTextView.addTextChangedListener(
            chiefRefereeWatcher
        )

        firstRefereeWatcher = refereeWatcher.build().setType(TypeTextInputWatcher.FIRST_REFEREE)
            .setTextView(firstRefereeAutoCompleteTextView).setLayout(firstRefereeLayout)
        firstRefereeAutoCompleteTextView.threshold = 1
        firstRefereeAutoCompleteTextView.addTextChangedListener(
            firstRefereeWatcher
        )

        secondRefereeWatcher = refereeWatcher.build().setType(TypeTextInputWatcher.SECOND_REFEREE)
            .setTextView(secondRefereeAutoCompleteTextView).setLayout(secondRefereeLayout)
        secondRefereeAutoCompleteTextView.threshold = 1
        secondRefereeAutoCompleteTextView.addTextChangedListener(
            secondRefereeWatcher
        )

        reserveRefereeWatcher = refereeWatcher.build().setType(TypeTextInputWatcher.RESERVE_REFEREE)
            .setTextView(reserveRefereeAutoCompleteTextView).setLayout(reserveRefereeLayout)
        reserveRefereeAutoCompleteTextView.threshold = 1
        reserveRefereeAutoCompleteTextView.addTextChangedListener(
            reserveRefereeWatcher
        )

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

    private fun updateAdapter(
        textView: AutoCompleteTextView,
        watcher: TextInputLayoutWatcher,
        list: List<Entity>
    ) {
        watcher.setList(list)
        textView.setAdapter(
            ArrayAdapter(
                requireContext(),
                android.R.layout.select_dialog_item,
                list.map { it.getEntityName() })
        )
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "__________ GameFragment onStop ____________ $gameWithAttributes")
        saveGame()
    }

    override fun saveGame() {
        gameDetailViewModel.saveGame(gameWithAttributes)
    }

    private fun updateUI() {
        Log.d(TAG, "_________ GameFragment updateUI __________ $gameWithAttributes")
        homeTeamAutoCompleteTextView.setText(gameWithAttributes.homeTeam?.getEntityName())
        guestTeamAutoCompleteTextView.setText(gameWithAttributes.guestTeam?.getEntityName())
        stadiumAutoCompleteTextView.setText(gameWithAttributes.stadium?.getEntityName())
        leagueAutoCompleteTextView.setText(gameWithAttributes.league?.getEntityName())
        dateButton.text = DateFormat.format(DATE_FORMAT, gameWithAttributes.game.date).toString()
        timeButton.text = DateFormat.format(TIME_FORMAT, gameWithAttributes.game.date).toString()
        chiefRefereeAutoCompleteTextView.setText(gameWithAttributes.chiefReferee?.getEntityName())
        firstRefereeAutoCompleteTextView.setText(gameWithAttributes.firstReferee?.getEntityName())
        secondRefereeAutoCompleteTextView.setText(gameWithAttributes.secondReferee?.getEntityName())
        reserveRefereeAutoCompleteTextView.setText(gameWithAttributes.reserveReferee?.getEntityName())
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

    override fun changeGameEntity(entity: Entity?, type: TypeTextInputWatcher?) {
        when (type) {
            TypeTextInputWatcher.HOME_TEAM -> gameWithAttributes.homeTeam = entity as Team?
            TypeTextInputWatcher.GUEST_TEAM -> gameWithAttributes.guestTeam = entity as Team?
            TypeTextInputWatcher.LEAGUE -> gameWithAttributes.league = entity as League?
            TypeTextInputWatcher.STADIUM -> gameWithAttributes.stadium = entity as Stadium?
            TypeTextInputWatcher.CHIEF_REFEREE -> gameWithAttributes.chiefReferee =
                entity as Referee?
            TypeTextInputWatcher.FIRST_REFEREE -> gameWithAttributes.firstReferee =
                entity as Referee?
            TypeTextInputWatcher.SECOND_REFEREE -> gameWithAttributes.secondReferee =
                entity as Referee?
            TypeTextInputWatcher.RESERVE_REFEREE -> gameWithAttributes.reserveReferee =
                entity as Referee?
            else -> {throw IllegalStateException("Illegal type: $type")}
        }
    }
}