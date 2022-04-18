package com.egraf.refapp

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.egraf.refapp.database.GameDatabase
import com.egraf.refapp.database.entities.Game
import com.egraf.refapp.database.entities.GameWithStadium
import com.egraf.refapp.database.entities.Stadium
import com.egraf.refapp.database.migration_1_2
import java.lang.IllegalStateException
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "referee.sqlite"

class GameRepository private constructor(context: Context) {

    private val database: GameDatabase =
        Room.databaseBuilder(context.applicationContext, GameDatabase::class.java, DATABASE_NAME)
            .addMigrations(migration_1_2)
            .build()

    private val gameDao = database.gameDao()
    private val stadiumDao = database.stadiumDao()
    private val executor = Executors.newSingleThreadExecutor()

    fun getGames(): LiveData<List<GameWithStadium>> = gameDao.getGames()
    fun countGames(): LiveData<Int> = gameDao.countGames()
    fun getGame(id: UUID): LiveData<GameWithStadium?> = gameDao.getGame(id)
    fun updateGame(game: Game) {
        executor.execute {
            gameDao.updateGame(game)
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

    fun deleteGame(game: Game) {
        executor.execute { gameDao.deleteGame(game) }
    }

    fun getStadiums(): LiveData<List<Stadium>> = stadiumDao.getStadiums()
    fun getStadium(id: Long): LiveData<Stadium?> = stadiumDao.getStadium(id)
    fun updateStadium(stadium: Stadium) {
        executor.execute{
            stadiumDao.updateStadium(stadium)
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