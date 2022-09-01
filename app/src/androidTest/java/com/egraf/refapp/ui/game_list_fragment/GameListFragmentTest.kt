package com.egraf.refapp.ui.game_list_fragment

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import androidx.test.filters.SmallTest
import com.egraf.refapp.GameRepository
import com.egraf.refapp.R
import com.egraf.refapp.database.entities.Game
import com.egraf.refapp.database.source.FakeGameDataSource
import com.egraf.refapp.ui.game_list.GameListFragment
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class GameListFragmentTest {
    private lateinit var navController: NavController
    private lateinit var gameRepository: GameRepository

    @Before
    fun setup() {
        // init fake repository
        GameRepository.initialize(FakeGameDataSource())
        gameRepository = GameRepository.get()
    }
    @Test
    fun gamesListEmpty_showTextAddGame() {
        launchFragmentInContainer<GameListFragment>(themeResId = R.style.Theme_RefApp)
        onView(withId(R.id.empty_list_textview)).check(matches(isDisplayed()))
    }
    @Test
    fun gamesListNotEmpty_notShowTextAddGame() {
        gameRepository.addGame(Game())
        launchFragmentInContainer<GameListFragment>(themeResId = R.style.Theme_RefApp)
        onView(withId(R.id.empty_list_textview)).check(matches(not(isDisplayed())))
    }
}