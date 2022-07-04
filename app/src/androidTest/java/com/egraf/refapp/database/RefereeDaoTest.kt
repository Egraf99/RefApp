package com.egraf.refapp.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.egraf.refapp.database.dao.LeagueDao
import com.egraf.refapp.database.dao.RefereeDao
import com.egraf.refapp.database.entities.Game
import com.egraf.refapp.database.entities.League
import com.egraf.refapp.database.entities.Referee
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
class RefereeDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: GameDatabase
    private lateinit var dao: RefereeDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            GameDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.refereeDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun addReferee() = runTest {
        val referee = Referee()
        dao.addReferee(referee)

        val receiveLeague = dao.getReferee(referee.id).getOrAwaitValue()

        assertThat(receiveLeague).isEqualTo(referee)
    }

    @Test
    fun updateReferee() {
        val referee = Referee()
        dao.addReferee(referee)
        referee.firstName = "Test name"
        dao.updateReferee(referee)

        val receiveLeague = dao.getReferee(referee.id).getOrAwaitValue()

        assertThat(receiveLeague).isEqualTo(referee)
    }

    @Test
    fun getReferee() = runTest {
        val referee1 = Referee()
        val referee2 = Referee()
        dao.addReferee(referee1)
        dao.addReferee(referee2)

        val receiveListLeague = dao.getReferees().getOrAwaitValue()

        assertThat(receiveListLeague).contains(referee1)
        assertThat(receiveListLeague).contains(referee2)
    }
}