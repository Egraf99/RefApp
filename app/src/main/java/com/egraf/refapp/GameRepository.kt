package com.egraf.refapp

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.egraf.refapp.database.GameDatabase
import java.lang.IllegalStateException
import java.util.*

private const val DATABASE_NAME = "referee.sqlite"

class GameRepository private constructor(context: Context) {

    private val database: GameDatabase =
        Room.databaseBuilder(context.applicationContext, GameDatabase::class.java, DATABASE_NAME)
            .build()

    private val gameDao = database.gameDao()

    fun getGames(): LiveData<List<Game>> = gameDao.getGames()
    fun getGame(id: UUID): LiveData<Game?> = gameDao.getGame(id)

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