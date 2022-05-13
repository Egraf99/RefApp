package com.egraf.refapp

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.CheckBox
import androidx.fragment.app.Fragment
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
    private lateinit var stadiumListAdapter: ArrayAdapter<String>

    private lateinit var leagueLayout: TextInputLayout
    private lateinit var leagueAutoCompleteTextView: AutoCompleteTextView
    private lateinit var leagueWatcher: TextInputLayoutWatcher
    private lateinit var leagueListAdapter: ArrayAdapter<String>

    private lateinit var homeTeamLayout: TextInputLayout
    private lateinit var homeTeamAutoCompleteTextView: AutoCompleteTextView
    private lateinit var homeTeamWatcher: TextInputLayoutWatcher
    private lateinit var homeTeamListAdapter: ArrayAdapter<String>

    private lateinit var guestTeamLayout: TextInputLayout
    private lateinit var guestTeamAutoCompleteTextView: AutoCompleteTextView
    private lateinit var guestTeamWatcher: TextInputLayoutWatcher
    private lateinit var guestTeamListAdapter: ArrayAdapter<String>

    private lateinit var chiefRefereeLayout: TextInputLayout
    private lateinit var chiefRefereeAutoCompleteTextView: AutoCompleteTextView
    private lateinit var chiefRefereeWatcher: TextInputLayoutWatcher
    private lateinit var chiefRefereeListAdapter: ArrayAdapter<String>

    private lateinit var firstRefereeLayout: TextInputLayout
    private lateinit var firstRefereeAutoCompleteTextView: AutoCompleteTextView
    private lateinit var firstRefereeWatcher: TextInputLayoutWatcher
    private lateinit var firstRefereeListAdapter: ArrayAdapter<String>

    private lateinit var secondRefereeLayout: TextInputLayout
    private lateinit var secondRefereeAutoCompleteTextView: AutoCompleteTextView
    private lateinit var secondRefereeWatcher: TextInputLayoutWatcher
    private lateinit var secondRefereeListAdapter: ArrayAdapter<String>

    private lateinit var reserveRefereeLayout: TextInputLayout
    private lateinit var reserveRefereeAutoCompleteTextView: AutoCompleteTextView
    private lateinit var reserveRefereeWatcher: TextInputLayoutWatcher
    private lateinit var reserveRefereeListAdapter: ArrayAdapter<String>

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
        Log.d(TAG, "onCreate: $gameWithAttributes")

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
            updateAdapterList(stadiumListAdapter, stadiumsList)
            updateWatcherList(stadiumWatcher, stadiumsList)
        }
        gameDetailViewModel.leagueListLiveData.observe(viewLifecycleOwner) { leagues ->
            leaguesList = leagues
            updateAdapterList(leagueListAdapter, leaguesList)
            updateWatcherList(leagueWatcher, leaguesList)
        }
        gameDetailViewModel.teamListLiveData.observe(viewLifecycleOwner) { teams ->
            teamsList = teams
            val listTextView = listOf(homeTeamListAdapter, guestTeamListAdapter)
            val listWatcher = listOf(homeTeamWatcher, guestTeamWatcher)
            for (pair in listTextView.zip(listWatcher)) {
                updateAdapterList(pair.first, teamsList)
                updateWatcherList(pair.second, teamsList)
            }

        }
        gameDetailViewModel.refereeListLiveData.observe(viewLifecycleOwner) { referee ->
            refereeList = referee
            val listTextView = listOf(
                chiefRefereeListAdapter,
                firstRefereeListAdapter,
                secondRefereeListAdapter,
                reserveRefereeListAdapter
            )
            val listWatcher = listOf(
                chiefRefereeWatcher,
                firstRefereeWatcher,
                secondRefereeWatcher,
                reserveRefereeWatcher
            )
            for (pair in listTextView.zip(listWatcher)) {
                updateAdapterList(pair.first, refereeList)
                updateWatcherList(pair.second, refereeList)
            }
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
        homeTeamListAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.select_dialog_item,
            teamsList.map { it.getEntityName() })
        homeTeamAutoCompleteTextView.apply {
            threshold = 1
            addTextChangedListener(homeTeamWatcher)
            setAdapter(homeTeamListAdapter)
        }

        guestTeamWatcher =
            teamWatcher.build().setType(TypeTextInputWatcher.GUEST_TEAM).setParent(this)
                .setTextView(guestTeamAutoCompleteTextView).setLayout(guestTeamLayout)
        guestTeamListAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.select_dialog_item,
            teamsList.map { it.getEntityName() })
        guestTeamAutoCompleteTextView.apply {
            threshold = 1
            addTextChangedListener(guestTeamWatcher)
            setAdapter(guestTeamListAdapter)
        }

        stadiumWatcher =
            WatcherFactory().setType(TypeTextInputWatcher.STADIUM).setList(stadiumsList)
                .setParent(this)
                .build().setTextView(stadiumAutoCompleteTextView)
                .setLayout(stadiumLayout)
        stadiumListAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.select_dialog_item,
            stadiumsList.map { it.getEntityName() })
        stadiumAutoCompleteTextView.apply {
            threshold = 1
            addTextChangedListener(stadiumWatcher)
            setAdapter(stadiumListAdapter)
        }

        leagueWatcher =
            WatcherFactory().setType(TypeTextInputWatcher.LEAGUE).setList(leaguesList)
                .setParent(this)
                .build().setTextView(leagueAutoCompleteTextView)
                .setLayout(leagueLayout)
        leagueListAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.select_dialog_item,
            leaguesList.map { it.getEntityName() })
        leagueAutoCompleteTextView.apply {
            threshold = 1
            addTextChangedListener(leagueWatcher)
            setAdapter(leagueListAdapter)
        }

        val refereeWatcher =
            WatcherFactory().setType(TypeTextInputWatcher.REFEREE).setList(refereeList)
                .setParent(this)
        chiefRefereeWatcher = refereeWatcher.build().setType(TypeTextInputWatcher.CHIEF_REFEREE)
            .setTextView(chiefRefereeAutoCompleteTextView).setLayout(chiefRefereeLayout)
        chiefRefereeListAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.select_dialog_item,
            refereeList.map { it.getEntityName() })
        chiefRefereeAutoCompleteTextView.apply {
            threshold = 1
            addTextChangedListener(chiefRefereeWatcher)
            setAdapter(chiefRefereeListAdapter)
        }

        firstRefereeWatcher = refereeWatcher.build().setType(TypeTextInputWatcher.FIRST_REFEREE)
            .setTextView(firstRefereeAutoCompleteTextView).setLayout(firstRefereeLayout)
        firstRefereeListAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.select_dialog_item,
            refereeList.map { it.getEntityName() })
        firstRefereeAutoCompleteTextView.apply {
            threshold = 1
            addTextChangedListener(firstRefereeWatcher)
            setAdapter(firstRefereeListAdapter)
        }

        secondRefereeWatcher = refereeWatcher.build().setType(TypeTextInputWatcher.SECOND_REFEREE)
            .setTextView(secondRefereeAutoCompleteTextView).setLayout(secondRefereeLayout)
        secondRefereeListAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.select_dialog_item,
            refereeList.map { it.getEntityName() })
        secondRefereeAutoCompleteTextView.apply {
            threshold = 1
            addTextChangedListener(secondRefereeWatcher)
            setAdapter(secondRefereeListAdapter)
        }

        reserveRefereeWatcher = refereeWatcher.build().setType(TypeTextInputWatcher.RESERVE_REFEREE)
            .setTextView(reserveRefereeAutoCompleteTextView).setLayout(reserveRefereeLayout)
        reserveRefereeListAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.select_dialog_item,
            refereeList.map { it.getEntityName() })
        reserveRefereeAutoCompleteTextView.apply {
            threshold = 1
            addTextChangedListener(reserveRefereeWatcher)
            setAdapter(reserveRefereeListAdapter)
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

    private fun updateAdapterList(
        adapter: ArrayAdapter<String>,
        list: List<Entity>
    ) {
        adapter.clear()
        adapter.addAll(list.map { it.getEntityName() })
        adapter.notifyDataSetChanged()
    }

    private fun updateWatcherList(
        watcher: TextInputLayoutWatcher,
        list: List<Entity>
    ) {
        watcher.setList(list)
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
        saveGame()
    }

    override fun saveGame() {
        gameDetailViewModel.saveGame(gameWithAttributes)
    }

    private fun updateUI() {
        Log.d(TAG, "updateUI() called")
        if (homeTeamAutoCompleteTextView.text.isNullOrEmpty())
            homeTeamAutoCompleteTextView.setText(gameWithAttributes.homeTeam?.getEntityName())
        if (guestTeamAutoCompleteTextView.text.isNullOrEmpty())
            guestTeamAutoCompleteTextView.setText(gameWithAttributes.guestTeam?.getEntityName())
        if (stadiumAutoCompleteTextView.text.isNullOrBlank())
            stadiumAutoCompleteTextView.setText(gameWithAttributes.stadium?.getEntityName())
        if (leagueAutoCompleteTextView.text.isNullOrEmpty())
            leagueAutoCompleteTextView.setText(gameWithAttributes.league?.getEntityName())
        if (chiefRefereeAutoCompleteTextView.text.isNullOrEmpty())
            chiefRefereeAutoCompleteTextView.setText(gameWithAttributes.chiefReferee?.getEntityName())
        if (firstRefereeAutoCompleteTextView.text.isNullOrEmpty())
            firstRefereeAutoCompleteTextView.setText(gameWithAttributes.firstReferee?.getEntityName())
        if (secondRefereeAutoCompleteTextView.text.isNullOrEmpty())
            secondRefereeAutoCompleteTextView.setText(gameWithAttributes.secondReferee?.getEntityName())
        if (reserveRefereeAutoCompleteTextView.text.isNullOrEmpty())
            reserveRefereeAutoCompleteTextView.setText(gameWithAttributes.reserveReferee?.getEntityName())


        dateButton.text = DateFormat.format(DATE_FORMAT, gameWithAttributes.game.date).toString()
        timeButton.text = DateFormat.format(TIME_FORMAT, gameWithAttributes.game.date).toString()

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