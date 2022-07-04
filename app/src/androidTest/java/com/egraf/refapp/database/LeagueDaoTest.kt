package com.egraf.refapp.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.egraf.refapp.database.dao.LeagueDao
import com.egraf.refapp.database.entities.Game
import com.egraf.refapp.database.entities.League
import com.egraf.refapp.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class LeagueDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: GameDatabase
    private lateinit var dao: LeagueDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            GameDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.leagueDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun addLeague() = runTest {
        val league = League()
        dao.addLeague(league)

        val receiveLeague = dao.getLeague(league.id).getOrAwaitValue()

        assertThat(receiveLeague).isEqualTo(league)
    }

    @Test
    fun updateLeague() {
        val league = League()
        dao.addLeague(league)
        league.name = "Test name"
        dao.updateLeague(league)

        val receiveLeague = dao.getLeague(league.id).getOrAwaitValue()

        assertThat(receiveLeague).isEqualTo(league)
    }

    @Test
    fun getLeagues() = runTest {
        val league1 = League()
        val league2 = League()
        dao.addLeague(league1)
        dao.addLeague(league2)

        val receiveListLeague = dao.getLeagues().getOrAwaitValue()

        assertThat(receiveListLeague).contains(league1)
        assertThat(receiveListLeague).contains(league2)
    }
}