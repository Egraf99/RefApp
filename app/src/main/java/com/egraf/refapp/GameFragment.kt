package com.egraf.refapp

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import java.lang.IllegalStateException
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

    private enum class TextEditType {
        DEFAULT,
        INFO,
        ADD
    }

    private enum class GameAttribute {
        LEAGUE,
        STADIUM,
        HOME_TEAM,
        GUEST_TEAM,
        CHIEF_REFEREE,
        FIRST_REFEREE,
        SECOND_REFEREE,
        RESERVE_REFEREE,
    }

    private inner class TextInputLayoutWatcher(
        val textInputLayout: TextInputLayout,
        val type: GameAttribute,
        var text: Editable? = null
    ) : TextWatcher {

        fun addNewEntity(text: Editable?) = run {
            when (type) {
                GameAttribute.HOME_TEAM -> {
                    val team = Team(name = text.toString().trim())
                    gameWithAttributes.homeTeam = team
                }

                GameAttribute.GUEST_TEAM -> {
                    val team = Team(name = text.toString().trim())
                    gameWithAttributes.guestTeam = team
                }
                GameAttribute.LEAGUE -> {
                    val league = League(name = text.toString().trim())
                    gameWithAttributes.league = league
                }
                GameAttribute.STADIUM -> {
                    val stadium = Stadium(name = text.toString().trim())
                    gameWithAttributes.stadium = stadium
                }
                else -> {
                    val name = text.toString().split(" ").toMutableList()
                    if (name.size == 1) {
//                        есть только одна фамилия, имя и отчество делаем пустыми
                        name.add("")
                        name.add("")
                    } else if (name.size == 2) {
//                        есть имя и фамилия, отчество делаем пустым
                        name.add("")
                    }
                    val referee =
                        Referee(firstName = name[1], secondName = name[0], thirdName = name[2])
                    when (type) {
                        GameAttribute.CHIEF_REFEREE -> {
                            gameWithAttributes.chiefReferee = referee
                        }
                        GameAttribute.FIRST_REFEREE -> {
                            gameWithAttributes.firstReferee = referee
                        }
                        GameAttribute.SECOND_REFEREE -> {
                            gameWithAttributes.secondReferee = referee
                        }
                        GameAttribute.RESERVE_REFEREE -> {
                            gameWithAttributes.reserveReferee = referee
                        }
                        else -> throw IllegalStateException("Unknown type $type")
                    }
                }
            }
        }


        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(text: Editable?) {
            this.text = text
            val namesList = getNamesList(type)
            val matches = namesList.map { it.getEntityName() }.indexOf(text.toString())
            when {
                text.isNullOrEmpty() -> {
//                пустой текст - убираем иконку взаимодействия
                    setEndIcon(TextEditType.DEFAULT)
                }
                matches == -1 -> {
//                нет совпадений по тексту - показывем иконку добавления Entity
                    setEndIcon(TextEditType.ADD)
                }
                else -> {
//                есть совпдение по тексту - показываем иконку info
                    setEndIcon(TextEditType.INFO)
                }
            }
        }

        var addedName = fun(text: Editable?): List<String> {
            return listOf(text.toString())
        }

        private fun getNamesList(
            attribute: GameAttribute
        ): List<Entity> {
            return when (attribute) {
                GameAttribute.HOME_TEAM, GameAttribute.GUEST_TEAM -> teamsList
                GameAttribute.LEAGUE -> leaguesList
                GameAttribute.STADIUM -> stadiumsList
                GameAttribute.CHIEF_REFEREE, GameAttribute.FIRST_REFEREE,
                GameAttribute.SECOND_REFEREE, GameAttribute.RESERVE_REFEREE -> refereeList
            }
        }

        private fun setEndIcon(
            type: TextEditType,
        ) {
            when (type) {
                TextEditType.DEFAULT -> {
                    textInputLayout.apply {
                        isEndIconVisible = false
                        setEndIconOnClickListener(null)
                    }
                }
                TextEditType.INFO -> {
                    textInputLayout.apply {
                        isEndIconVisible = true
                        setEndIconDrawable(R.drawable.ic_info)
                        setEndIconTintList(
                            ContextCompat.getColorStateList(
                                requireContext(),
                                com.google.android.material.R.color.design_default_color_primary
                            )
                        )
                        setEndIconOnClickListener {
                            Toast.makeText(requireContext(), "Info Clicked", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
                TextEditType.ADD -> {
                    textInputLayout.apply {
                        isEndIconVisible = true
                        setEndIconDrawable(R.drawable.ic_add_outline)
                        setEndIconTintList(
                            ContextCompat.getColorStateList(
                                requireContext(),
                                R.color.green
                            )
                        )
                        setEndIconOnClickListener {
                            addNewEntity(text)
                            Toast.makeText(requireContext(), "Add click", Toast.LENGTH_SHORT).show()
                            setEndIcon(TextEditType.INFO)
                        }
                    }
                }
            }
        }
    }


    override fun onStart() {
        super.onStart()
        homeTeamAutoCompleteTextView.threshold = 1
        homeTeamAutoCompleteTextView.addTextChangedListener(
            TextInputLayoutWatcher(
                homeTeamLayout,
                GameAttribute.HOME_TEAM
            )
        )

        guestTeamAutoCompleteTextView.threshold = 1
        guestTeamAutoCompleteTextView.addTextChangedListener(
            TextInputLayoutWatcher(
                guestTeamLayout,
                GameAttribute.GUEST_TEAM
            )
        )

        stadiumAutoCompleteTextView.threshold = 1
        stadiumAutoCompleteTextView.addTextChangedListener(
            TextInputLayoutWatcher(
                stadiumLayout,
                GameAttribute.STADIUM
            )
        )

        leagueAutoCompleteTextView.threshold = 1
        leagueAutoCompleteTextView.addTextChangedListener(
            TextInputLayoutWatcher(
                leagueLayout,
                GameAttribute.LEAGUE
            )
        )

        chiefRefereeAutoCompleteTextView.threshold = 1
        chiefRefereeAutoCompleteTextView.addTextChangedListener(
            TextInputLayoutWatcher(
                chiefRefereeLayout,
                GameAttribute.CHIEF_REFEREE
            )
        )

        firstRefereeAutoCompleteTextView.threshold = 1
        firstRefereeAutoCompleteTextView.addTextChangedListener(
            TextInputLayoutWatcher(
                firstRefereeLayout,
                GameAttribute.FIRST_REFEREE
            )
        )

        secondRefereeAutoCompleteTextView.threshold = 1
        secondRefereeAutoCompleteTextView.addTextChangedListener(
            TextInputLayoutWatcher(
                secondRefereeLayout,
                GameAttribute.SECOND_REFEREE
            )
        )

        reserveRefereeAutoCompleteTextView.threshold = 1
        reserveRefereeAutoCompleteTextView.addTextChangedListener(
            TextInputLayoutWatcher(
                reserveRefereeLayout,
                GameAttribute.RESERVE_REFEREE
            )
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