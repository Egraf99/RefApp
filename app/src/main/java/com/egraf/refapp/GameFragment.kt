package com.egraf.refapp

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.ViewModelProvider
import com.egraf.refapp.database.entities.Game
import com.egraf.refapp.database.entities.GameWithStadium
import com.egraf.refapp.database.entities.Stadium
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

    private var callbacks: Callbacks? = null
    private lateinit var gameWithStadium: GameWithStadium
    private lateinit var homeTeamEditText: EditText
    private lateinit var guestTeamEditText: EditText
    private lateinit var stadiumEditText: EditText
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
        val stadium = Stadium()
        gameWithStadium = GameWithStadium(game, stadium)

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
        stadiumEditText = view.findViewById(R.id.stadium_edittext) as EditText
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
                this.gameWithStadium = game
                updateUI()
            }
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
                gameWithStadium.game.homeTeam = sequence.toString()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        }
        homeTeamEditText.addTextChangedListener(homeTeamWatcher)

        val guestTeamTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(sequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
                gameWithStadium.game.guestTeam = sequence.toString()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        }
        guestTeamEditText.addTextChangedListener(guestTeamTextWatcher)

        val stadiumWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(sequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
                gameWithStadium.stadium.name = sequence.toString()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        }
        stadiumEditText.addTextChangedListener(stadiumWatcher)

        val leagueWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(sequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
                gameWithStadium.game.league = sequence.toString()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        }
        leagueEditText.addTextChangedListener(leagueWatcher)

        gamePaidCheckBox.apply {
            setOnCheckedChangeListener { _, isPaid -> gameWithStadium.game.isPaid = isPaid }
        }

        dateButton.setOnClickListener {
            DatePickerFragment
                .newInstance(gameWithStadium.game.date, REQUEST_DATE)
                .show(parentFragmentManager, REQUEST_DATE)
        }

        timeButton.setOnClickListener {
            TimePickerFragment
                .newInstance(gameWithStadium.game.date, REQUEST_TIME)
                .show(parentFragmentManager, REQUEST_TIME)
        }

        buttonDelete.setOnClickListener {
            DeleteDialog
                .newInstance(REQUEST_DELETE)
                .show(parentFragmentManager, REQUEST_DELETE)
        }
    }

    override fun onStop() {
        super.onStop()
        gameDetailViewModel.saveGame(gameWithStadium.game, gameWithStadium.stadium)
    }

    private fun updateUI() {
        homeTeamEditText.setText(gameWithStadium.game.homeTeam)
        guestTeamEditText.setText(gameWithStadium.game.guestTeam)
        stadiumEditText.setText(gameWithStadium.stadium.name)
        leagueEditText.setText(gameWithStadium.game.league)
        dateButton.text = DateFormat.format(DATE_FORMAT, gameWithStadium.game.date).toString()
        timeButton.text = DateFormat.format(TIME_FORMAT, gameWithStadium.game.date).toString()
        gamePaidCheckBox.apply {
            isChecked = gameWithStadium.game.isPaid
            jumpDrawablesToCurrentState()
        }
    }

    override fun onFragmentResult(requestKey: String, result: Bundle) {
        when(requestKey) {
            REQUEST_DATE -> {
                gameWithStadium.game.date = DatePickerFragment.getSelectedDate(result)
                updateUI()
            }
            REQUEST_TIME -> {
                gameWithStadium.game.date = TimePickerFragment.getSelectedTime(result)
                updateUI()
            }

            REQUEST_DELETE -> {
                when(DeleteDialog.getDeleteAnswer(result)) {
                    AlertDialog.BUTTON_POSITIVE -> {
                        gameDetailViewModel.deleteGame(gameWithStadium.game)
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