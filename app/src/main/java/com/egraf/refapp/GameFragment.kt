package com.egraf.refapp

import android.app.AlertDialog
import android.content.Context
import android.content.Entity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.ViewModelProvider
import com.egraf.refapp.database.entities.*
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

    private var callbacks: Callbacks? = null
    private lateinit var gameWithAttributes: GameWithAttributes
    private lateinit var homeTeamEditText: EditText
    private lateinit var guestTeamEditText: EditText
    private lateinit var stadiumAutoCompleteTextView: AutoCompleteTextView
    private lateinit var leagueEditText: EditText
    private lateinit var dateButton: Button
    private lateinit var timeButton: Button
    private lateinit var gamePaidCheckBox: CheckBox
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
        homeTeamEditText = view.findViewById(R.id.team_home_edittext) as EditText
        guestTeamEditText = view.findViewById(R.id.team_guest_edittext) as EditText
        stadiumAutoCompleteTextView =
            view.findViewById(R.id.stadium_autocomplete) as AutoCompleteTextView
        leagueEditText = view.findViewById(R.id.league_edittext) as EditText
        dateButton = view.findViewById(R.id.game_date) as Button
        timeButton = view.findViewById(R.id.game_time) as Button
        buttonDelete = view.findViewById(R.id.button_delete) as Button
        gamePaidCheckBox = view.findViewById(R.id.game_paid) as CheckBox

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
            updateStadiumAdapter()
        }

        parentFragmentManager.setFragmentResultListener(REQUEST_DATE, viewLifecycleOwner, this)
        parentFragmentManager.setFragmentResultListener(REQUEST_TIME, viewLifecycleOwner, this)
        parentFragmentManager.setFragmentResultListener(REQUEST_DELETE, viewLifecycleOwner, this)
    }

    override fun onStart() {
        super.onStart()

        val homeTeamWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(sequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!sequence.isNullOrBlank()) {
                    if (gameWithAttributes.homeTeam == null)
                        gameWithAttributes.homeTeam = Team()

                    gameWithAttributes.homeTeam!!.name = sequence.toString()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        }
        homeTeamEditText.addTextChangedListener(homeTeamWatcher)

        val guestTeamTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(sequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!sequence.isNullOrBlank()) {
                    if (gameWithAttributes.guestTeam == null)
                        gameWithAttributes.guestTeam = Team()

                    gameWithAttributes.guestTeam!!.name = sequence.toString()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        }
        guestTeamEditText.addTextChangedListener(guestTeamTextWatcher)

        stadiumAutoCompleteTextView.threshold = 1
        updateStadiumAdapter()
        stadiumAutoCompleteTextView.setOnItemClickListener { _, _, i, _ ->
            gameWithAttributes.stadium = stadiumsList[i]
        }

        val leagueWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(sequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!sequence.isNullOrBlank()) {
                    if (gameWithAttributes.league == null)
                        gameWithAttributes.league = League()

                    gameWithAttributes.league!!.name = sequence.toString()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        }
        leagueEditText.addTextChangedListener(leagueWatcher)

        gamePaidCheckBox.apply {
            setOnCheckedChangeListener { _, isPaid -> gameWithAttributes.game.isPaid = isPaid }
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

    private inner class StadiumAdapter(context: Context, resource: Int, objects: List<String>) :
        ArrayAdapter<String>(context, resource, objects) {
        private val size = objects.size

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = if (position < size) {
                Log.d(TAG, ")))))))))))))")
                layoutInflater.inflate(R.layout.add_entity_button, parent, false)
            } else {
                Log.d(TAG, "(((((((((((((")
                layoutInflater.inflate(android.R.layout.select_dialog_item, parent, false)
            }
            return view
        }

        override fun getCount(): Int {
            return size - 1
        }
    }

    private fun updateStadiumAdapter() {
        stadiumAutoCompleteTextView.setAdapter(
            ArrayAdapter(
                requireContext(),
                android.R.layout.select_dialog_item,
                stadiumsList.map { it.name })
        )
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "__________ GameFragment onStop ____________ $gameWithAttributes")
        checkGameAttributes()
        gameDetailViewModel.saveGame(gameWithAttributes)
    }

    private fun checkGameAttributes() {
        if (gameWithAttributes.stadium?.name != stadiumAutoCompleteTextView.text.toString())
            gameWithAttributes.stadium = Stadium(name = stadiumAutoCompleteTextView.text.toString())
    }

    private fun updateUI() {
        Log.d(TAG, "_________ GameFragment updateUI __________ $gameWithAttributes")
        homeTeamEditText.setText(gameWithAttributes.homeTeam?.name)
        guestTeamEditText.setText(gameWithAttributes.guestTeam?.name)
        stadiumAutoCompleteTextView.setText(gameWithAttributes.stadium?.name)
        leagueEditText.setText(gameWithAttributes.league?.name)
        dateButton.text = DateFormat.format(DATE_FORMAT, gameWithAttributes.game.date).toString()
        timeButton.text = DateFormat.format(TIME_FORMAT, gameWithAttributes.game.date).toString()
        gamePaidCheckBox.apply {
            isChecked = gameWithAttributes.game.isPaid
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