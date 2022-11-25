package com.egraf.refapp.ui.game_list

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.egraf.refapp.GameRepository
import com.egraf.refapp.R
import com.egraf.refapp.database.source.FakeGameDataSource
import com.egraf.refapp.launchFragmentInHiltContainer
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class GameListFragmentTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        GameRepository.initialize(FakeGameDataSource(initFakeData = true))
        hiltRule.inject()
    }

    @Test
    fun clickAddButton_ShouldNavigateToAddGameBottomFragment() {
        val navController = mock(NavController::class.java)
        launchFragmentInHiltContainer<GameListFragment> {
           Navigation.setViewNavController(requireView(), navController)
        }

        onView(withId(R.id.add_new_game_button)).perform(click())

        verify(navController).navigate(
            R.id.action_gameListFragment_to_addNewGame
        )
    }

    @Test
    fun clickOnItemInRecycle_shouldNavigateToGameFragment() {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        launchFragmentInHiltContainer<GameListFragment> {
            navController.setGraph(R.navigation.route)
            Navigation.setViewNavController(requireView(), navController)
        }

        onView(withId(R.id.game_recycle_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<GameListFragment.GameHolder>(
                0,
                click()
            )
        )

//        verify(navController).navigate(
//            R.id.action_gameListFragment_to_gameFragment
//        )
        assertThat(navController.currentDestination?.id).isEqualTo(R.id.gameFragment)
    }
}