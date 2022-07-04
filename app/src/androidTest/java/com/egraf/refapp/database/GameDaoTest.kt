package com.egraf.refapp.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.egraf.refapp.database.dao.GameDao
import com.egraf.refapp.database.entities.Game
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
import org.junit.runner.RunWith
import java.util.*
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class GameDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: GameDatabase
    private lateinit var dao: GameDao

    @Before
    fun setUp() {
        hiltRule.inject()
        dao = database.gameDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun addGame() = runTest {
        val game = Game()
        dao.addGame(game)

        val receiveGame = dao.getGame(game.id).getOrAwaitValue()?.game

        assertThat(receiveGame).isEqualTo(game)
    }

    @Test
    fun getGames() {
        val game1 = Game()
        val game2 = Game()
        dao.addGame(game1)
        dao.addGame(game2)

        val receiveGameList = dao.getGames().getOrAwaitValue().map { it.game }

        assertThat(receiveGameList).contains(game1)
        assertThat(receiveGameList).contains(game2)
    }

    @Test
    fun countGames() {
        val game = Game()
        val game2 = Game()
        dao.addGame(game)
        dao.addGame(game2)

        val receiveCount = dao.countGames().getOrAwaitValue()

        assertThat(receiveCount).isEqualTo(2)
    }

    @Test
    fun updateGame() {
        val game = Game()
        dao.addGame(game)
        game.stadiumId = UUID.randomUUID()
        dao.updateGame(game)

        val receiveGame = dao.getGame(game.id).getOrAwaitValue()?.game

        assertThat(receiveGame).isEqualTo(game)
    }

    @Test
    fun deleteGame() {
        val game = Game()
        dao.addGame(game)
        dao.deleteGame(game)

        val receiveGame = dao.getGame(game.id).getOrAwaitValue()?.game

        assertThat(receiveGame).isNull()
    }
}