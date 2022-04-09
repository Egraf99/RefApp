package com.egraf.refapp

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
import android.widget.TextView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GameFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GameFragment : Fragment() {
    private lateinit var game: Game
    private lateinit var homeTeamTextView: TextView
    private lateinit var guestTeamTextView: TextView
    private lateinit var stadiumEditText: EditText
    private lateinit var leagueEditText: EditText
    private lateinit var dateButton: Button
    private lateinit var gamePaidCheckBox: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        game = Game()
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_game, container, false)
        homeTeamTextView = view.findViewById(R.id.team_home_textview) as TextView
        guestTeamTextView = view.findViewById(R.id.team_guest_textview) as TextView
        stadiumEditText = view.findViewById(R.id.stadium_edittext) as EditText
        leagueEditText = view.findViewById(R.id.league_edittext) as EditText

        dateButton = view.findViewById(R.id.game_date) as Button
        dateButton.apply {
            this.text = game.date.toString()
            this.isEnabled = false
        }

        gamePaidCheckBox = view.findViewById(R.id.game_paid) as CheckBox

        return view

    }

    override fun onStart() {
        super.onStart()

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

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment GameFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GameFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}