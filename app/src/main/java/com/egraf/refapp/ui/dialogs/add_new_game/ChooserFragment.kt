package com.egraf.refapp.ui.dialogs.add_new_game

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.egraf.refapp.database.local.entities.*
import com.egraf.refapp.utils.*
import java.time.LocalDateTime

private const val TAG = "AddGame"
private const val BUNDLE_KEY = "BundleKey"

abstract class ChooserFragment : Fragment() {
    protected var bundle: Bundle = Bundle().putCurrentTime()
    protected val addNewGameViewModel: AddNewGameViewModel by lazy {
        ViewModelProvider(this)[AddNewGameViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setPopBackObserver()
        bundle = when {
            savedInstanceState != null && arguments != null -> updateBundle(
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
            bundle = putGameComponentsInSavedBundle(bundle)
        }
    }

    fun updateArguments(bundle: Bundle) {
        this.bundle = updateBundle(this.bundle, bundle.putCurrentTime())
        getGameComponentsFromSavedBundle(this.bundle)
    }

    protected open fun putGameComponentsInSavedBundle(bundle: Bundle): Bundle =
        bundle.putCurrentTime()

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

    protected fun getGameFromBundle(bundle: Bundle): Game = Game(
        stadiumId = bundle.getStadium()?.id,
        dateTime = GameDateTime(
            bundle.getDate() ?: GameDate(),
            bundle.getTime() ?: GameTime()
        ),
        isPaid = bundle.getPaid(),
        isPassed = bundle.getPassed(),
        homeTeamId = bundle.getHomeTeam()?.id,
        guestTeamId = bundle.getGuestTeam()?.id,
        leagueId = bundle.getLeague()?.id,
        chiefRefereeId = bundle.getChiefReferee()?.id,
        firstRefereeId = bundle.getFirstAssistant()?.id,
        secondRefereeId = bundle.getSecondAssistant()?.id,
        reserveRefereeId = bundle.getReserveReferee()?.id,
        inspectorId = bundle.getInspector()?.id,
    )

    protected fun addGameToDB(game: Game) {
        addNewGameViewModel.addGameToDB(game)
    }

    /** Передаются два Bundle для поиска нужного значения в одном из них.
     *  Если значение есть в обоих Bundle, обновляемое значение берется из того, который создан позже. **/
    private fun updateBundle(firstBundle: Bundle, secondBundle: Bundle = Bundle()): Bundle {
        val firstBundleCreateTime =
            firstBundle.getCreatedTime()
        val secondBundleCreateTime =
            secondBundle.getCreatedTime()
        val (laterBundle, earlierBundle) = when {
            firstBundleCreateTime > secondBundleCreateTime -> Pair(firstBundle, secondBundle)
            else -> Pair(secondBundle, firstBundle)
        }
        return Bundle().apply {
            putStadium(laterBundle.getStadium() ?: earlierBundle.getStadium())
            putDate(laterBundle.getDate() ?: earlierBundle.getDate())
            putTime(laterBundle.getTime() ?: earlierBundle.getTime())
            val passed = when {
                laterBundle.containPassKey() -> laterBundle.getPassed()
                earlierBundle.containPassKey() -> earlierBundle.getPassed()
                else -> false
            }
            putPassed(passed)
            val pay = when {
                laterBundle.containPaidKey() -> laterBundle.getPaid()
                earlierBundle.containPaidKey() -> earlierBundle.getPaid()
                else -> false
            }
            putPaid(pay)
            putHomeTeam(laterBundle.getHomeTeam() ?: earlierBundle.getHomeTeam())
            putGuestTeam(laterBundle.getGuestTeam() ?: earlierBundle.getGuestTeam())
            putLeague(laterBundle.getLeague() ?: earlierBundle.getLeague())
            putChiefReferee(laterBundle.getChiefReferee() ?: earlierBundle.getChiefReferee())
            putFirstAssistant(laterBundle.getFirstAssistant() ?: earlierBundle.getFirstAssistant())
            putSecondAssistant(
                laterBundle.getSecondAssistant() ?: earlierBundle.getSecondAssistant()
            )
            putReserveReferee(laterBundle.getReserveReferee() ?: earlierBundle.getReserveReferee())
            putInspector(laterBundle.getInspector() ?: earlierBundle.getInspector())
        }
    }
}

enum class Position {
    DISMISS, FIRST, MIDDLE, LAST

}
