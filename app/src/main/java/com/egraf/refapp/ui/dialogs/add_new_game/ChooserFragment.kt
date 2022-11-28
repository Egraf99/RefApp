package com.egraf.refapp.ui.dialogs.add_new_game

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.egraf.refapp.database.entities.Game
import com.egraf.refapp.database.entities.GameWithAttributes

private const val ARG_GAME = "GameBundleKey"
private const val ARG_HOME_TEAM = "HomeTeamBundleKey"
private const val ARG_GUEST_TEAM = "GuestTeamBundleKey"
private const val ARG_STADIUM = "StadiumBundleKey"
private const val ARG_LEAGUE = "LeagueBundleKey"
private const val ARG_CHIEF_REF = "ChiefRefereeBundleKey"
private const val ARG_FIRST_REF = "FirstRefereeBundleKey"
private const val ARG_SECOND_REF = "SecondRefereeBundleKey"
private const val ARG_RESERVE_REF = "ReserveRefereeBundleKey"
private const val ARG_INSPECTOR = "InspectorBundleKey"

private const val TAG = "AddGame"
private const val BUNDLE_KEY = "BundleKey"

abstract class ChooserFragment : Fragment() {
    abstract fun updateUI()

    protected val addNewGameViewModel: AddNewGameViewModel by lazy {
        ViewModelProvider(this)[AddNewGameViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setPopBackObserver()
        setGameWithAttributes(arguments)
    }

    private fun setGameWithAttributes(bundle: Bundle?) {
        if (bundle == null) return
        val game = getGameWithAttributes(bundle)
        Log.d(TAG, "setGameWithAttributes: $game")
        addNewGameViewModel.setGameWithAttributes(game)
        updateUI()
    }

    private fun setPopBackObserver() {
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Bundle>(BUNDLE_KEY)
            ?.observe(viewLifecycleOwner) {
                it?.let {
                    addNewGameViewModel.setGameWithAttributes(getGameWithAttributes(it))
                    updateUI()
                }
            }
    }

    private fun getGameWithAttributes(bundle: Bundle): GameWithAttributes {
        return GameWithAttributes(
            game = bundle.getParcelable(ARG_GAME) ?: Game(),
            homeTeam = bundle.getParcelable(ARG_HOME_TEAM),
            guestTeam = bundle.getParcelable(ARG_GUEST_TEAM),
            stadium = bundle.getParcelable(ARG_STADIUM),
            league = bundle.getParcelable(ARG_LEAGUE),
            chiefReferee = bundle.getParcelable(ARG_CHIEF_REF),
            firstReferee = bundle.getParcelable(ARG_FIRST_REF),
            secondReferee = bundle.getParcelable(ARG_SECOND_REF),
            reserveReferee = bundle.getParcelable(ARG_RESERVE_REF),
            inspector = bundle.getParcelable(ARG_INSPECTOR),
        )
    }

    fun putGameWithAttributes(): Bundle {
        Log.d(TAG, "putGameWithAttributes: ${addNewGameViewModel.gameWithAttributes.game}")
        return Bundle().apply {
            putParcelable(ARG_GAME, addNewGameViewModel.gameWithAttributes.game)
            putParcelable(ARG_HOME_TEAM, addNewGameViewModel.gameWithAttributes.homeTeam)
            putParcelable(ARG_GUEST_TEAM, addNewGameViewModel.gameWithAttributes.guestTeam)
            putParcelable(ARG_STADIUM, addNewGameViewModel.gameWithAttributes.stadium)
            putParcelable(ARG_LEAGUE, addNewGameViewModel.gameWithAttributes.league)
            putParcelable(ARG_CHIEF_REF, addNewGameViewModel.gameWithAttributes.chiefReferee)
            putParcelable(ARG_FIRST_REF, addNewGameViewModel.gameWithAttributes.firstReferee)
            putParcelable(ARG_SECOND_REF, addNewGameViewModel.gameWithAttributes.secondReferee)
            putParcelable(ARG_RESERVE_REF, addNewGameViewModel.gameWithAttributes.reserveReferee)
            putParcelable(ARG_INSPECTOR, addNewGameViewModel.gameWithAttributes.inspector)
        }

    }

    fun putGameInBundle() {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(
            BUNDLE_KEY, putGameWithAttributes()
        )
    }

    fun addGameToDB() {
        addNewGameViewModel.addGameToDB()
    }
}
