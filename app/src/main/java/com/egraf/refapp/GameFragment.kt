package com.egraf.refapp

import DatePickerFragment
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import java.util.*

private const val ARG_GAME_ID = "game_id"
private const val ARG_DATE = "date"
private const val REQUEST_DATE = "DialogDate"
private const val DIALOG_DATE = "DialogDate"

class GameFragment : Fragment(), FragmentResultListener {
    private lateinit var game: Game
    private lateinit var homeTeamEditText: EditText
    private lateinit var guestTeamEditText: EditText
    private lateinit var stadiumEditText: EditText
    private lateinit var leagueEditText: EditText
    private lateinit var dateButton: Button
    private lateinit var gamePaidCheckBox: CheckBox
    private lateinit var buttonDelete: Button
    private val gameDetailViewModel: GameDetailViewModel by lazy {
        ViewModelProvider(this).get(GameDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        game = Game()

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
        buttonDelete = view.findViewById(R.id.button_delete) as Button
        gamePaidCheckBox = view.findViewById(R.id.game_paid) as CheckBox

        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gameDetailViewModel.gameLiveData.observe(viewLifecycleOwner) { game ->
            game?.let {
                this.game = game
                updateUI()
            }
        }

        parentFragmentManager.setFragmentResultListener(REQUEST_DATE, viewLifecycleOwner, this)
    }

    override fun onStart() {
        super.onStart()

        val homeTeamWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(sequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
                game.homeTeam = sequence.toString()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        }
        homeTeamEditText.addTextChangedListener(homeTeamWatcher)

        val guestTeamTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(sequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
                game.guestTeam = sequence.toString()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        }
        guestTeamEditText.addTextChangedListener(guestTeamTextWatcher)

        val stadiumWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(sequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
                game.stadium = sequence.toString()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        }
        stadiumEditText.addTextChangedListener(stadiumWatcher)

        val leagueWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(sequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
                game.league = sequence.toString()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        }
        leagueEditText.addTextChangedListener(leagueWatcher)

        gamePaidCheckBox.apply {
            setOnCheckedChangeListener { _, isPaid -> game.isPaid = isPaid }
        }

        dateButton.setOnClickListener {
            DatePickerFragment
                .newInstance(game.date, REQUEST_DATE)
                .show(parentFragmentManager, REQUEST_DATE)
        }

        buttonDelete.setOnClickListener {
            gameDetailViewModel.deleteGame(game)
            parentFragmentManager.beginTransaction().remove(this).commit()
            parentFragmentManager.popBackStack()
        }
    }

    override fun onStop() {
        super.onStop()
        gameDetailViewModel.saveGame(game)
    }

    private fun updateUI() {
        homeTeamEditText.setText(game.homeTeam)
        guestTeamEditText.setText(game.guestTeam)
        stadiumEditText.setText(game.stadium)
        leagueEditText.setText(game.league)
        dateButton.text = game.date.toString()
        gamePaidCheckBox.apply {
            isChecked = game.isPaid
            jumpDrawablesToCurrentState()
        }
    }

    override fun onFragmentResult(requestKey: String, result: Bundle) {
        when(requestKey) {
            REQUEST_DATE -> {
                game.date = DatePickerFragment.getSelectedDate(result)
                updateUI()
            }
        }
    }

    companion object {
        fun newInstance(gameId: UUID): GameFragment {
            val args = Bundle()
                .apply {
                    putSerializable(ARG_GAME_ID, gameId)
                }
            return GameFragment().apply {
                arguments = args
            }
        }

        fun putGameId(gameId: UUID): Bundle {
            return Bundle().apply { putSerializable(ARG_GAME_ID, gameId) }
        }
    }

}