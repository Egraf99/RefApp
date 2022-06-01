package com.egraf.refapp

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.egraf.refapp.database.GameDatabase
import com.egraf.refapp.database.entities.*
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

    private val executor = Executors.newSingleThreadExecutor()
    private val gameDao = database.gameDao()
    private val stadiumDao = database.stadiumDao()
    private val leagueDao = database.leagueDao()
    private val teamDao = database.teamDao()
    private val refereeDao = database.refereeDao()

//    game block
    fun getGames(): LiveData<List<GameWithAttributes>> = gameDao.getGames()
    fun countGames(): LiveData<Int> = gameDao.countGames()
    fun getGame(id: UUID): LiveData<GameWithAttributes?> = gameDao.getGame(id)
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
    fun deleteGame(game: Game) {
        executor.execute { gameDao.deleteGame(game) }
    }

//    stadium block
    fun addStadium(stadium: Stadium) {
        executor.execute {
            stadiumDao.addStadium(stadium)
        }
    }
    fun getStadiums(): LiveData<List<Stadium>> = stadiumDao.getStadiums()
    fun getStadium(id: UUID): LiveData<Stadium?> = stadiumDao.getStadium(id)
    fun updateStadium(stadium: Stadium) {
        executor.execute {
            stadiumDao.updateStadium(stadium)
        }
    }

    //    league block
    fun getLeagues(): LiveData<List<League>> = leagueDao.getLeagues()
    fun addLeague(league: League) {
        executor.execute {
            leagueDao.addLeague(league)
        }
    }
    fun updateLeague(league: League) {
        executor.execute {
            leagueDao.updateLeague(league)
        }
    }

//    team block
    fun getTeams(): LiveData<List<Team>> = teamDao.getTeams()
    fun addTeam(team: Team){
        executor.execute {
            teamDao.addTeam(team)
        }
    }

//    referee block
    fun getReferees(): LiveData<List<Referee>> = refereeDao.getReferees()
    fun addReferee(referee: Referee) {
        executor.execute { refereeDao.addReferee(referee) }
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