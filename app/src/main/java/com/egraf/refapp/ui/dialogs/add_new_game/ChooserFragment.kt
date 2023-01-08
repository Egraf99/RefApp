package com.egraf.refapp.ui.dialogs.add_new_game

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.egraf.refapp.R
import com.egraf.refapp.database.entities.*

private const val TAG = "AddGame"
private const val BUNDLE_KEY = "BundleKey"

abstract class ChooserFragment : Fragment() {
    protected var bundle: Bundle = Bundle()
    protected val addNewGameViewModel: AddNewGameViewModel by lazy {
        ViewModelProvider(this)[AddNewGameViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setPopBackObserver()
        bundle = when {
            savedInstanceState != null && arguments != null  -> updateBundle(
                savedInstanceState,
                requireArguments(),
            )
            savedInstanceState != null -> updateBundle(bundle, savedInstanceState)
            arguments != null -> updateBundle(bundle, requireArguments())
            else -> Bundle()
        }
        getGameComponentsFromSavedBundle(bundle)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (view != null) {
            bundle = putGameComponentsInSavedBundle(outState)
        }
    }

    abstract fun putGameComponentsInSavedBundle(bundle: Bundle): Bundle

    /** Передаются два Bundle для поиска нужного значения в одном из них.
     *  Если значение есть в обоих Bundle, значение берется из первого. **/
    abstract fun getGameComponentsFromSavedBundle(bundle: Bundle)

    abstract val nextPosition: Position
    abstract val previousPosition: Position
    abstract fun showNextFragment()
    abstract fun showPreviousFragment()

    private fun setPopBackObserver() {
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Bundle>(BUNDLE_KEY)
            ?.observe(viewLifecycleOwner) {
                bundle = updateBundle(this.bundle, it)
                getGameComponentsFromSavedBundle(bundle)
            }
    }

    fun putComponentsInArguments(): Bundle {
        bundle = putGameComponentsInSavedBundle(bundle)
        findNavController().previousBackStackEntry?.savedStateHandle?.set(
            BUNDLE_KEY, bundle
        )
        return bundle
    }

    fun addGameToDB() {
        addNewGameViewModel.addGameToDB()
    }

    /** Передаются два Bundle для поиска нужного значения в одном из них.
     *  Если значение есть в обоих Bundle, обновляемое значение берется из первого. **/
    private fun updateBundle(firstBundle: Bundle, secondBundle: Bundle = Bundle()): Bundle =
        Bundle().apply {
            putParcelable(
                STADIUM_VALUE,
                firstBundle.getParcelable<Stadium>(STADIUM_VALUE)
                    ?: secondBundle.getParcelable<Stadium>(STADIUM_VALUE)
            )
            putParcelable(
                DATE_VALUE,
                firstBundle.getParcelable<GameDate>(DATE_VALUE)
                    ?: secondBundle.getParcelable<GameDate>(DATE_VALUE)
            )
            putParcelable(
                TIME_VALUE,
                firstBundle.getParcelable<GameTime>(TIME_VALUE)
                    ?: secondBundle.getParcelable<GameTime>(TIME_VALUE)
            )
            val passed = when {
                bundle.containsKey(PASS_VALUE) -> bundle.getBoolean(PASS_VALUE)
                secondBundle.containsKey(PASS_VALUE) -> secondBundle.getBoolean(PASS_VALUE)
                else -> false
            }
            putBoolean(PASS_VALUE, passed)
            val pay = when {
                bundle.containsKey(PAY_VALUE) -> bundle.getBoolean(PAY_VALUE)
                secondBundle.containsKey(PAY_VALUE) -> secondBundle.getBoolean(PAY_VALUE)
                else -> false
            }
            putBoolean(PAY_VALUE, pay)
            putParcelable(
                HOME_TEAM_VALUE,
                firstBundle.getParcelable<Team>(HOME_TEAM_VALUE)
                    ?: secondBundle.getParcelable<Team>(HOME_TEAM_VALUE)
            )
            putParcelable(
                GUEST_TEAM_VALUE,
                firstBundle.getParcelable<Team>(GUEST_TEAM_VALUE)
                    ?: secondBundle.getParcelable<Team>(GUEST_TEAM_VALUE)
            )
            putParcelable(
                LEAGUE_VALUE,
                firstBundle.getParcelable<League>(LEAGUE_VALUE)
                    ?: secondBundle.getParcelable<League>(LEAGUE_VALUE)
            )
        }

    companion object {
        const val STADIUM_VALUE = "StadiumValue"
        const val DATE_VALUE = "DateValue"
        const val TIME_VALUE = "TimeValue"
        const val PAY_VALUE = "PayValue"
        const val PASS_VALUE = "PassValue"
        const val HOME_TEAM_VALUE = "HomeTeamValue"
        const val GUEST_TEAM_VALUE = "GuestTeamValue"
        const val LEAGUE_VALUE = "LeagueValue"
    }
}

enum class Position {
    DISMISS, FIRST, MIDDLE, LAST

}
