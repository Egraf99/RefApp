package com.egraf.refapp.ui

import android.content.Context
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import com.egraf.refapp.GameRepository
import com.egraf.refapp.R
import com.egraf.refapp.database.source.FakeGameDataSource
import com.egraf.refapp.ui.game_list.GameListFragment
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
@MediumTest
@HiltAndroidTest
class GameListFragmentTest {

    @Before
    fun setup() {
        GameRepository.initialize(FakeGameDataSource())
    }

    @Test
    fun addButtonClick_navigateToGameFragment() {
        // Create` a TestNavHostController
        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()
        )

        val gameListScenario =
            launchFragmentInContainer<GameListFragment>(themeResId = R.style.Theme_RefApp)

        gameListScenario.onFragment { fragment ->
            // Set the graph on the TestNavHostController
            navController.setGraph(R.navigation.route)

            // Make the NavController available via the findNavController() APIs
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        onView(withId(R.id.new_game_floating_button)).perform(click())
        assertThat(navController.currentDestination?.id).isEqualTo(R.id.gameFragment)
    }
}