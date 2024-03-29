package com.egraf.refapp.database.local.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.egraf.refapp.database.local.GameDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@SmallTest
@RunWith(AndroidJUnit4::class)
class TeamDaoTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: GameDatabase
    private lateinit var teamDao: TeamDao

    @Before
    fun setup() {
//        GameRepository.initialize(FakeGameDataSource())
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

//    @Test
//    fun insertTeam() = runTest {
//        val team = Team(name="TestTeam")
//
//        teamDao.addTeam(team)
//        val teams = teamDao.getTeams().getOrAwaitValue()
//        assertThat(teams).contains(team)
//    }
}