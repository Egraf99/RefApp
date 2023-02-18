package com.egraf.refapp.navigation

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.egraf.refapp.GameRepository
import com.egraf.refapp.R
import com.egraf.refapp.database.local.source.FakeGameDataSource
import com.egraf.refapp.ui.game_list.GameListFragment
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class NavigateTest {
    private lateinit var navController: NavController

    @Before
    fun setUp() {

        // Create` a TestNavHostController
        navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()
        )
        val gameListScenario =
            launchFragmentInContainer<GameListFragment>(themeResId = R.style.Theme_RefApp)

        gameListScenario.onFragment { fragment ->
            GameRepository.initialize(FakeGameDataSource())
            // Set the graph on the TestNavHostController
            navController.setGraph(R.navigation.route)

            // Make the NavController available via the findNavController() APIs
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

    }

    @Test
    fun addButtonClick_navigateToGameFragment() {
        onView(withId(R.id.add_new_game_button)).perform(click())
        Truth.assertThat(navController.currentDestination?.id)
            .isEqualTo(R.id.addNewGameBottomDialog)
    }

}
