package com.egraf.refapp.ui

import android.content.Context
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.internal.inject.InstrumentationContext
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import com.egraf.refapp.GameRepository
import com.egraf.refapp.ui.game_details.GameDetailFragment
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Test
import com.egraf.refapp.R
import com.egraf.refapp.actions.DrawableAssertions.Companion.withEndIconType
import com.egraf.refapp.actions.TextInputLayoutActions
import com.egraf.refapp.database.entities.Game
import com.egraf.refapp.database.entities.Team
import com.egraf.refapp.ui.game_details.GameDetailViewModel
import com.egraf.refapp.views.textInput.ETIWithEndButton
import io.mockk.every
import io.mockk.mockk

@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class GameDetailFragmentTest {
    private lateinit var instrumentationContext: Context
    private lateinit var gameDetailViewModel: GameDetailViewModel

    @Before
    fun setUp() {
        instrumentationContext = InstrumentationRegistry.getInstrumentation().targetContext
        GameRepository.initialize(instrumentationContext)
    }

    @Test
    fun writeDoNotExistHomeTeamInTeamETI_showAddEndIcon() {
        val bundle = GameDetailFragment.putGameId(Game().id)
        launchFragmentInContainer<GameDetailFragment>(
            themeResId = R.style.Theme_RefApp,
            fragmentArgs = bundle
        )
        onView(withId(R.id.team_home_layout)).perform(
            click(),
            TextInputLayoutActions.replaceText("НесущКоманда")
        )
        onView(withId(R.id.team_home_layout)).check(
            matches(
                withEndIconType(
                    ETIWithEndButton.TextEditIconType.ADD
                )
            )
        )
    }

    @Test
    fun writeExistHomeTeamInTeamETI_showInfoEndIcon() {
        val bundle = GameDetailFragment.putGameId(Game().id)
        launchFragmentInContainer<GameDetailFragment>(
            themeResId = R.style.Theme_RefApp,
            fragmentArgs = bundle
        )
        onView(withId(R.id.team_home_layout)).perform(
            click(),
            TextInputLayoutActions.replaceText("Луч")
        )
        onView(withId(R.id.team_home_layout)).check(
            matches(
                withEndIconType(
                    ETIWithEndButton.TextEditIconType.INFO
                )
            )
        )
    }

    @Test
    fun writeDoNotExistGuestTeamInTeamETI_showAddEndIcon() {
        val bundle = GameDetailFragment.putGameId(Game().id)
        launchFragmentInContainer<GameDetailFragment>(
            themeResId = R.style.Theme_RefApp,
            fragmentArgs = bundle
        )
        onView(withId(R.id.team_guest_layout)).perform(
            click(),
            TextInputLayoutActions.replaceText("НесущКоманда")
        )
        onView(withId(R.id.team_guest_layout)).check(
            matches(
                withEndIconType(
                    ETIWithEndButton.TextEditIconType.ADD
                )
            )
        )
    }

    @Test
    fun writeExistGuestTeamInTeamETI_showInfoEndIcon() {
        val bundle = GameDetailFragment.putGameId(Game().id)
        launchFragmentInContainer<GameDetailFragment>(
            themeResId = R.style.Theme_RefApp,
            fragmentArgs = bundle
        )
        onView(withId(R.id.team_guest_layout)).perform(
            click(),
            TextInputLayoutActions.replaceText("Луч")
        )
        onView(withId(R.id.team_guest_layout)).check(
            matches(
                withEndIconType(
                    ETIWithEndButton.TextEditIconType.INFO
                )
            )
        )
    }

    @Test
    fun writeDoNotExistStadiumInStadiumETI_showAddEndIcon() {
        val bundle = GameDetailFragment.putGameId(Game().id)
        launchFragmentInContainer<GameDetailFragment>(
            themeResId = R.style.Theme_RefApp,
            fragmentArgs = bundle
        )
        onView(withId(R.id.stadium_layout)).perform(
            click(),
            TextInputLayoutActions.replaceText("НесущСтадион")
        )
        onView(withId(R.id.stadium_layout)).check(
            matches(
                withEndIconType(
                    ETIWithEndButton.TextEditIconType.ADD
                )
            )
        )
    }

    @Test
    fun writeExistStadiumTeamInStadiumETI_showInfoEndIcon() {
        val bundle = GameDetailFragment.putGameId(Game().id)
        launchFragmentInContainer<GameDetailFragment>(
            themeResId = R.style.Theme_RefApp,
            fragmentArgs = bundle
        )
        onView(withId(R.id.stadium_layout)).perform(
            click(),
            TextInputLayoutActions.replaceText("Лужники")
        )
        onView(withId(R.id.stadium_layout)).check(
            matches(
                withEndIconType(
                    ETIWithEndButton.TextEditIconType.INFO
                )
            )
        )
    }

    @Test
    fun writeDoNotExistLeagueInLeagueETI_showAddEndIcon() {
        val bundle = GameDetailFragment.putGameId(Game().id)
        launchFragmentInContainer<GameDetailFragment>(
            themeResId = R.style.Theme_RefApp,
            fragmentArgs = bundle
        )
        onView(withId(R.id.league_layout)).perform(
            click(),
            TextInputLayoutActions.replaceText("НесущЛига")
        )
        onView(withId(R.id.league_layout)).check(
            matches(
                withEndIconType(
                    ETIWithEndButton.TextEditIconType.ADD
                )
            )
        )
    }

    @Test
    fun writeExistLeagueInLeagueETI_showInfoEndIcon() {
        val bundle = GameDetailFragment.putGameId(Game().id)
        launchFragmentInContainer<GameDetailFragment>(
            themeResId = R.style.Theme_RefApp,
            fragmentArgs = bundle
        )
        onView(withId(R.id.league_layout)).perform(
            click(),
            TextInputLayoutActions.replaceText("МПР")
        )
        onView(withId(R.id.league_layout)).check(
            matches(
                withEndIconType(
                    ETIWithEndButton.TextEditIconType.INFO
                )
            )
        )
    }

    @Test
    fun writeDoNotExistRefereeInChiefRefereeETI_showAddEndIcon() {
        val bundle = GameDetailFragment.putGameId(Game().id)
        launchFragmentInContainer<GameDetailFragment>(
            themeResId = R.style.Theme_RefApp,
            fragmentArgs = bundle
        )
        onView(withId(R.id.chief_referee_layout)).perform(
            click(),
            TextInputLayoutActions.replaceText("НесущСудья")
        )
        onView(withId(R.id.chief_referee_layout)).check(
            matches(
                withEndIconType(
                    ETIWithEndButton.TextEditIconType.ADD
                )
            )
        )
    }

    @Test
    fun writeExistRefereeInChiefRefereeETI_showInfoEndIcon() {
        val bundle = GameDetailFragment.putGameId(Game().id)
        launchFragmentInContainer<GameDetailFragment>(
            themeResId = R.style.Theme_RefApp,
            fragmentArgs = bundle
        )
        onView(withId(R.id.chief_referee_layout)).perform(
            click(),
            TextInputLayoutActions.replaceText("Ходин Егор")
        )
        onView(withId(R.id.chief_referee_layout)).check(
            matches(
                withEndIconType(
                    ETIWithEndButton.TextEditIconType.INFO
                )
            )
        )
    }

    @Test
    fun writeDoNotExistRefereeInFirstRefereeETI_showAddEndIcon() {
        val bundle = GameDetailFragment.putGameId(Game().id)
        launchFragmentInContainer<GameDetailFragment>(
            themeResId = R.style.Theme_RefApp,
            fragmentArgs = bundle
        )
        onView(withId(R.id.first_referee_layout)).perform(
            click(),
            TextInputLayoutActions.replaceText("НесущСудья")
        )
        onView(withId(R.id.first_referee_layout)).check(
            matches(
                withEndIconType(
                    ETIWithEndButton.TextEditIconType.ADD
                )
            )
        )
    }

    @Test
    fun writeExistRefereeInFirstRefereeETI_showInfoEndIcon() {
        val bundle = GameDetailFragment.putGameId(Game().id)
        launchFragmentInContainer<GameDetailFragment>(
            themeResId = R.style.Theme_RefApp,
            fragmentArgs = bundle
        )
        onView(withId(R.id.first_referee_layout)).perform(
            click(),
            TextInputLayoutActions.replaceText("Ходин Егор")
        )
        onView(withId(R.id.first_referee_layout)).check(
            matches(
                withEndIconType(
                    ETIWithEndButton.TextEditIconType.INFO
                )
            )
        )
    }

    @Test
    fun writeDoNotExistRefereeInSecondRefereeETI_showAddEndIcon() {
        val bundle = GameDetailFragment.putGameId(Game().id)
        launchFragmentInContainer<GameDetailFragment>(
            themeResId = R.style.Theme_RefApp,
            fragmentArgs = bundle
        )
        onView(withId(R.id.second_referee_layout)).perform(
            click(),
            TextInputLayoutActions.replaceText("НесущСудья")
        )
        onView(withId(R.id.second_referee_layout)).check(
            matches(
                withEndIconType(
                    ETIWithEndButton.TextEditIconType.ADD
                )
            )
        )
    }

    @Test
    fun writeExistRefereeInSecondRefereeETI_showInfoEndIcon() {
        val bundle = GameDetailFragment.putGameId(Game().id)
        launchFragmentInContainer<GameDetailFragment>(
            themeResId = R.style.Theme_RefApp,
            fragmentArgs = bundle
        )
        onView(withId(R.id.second_referee_layout)).perform(
            click(),
            TextInputLayoutActions.replaceText("Ходин Егор")
        )
        onView(withId(R.id.second_referee_layout)).check(
            matches(
                withEndIconType(
                    ETIWithEndButton.TextEditIconType.INFO
                )
            )
        )
    }

    @Test
    fun writeDoNotExistRefereeInReserveRefereeETI_showAddEndIcon() {
        val bundle = GameDetailFragment.putGameId(Game().id)
        launchFragmentInContainer<GameDetailFragment>(
            themeResId = R.style.Theme_RefApp,
            fragmentArgs = bundle
        )
        onView(withId(R.id.reserve_referee_layout)).perform(
            click(),
            TextInputLayoutActions.replaceText("НесущСудья")
        )
        onView(withId(R.id.reserve_referee_layout)).check(
            matches(
                withEndIconType(
                    ETIWithEndButton.TextEditIconType.ADD
                )
            )
        )
    }

    @Test
    fun writeExistRefereeInReserveRefereeETI_showInfoEndIcon() {
        val bundle = GameDetailFragment.putGameId(Game().id)
        launchFragmentInContainer<GameDetailFragment>(
            themeResId = R.style.Theme_RefApp,
            fragmentArgs = bundle
        )
        onView(withId(R.id.reserve_referee_layout)).perform(
            click(),
            TextInputLayoutActions.replaceText("Ходин Егор")
        )
        onView(withId(R.id.reserve_referee_layout)).check(
            matches(
                withEndIconType(
                    ETIWithEndButton.TextEditIconType.INFO
                )
            )
        )
    }

    @Test
    fun writeDoNotExistRefereeInInspectorETI_showAddEndIcon() {
        val bundle = GameDetailFragment.putGameId(Game().id)
        launchFragmentInContainer<GameDetailFragment>(
            themeResId = R.style.Theme_RefApp,
            fragmentArgs = bundle
        )
        onView(withId(R.id.inspector_layout)).perform(
            click(),
            TextInputLayoutActions.replaceText("НесущСудья")
        )
        onView(withId(R.id.inspector_layout)).check(
            matches(
                withEndIconType(
                    ETIWithEndButton.TextEditIconType.ADD
                )
            )
        )
    }

    @Test
    fun writeExistRefereeInInspectorTI_showInfoEndIcon() {
        val bundle = GameDetailFragment.putGameId(Game().id)
        launchFragmentInContainer<GameDetailFragment>(
            themeResId = R.style.Theme_RefApp,
            fragmentArgs = bundle
        )
        onView(withId(R.id.inspector_layout)).perform(
            click(),
            TextInputLayoutActions.replaceText("Ходин Егор")
        )
        onView(withId(R.id.inspector_layout)).check(
            matches(
                withEndIconType(
                    ETIWithEndButton.TextEditIconType.INFO
                )
            )
        )
    }
}