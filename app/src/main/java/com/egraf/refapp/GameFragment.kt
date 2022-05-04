package com.egraf.refapp

import android.app.AlertDialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.Editable
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.ViewModelProvider
import com.egraf.refapp.database.entities.*
import com.google.android.material.textfield.TextInputLayout
import java.util.*
import kotlin.reflect.KClass

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
            updateAdapter(stadiumAutoCompleteTextView, stadiumsList.map { it.getEntityName() })
        }
        gameDetailViewModel.leagueListLiveData.observe(viewLifecycleOwner) { leagues ->
            leaguesList = leagues
            updateAdapter(leagueAutoCompleteTextView, leagues.map { it.getEntityName() })
        }
        gameDetailViewModel.teamListLiveData.observe(viewLifecycleOwner) { teams ->
            teamsList = teams
            val teamNameList = teams.map { it.getEntityName() }
            for (autoTextView in listOf(
                homeTeamAutoCompleteTextView,
                guestTeamAutoCompleteTextView,
            ))
                updateAdapter(autoTextView, teamNameList)
        }
        gameDetailViewModel.refereeListLiveData.observe(viewLifecycleOwner) { referee ->
            refereeList = referee
            val refereeNameList = referee.map { it.getEntityName() }
            for (autoTextView in listOf(
                chiefRefereeAutoCompleteTextView,
                firstRefereeAutoCompleteTextView,
                secondRefereeAutoCompleteTextView,
                reserveRefereeAutoCompleteTextView
            ))
                updateAdapter(autoTextView, refereeNameList)
        }

        parentFragmentManager.setFragmentResultListener(REQUEST_DATE, viewLifecycleOwner, this)
        parentFragmentManager.setFragmentResultListener(REQUEST_TIME, viewLifecycleOwner, this)
        parentFragmentManager.setFragmentResultListener(REQUEST_DELETE, viewLifecycleOwner, this)
    }

    private enum class GameAttribute {
        LEAGUE,
        REFEREE,
        STADIUM,
        TEAM
    }

    private fun getNamesList(
        attribute: GameAttribute
    ): List<Entity> {
        return when (attribute) {
            GameAttribute.TEAM -> teamsList
            GameAttribute.LEAGUE -> leaguesList
            GameAttribute.STADIUM -> stadiumsList
            GameAttribute.REFEREE -> refereeList
        }
    }

    private fun checkTextForChangeGameEntity(
        text: Editable?,
        attribute: GameAttribute,
        textInputLayout: TextInputLayout
    ): Entity? {
        val namesList = getNamesList(attribute)
        val matches = namesList.map { it.getEntityName() }.indexOf(text.toString())

        return when {
            text.isNullOrEmpty() -> {
                textInputLayout.isEndIconVisible = false
                textInputLayout.setEndIconOnClickListener(null)
                null
            }
            matches > 1 -> {
                textInputLayout.setEndIconDrawable(R.drawable.ic_info)
                textInputLayout.setEndIconTintList(
                    ContextCompat.getColorStateList(
                        requireContext(),
                        com.google.android.material.R.color.design_default_color_primary
                    )
                )
                namesList[matches]
            }
            else -> {
                textInputLayout.setEndIconDrawable(R.drawable.ic_add_outline)
                textInputLayout.setEndIconTintList(
                    ContextCompat.getColorStateList(
                        requireContext(),
                        R.color.green
                    )
                )
                null
            }
        }
    }

    private fun addNewEntity(
        text: Editable?,
        entity: Entity,
        textInputLayout: TextInputLayout,
        toastMessage: String
    ): Entity {
        textInputLayout.isEndIconVisible = false
        Toast.makeText(requireContext(), toastMessage, Toast.LENGTH_SHORT)
            .show()

        return entity.setEntityName(text.toString())
    }

    override fun onStart() {
        super.onStart()
        homeTeamAutoCompleteTextView.threshold = 1
        homeTeamAutoCompleteTextView.doAfterTextChanged { text ->
            gameWithAttributes.homeTeam =
                checkTextForChangeGameEntity(text, GameAttribute.TEAM, homeTeamLayout) as Team?
        }

        homeTeamLayout.setEndIconOnClickListener {
            gameWithAttributes.homeTeam = addNewEntity(
                homeTeamAutoCompleteTextView.text,
                Team(),
                homeTeamLayout,
                getString(
                    R.string.team_add_message,
                    homeTeamAutoCompleteTextView.text.toString()
                )
            ) as Team
        }

        guestTeamAutoCompleteTextView.threshold = 1
        guestTeamAutoCompleteTextView.doAfterTextChanged { text ->
            gameWithAttributes.guestTeam =
                checkTextForChangeGameEntity(text, GameAttribute.TEAM, guestTeamLayout) as Team?
        }
        guestTeamLayout.setEndIconOnClickListener {
            gameWithAttributes.guestTeam = addNewEntity(
                guestTeamAutoCompleteTextView.text,
                Team(),
                guestTeamLayout,
                getString(
                    R.string.team_add_message,
                    guestTeamAutoCompleteTextView.text.toString()
                )
            ) as Team
        }

        stadiumAutoCompleteTextView.threshold = 1
        stadiumAutoCompleteTextView.doAfterTextChanged { text ->
            gameWithAttributes.stadium =
                checkTextForChangeGameEntity(text, GameAttribute.STADIUM, stadiumLayout) as Stadium?
        }

        stadiumLayout.setEndIconOnClickListener {
            gameWithAttributes.stadium = addNewEntity(
                stadiumAutoCompleteTextView.text,
                Stadium(),
                stadiumLayout,
                getString(
                    R.string.stadium_add_message,
                    stadiumAutoCompleteTextView.text.toString()
                )
            ) as Stadium
        }

        leagueAutoCompleteTextView.threshold = 1
        leagueAutoCompleteTextView.doAfterTextChanged { text ->
            gameWithAttributes.league =
                checkTextForChangeGameEntity(text, GameAttribute.LEAGUE, leagueLayout) as League?
        }

        leagueLayout.setEndIconOnClickListener {
            gameWithAttributes.league = addNewEntity(
                leagueAutoCompleteTextView.text,
                League(),
                leagueLayout,
                getString(
                    R.string.league_add_message,
                    leagueAutoCompleteTextView.text.toString()
                )
            ) as League
        }

        chiefRefereeAutoCompleteTextView.threshold = 1
        chiefRefereeAutoCompleteTextView.doAfterTextChanged { text ->
            gameWithAttributes.chiefReferee =
                checkTextForChangeGameEntity(
                    text,
                    GameAttribute.REFEREE,
                    chiefRefereeLayout
                ) as Referee?
        }

        chiefRefereeLayout.setEndIconOnClickListener {
            gameWithAttributes.chiefReferee = addNewEntity(
                chiefRefereeAutoCompleteTextView.text,
                Referee(),
                chiefRefereeLayout,
                getString(
                    R.string.referee_add_message,
                    chiefRefereeAutoCompleteTextView.text.toString()
                )
            ) as Referee
        }

        firstRefereeAutoCompleteTextView.threshold = 1
        firstRefereeAutoCompleteTextView.doAfterTextChanged { text ->
            gameWithAttributes.firstReferee =
                checkTextForChangeGameEntity(
                    text,
                    GameAttribute.REFEREE,
                    firstRefereeLayout
                ) as Referee?
        }

        firstRefereeLayout.setEndIconOnClickListener {
            gameWithAttributes.firstReferee = addNewEntity(
                firstRefereeAutoCompleteTextView.text,
                Referee(),
                firstRefereeLayout,
                getString(
                    R.string.referee_add_message,
                    firstRefereeAutoCompleteTextView.text.toString()
                )
            ) as Referee
        }

        secondRefereeAutoCompleteTextView.threshold = 1
        secondRefereeAutoCompleteTextView.doAfterTextChanged { text ->
            gameWithAttributes.secondReferee =
                checkTextForChangeGameEntity(
                    text,
                    GameAttribute.REFEREE,
                    secondRefereeLayout
                ) as Referee?
        }

        secondRefereeLayout.setEndIconOnClickListener {
            gameWithAttributes.secondReferee = addNewEntity(
                secondRefereeAutoCompleteTextView.text,
                Referee(),
                secondRefereeLayout,
                getString(
                    R.string.referee_add_message,
                    secondRefereeAutoCompleteTextView.text.toString()
                )
            ) as Referee
        }

        reserveRefereeAutoCompleteTextView.threshold = 1
        reserveRefereeAutoCompleteTextView.doAfterTextChanged { text ->
            gameWithAttributes.reserveReferee =
                checkTextForChangeGameEntity(
                    text,
                    GameAttribute.REFEREE,
                    reserveRefereeLayout
                ) as Referee?
        }

        reserveRefereeLayout.setEndIconOnClickListener {
            gameWithAttributes.reserveReferee = addNewEntity(
                reserveRefereeAutoCompleteTextView.text,
                Referee(),
                reserveRefereeLayout,
                getString(
                    R.string.referee_add_message,
                    reserveRefereeAutoCompleteTextView.text.toString()
                )
            ) as Referee
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

}