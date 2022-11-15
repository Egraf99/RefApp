package com.egraf.refapp.database.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.egraf.refapp.database.GameDatabase
import com.egraf.refapp.database.entities.Team
import com.egraf.refapp.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class TeamDaoTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: GameDatabase
    private lateinit var teamDao: TeamDao

    @Before
    fun setup() {
//        GameRepository.initialize(FakeGameDataSource())
        hiltRule.inject()
        teamDao = database.teamDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertTeam() = runTest {
        val team = Team(name="TestTeam")

        teamDao.addTeam(team)
        val teams = teamDao.getTeams().getOrAwaitValue()
        assertThat(teams).contains(team)
    }
}