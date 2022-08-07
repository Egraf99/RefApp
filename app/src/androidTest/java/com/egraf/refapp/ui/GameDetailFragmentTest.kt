package com.egraf.refapp.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.SmallTest
import com.egraf.refapp.GameRepository
import com.egraf.refapp.R
import com.egraf.refapp.actions.DrawableAssertions.Companion.withEndIconType
import com.egraf.refapp.actions.TextInputLayoutActions
import com.egraf.refapp.database.source.FakeGameDataSource
import com.egraf.refapp.ui.game_details.GameDetailFragment
import com.egraf.refapp.views.textInput.ETIWithEndButton
import dagger.hilt.android.testing.HiltAndroidTest
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@ExperimentalCoroutinesApi
@SmallTest
@RunWith(JUnitParamsRunner::class)
class GameDetailFragmentTest {
    @Rule
    @JvmField
    var mInstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        GameRepository.initialize(FakeGameDataSource())
    }

    @Test
    fun writeDoNotExistHomeTeamInTeamETI_showAddEndIcon() {
        launchFragmentInContainer<GameDetailFragment>(themeResId = R.style.Theme_RefApp)
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
        launchFragmentInContainer<GameDetailFragment>(themeResId = R.style.Theme_RefApp)
        onView(withId(R.id.team_home_layout)).perform(
            click(),
            TextInputLayoutActions.replaceText("Команда")
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
        launchFragmentInContainer<GameDetailFragment>(themeResId = R.style.Theme_RefApp)
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
        launchFragmentInContainer<GameDetailFragment>(themeResId = R.style.Theme_RefApp)
        onView(withId(R.id.team_guest_layout)).perform(
            click(),
            TextInputLayoutActions.replaceText("Команда")
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
        launchFragmentInContainer<GameDetailFragment>(themeResId = R.style.Theme_RefApp)
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
        launchFragmentInContainer<GameDetailFragment>(themeResId = R.style.Theme_RefApp)
        onView(withId(R.id.stadium_layout)).perform(
            click(),
            TextInputLayoutActions.replaceText("Стадион")
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
        launchFragmentInContainer<GameDetailFragment>(themeResId = R.style.Theme_RefApp)
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
        launchFragmentInContainer<GameDetailFragment>(themeResId = R.style.Theme_RefApp)
        onView(withId(R.id.league_layout)).perform(
            click(),
            TextInputLayoutActions.replaceText("Лига")
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
        launchFragmentInContainer<GameDetailFragment>(themeResId = R.style.Theme_RefApp)
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
        launchFragmentInContainer<GameDetailFragment>(themeResId = R.style.Theme_RefApp)
        onView(withId(R.id.chief_referee_layout)).perform(
            click(),
            TextInputLayoutActions.replaceText("Фамилия Имя")
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
        launchFragmentInContainer<GameDetailFragment>(themeResId = R.style.Theme_RefApp)
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
        launchFragmentInContainer<GameDetailFragment>(themeResId = R.style.Theme_RefApp)
        onView(withId(R.id.first_referee_layout)).perform(
            click(),
            TextInputLayoutActions.replaceText("Фамилия Имя")
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
        launchFragmentInContainer<GameDetailFragment>(themeResId = R.style.Theme_RefApp)
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
        launchFragmentInContainer<GameDetailFragment>(themeResId = R.style.Theme_RefApp)
        onView(withId(R.id.second_referee_layout)).perform(
            click(),
            TextInputLayoutActions.replaceText("Фамилия Имя")
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
        launchFragmentInContainer<GameDetailFragment>(themeResId = R.style.Theme_RefApp)
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
        launchFragmentInContainer<GameDetailFragment>(themeResId = R.style.Theme_RefApp)
        onView(withId(R.id.reserve_referee_layout)).perform(
            click(),
            TextInputLayoutActions.replaceText("Фамилия Имя")
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
        launchFragmentInContainer<GameDetailFragment>(themeResId = R.style.Theme_RefApp)
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
    fun writeExistRefereeInInspectorETI_showInfoEndIcon() {
        launchFragmentInContainer<GameDetailFragment>(themeResId = R.style.Theme_RefApp)
        onView(withId(R.id.inspector_layout)).perform(
            click(),
            TextInputLayoutActions.replaceText("Фамилия Имя")
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