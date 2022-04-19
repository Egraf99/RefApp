package com.egraf.refapp

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.egraf.refapp.database.GameDatabase
import com.egraf.refapp.database.entities.Game
import com.egraf.refapp.database.entities.GameWithAttributes
import com.egraf.refapp.database.entities.League
import com.egraf.refapp.database.entities.Stadium
import com.egraf.refapp.database.migration_1_2
import com.egraf.refapp.database.migration_2_3
import java.lang.IllegalStateException
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "referee.sqlite"

class GameRepository private constructor(context: Context) {

    private val database: GameDatabase =
        Room.databaseBuilder(context.applicationContext, GameDatabase::class.java, DATABASE_NAME)
            .addMigrations(migration_1_2, migration_2_3)
            .build()

    private val gameDao = database.gameDao()
    private val stadiumDao = database.stadiumDao()
    private val leagueDao = database.leagueDao()
    private val executor = Executors.newSingleThreadExecutor()

    fun getGames(): LiveData<List<GameWithAttributes>> = gameDao.getGames()
    fun countGames(): LiveData<Int> = gameDao.countGames()
    fun getGame(id: UUID): LiveData<GameWithAttributes?> = gameDao.getGame(id)
    fun updateGame(game: Game) {
        executor.execute {
            gameDao.updateGame(game)
        }
    }

    fun addGameWithAttributes(gameWithAttributes: GameWithAttributes) {
        executor.execute {
            gameDao.addGame(gameWithAttributes.game)
            if (gameWithAttributes.stadium != null) {
                stadiumDao.addStadium(gameWithAttributes.stadium!!)
            }
            if (gameWithAttributes.league != null) {
                leagueDao.addLeague(gameWithAttributes.league!!)
            }
        }
    }

    fun addGame(game: Game) {
        executor.execute {
            gameDao.addGame(game)
        }
    }

    fun addStadium(stadium: Stadium) {
        executor.execute {
            stadiumDao.addStadium(stadium)
        }
    }

    fun addLeague(league: League) {
        executor.execute {
            leagueDao.addLeague(league)
        }
    }

    fun deleteGame(game: Game) {
        executor.execute { gameDao.deleteGame(game) }
    }

    fun getStadiums(): LiveData<List<Stadium>> = stadiumDao.getStadiums()
    fun getStadium(id: Long): LiveData<Stadium?> = stadiumDao.getStadium(id)
    fun updateStadium(stadium: Stadium?) {
        if (stadium != null) {
            executor.execute {
                stadiumDao.updateStadium(stadium)
            }
        }
    }

    fun updateLeague(league: League?) {
        if (league != null) {
            executor.execute {
                leagueDao.updateLeague(league)
            }
        }
    }

    companion object {
        private var INSTANCE: GameRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) INSTANCE = GameRepository(context)
        }

        fun get(): GameRepository {
            return INSTANCE ?: throw IllegalStateException("GameRepository must be initialized")
        }
    }
}