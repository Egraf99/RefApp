package com.egraf.refapp.database.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.egraf.refapp.database.GameDatabase
import com.egraf.refapp.database.entities.Team
import com.egraf.refapp.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
@SmallTest
class TeamDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var database: GameDatabase
    private lateinit var teamDao: TeamDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            GameDatabase::class.java
        ).allowMainThreadQueries().build()
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