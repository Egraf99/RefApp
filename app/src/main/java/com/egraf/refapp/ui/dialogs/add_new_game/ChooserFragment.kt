package com.egraf.refapp.ui.dialogs.add_new_game

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.egraf.refapp.database.local.entities.*
import java.time.LocalDateTime

private const val TAG = "AddGame"
private const val BUNDLE_KEY = "BundleKey"

private fun Bundle.putCurrentTime(): Bundle =
    this.apply { putSerializable(ChooserFragment.CREATED_TIME, LocalDateTime.now()) }

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
            bundle = putGameComponentsInSavedBundle(outState)
        }
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
        stadiumId = bundle.getParcelable<Stadium>(STADIUM_VALUE)?.id,
        dateTime = GameDateTime(
            bundle.getParcelable(DATE_VALUE) ?: GameDate(),
            bundle.getParcelable(TIME_VALUE) ?: GameTime()
        ),
        isPaid = bundle.getBoolean(PAY_VALUE),
        isPassed = bundle.getBoolean(PASS_VALUE),
        homeTeamId = bundle.getParcelable<Team>(HOME_TEAM_VALUE)?.id,
        guestTeamId = bundle.getParcelable<Team>(GUEST_TEAM_VALUE)?.id,
        leagueId = bundle.getParcelable<League>(LEAGUE_VALUE)?.id,
        chiefRefereeId = bundle.getParcelable<Referee>(CHIEF_REFEREE_VALUE)?.id,
        firstRefereeId = bundle.getParcelable<Referee>(FIRST_ASSISTANT_VALUE)?.id,
        secondRefereeId = bundle.getParcelable<Referee>(SECOND_ASSISTANT_VALUE)?.id,
        reserveRefereeId = bundle.getParcelable<Referee>(RESERVE_REFEREE_VALUE)?.id,
        inspectorId = bundle.getParcelable<Referee>(INSPECTOR_VALUE)?.id,
    )

    protected fun addGameToDB(game: Game) {
        addNewGameViewModel.addGameToDB(game)
    }

    /** Передаются два Bundle для поиска нужного значения в одном из них.
     *  Если значение есть в обоих Bundle, обновляемое значение берется из того, который создан позже. **/
    private fun updateBundle(firstBundle: Bundle, secondBundle: Bundle = Bundle()): Bundle {
        val firstBundleCreateTime =
            firstBundle.getSerializable(CREATED_TIME) as LocalDateTime? ?: LocalDateTime.MIN
        val secondBundleCreateTime =
            secondBundle.getSerializable(CREATED_TIME) as LocalDateTime? ?: LocalDateTime.MIN
        val (laterBundle, earlierBundle) = when {
            firstBundleCreateTime > secondBundleCreateTime -> Pair(firstBundle, secondBundle)
            else -> Pair(secondBundle, firstBundle)
        }
        return Bundle().apply {
            putParcelable(
                STADIUM_VALUE,
                laterBundle.getParcelable<Stadium>(STADIUM_VALUE)
                    ?: earlierBundle.getParcelable<Stadium>(STADIUM_VALUE)
            )
            putParcelable(
                DATE_VALUE,
                laterBundle.getParcelable<GameDate>(DATE_VALUE)
                    ?: earlierBundle.getParcelable<GameDate>(DATE_VALUE)
            )
            putParcelable(
                TIME_VALUE,
                laterBundle.getParcelable<GameTime>(TIME_VALUE)
                    ?: earlierBundle.getParcelable<GameTime>(TIME_VALUE)
            )
            val passed = when {
                laterBundle.containsKey(PASS_VALUE) -> laterBundle.getBoolean(PASS_VALUE)
                earlierBundle.containsKey(PASS_VALUE) -> earlierBundle.getBoolean(PASS_VALUE)
                else -> false
            }
            putBoolean(PASS_VALUE, passed)
            val pay = when {
                laterBundle.containsKey(PAY_VALUE) -> laterBundle.getBoolean(PAY_VALUE)
                earlierBundle.containsKey(PAY_VALUE) -> earlierBundle.getBoolean(PAY_VALUE)
                else -> false
            }
            putBoolean(PAY_VALUE, pay)
            putParcelable(
                HOME_TEAM_VALUE,
                laterBundle.getParcelable<Team>(HOME_TEAM_VALUE)
                    ?: earlierBundle.getParcelable<Team>(HOME_TEAM_VALUE)
            )
            putParcelable(
                GUEST_TEAM_VALUE,
                laterBundle.getParcelable<Team>(GUEST_TEAM_VALUE)
                    ?: earlierBundle.getParcelable<Team>(GUEST_TEAM_VALUE)
            )
            putParcelable(
                LEAGUE_VALUE,
                laterBundle.getParcelable<League>(LEAGUE_VALUE)
                    ?: earlierBundle.getParcelable<League>(LEAGUE_VALUE)
            )
            putParcelable(
                CHIEF_REFEREE_VALUE,
                laterBundle.getParcelable<Referee>(CHIEF_REFEREE_VALUE)
                    ?: earlierBundle.getParcelable<Referee>(CHIEF_REFEREE_VALUE)
            )
            putParcelable(
                FIRST_ASSISTANT_VALUE,
                laterBundle.getParcelable<Referee>(FIRST_ASSISTANT_VALUE)
                    ?: earlierBundle.getParcelable<Referee>(FIRST_ASSISTANT_VALUE)
            )
            putParcelable(
                SECOND_ASSISTANT_VALUE,
                laterBundle.getParcelable<Referee>(SECOND_ASSISTANT_VALUE)
                    ?: earlierBundle.getParcelable<Referee>(SECOND_ASSISTANT_VALUE)
            )
            putParcelable(
                RESERVE_REFEREE_VALUE,
                laterBundle.getParcelable<Referee>(RESERVE_REFEREE_VALUE)
                    ?: earlierBundle.getParcelable<Referee>(RESERVE_REFEREE_VALUE)
            )
            putParcelable(
                INSPECTOR_VALUE,
                laterBundle.getParcelable<Referee>(INSPECTOR_VALUE)
                    ?: earlierBundle.getParcelable<Referee>(INSPECTOR_VALUE)
            )
        }
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
        const val CHIEF_REFEREE_VALUE = "ChiefRefereeValue"
        const val FIRST_ASSISTANT_VALUE = "FirstAssistantValue"
        const val SECOND_ASSISTANT_VALUE = "SecondAssistantValue"
        const val RESERVE_REFEREE_VALUE = "ReserveRefereeValue"
        const val INSPECTOR_VALUE = "InspectorValue"

        const val CREATED_TIME = "BundleCreatedTime"
    }
}

enum class Position {
    DISMISS, FIRST, MIDDLE, LAST

}
