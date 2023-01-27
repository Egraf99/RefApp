package com.egraf.refapp.database.local.source

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.egraf.refapp.database.local.GameDatabase
import com.egraf.refapp.database.local.entities.*
import com.egraf.refapp.database.local.migration_1_2
import com.egraf.refapp.database.remote.Common
import com.egraf.refapp.database.remote.model.Weather
import com.egraf.refapp.database.remote.model.WeatherResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "referee.sqlite"

class LocalGameDataSource(context: Context) : GameDataSource {
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
    override fun getGames(): Flow<List<GameWithAttributes>> = gameDao.getGames()
    override fun countGames(): LiveData<Int> = gameDao.countGames()
    override fun getGame(id: UUID): LiveData<GameWithAttributes?> = gameDao.getGame(id)
    override fun updateGame(game: Game) {
        executor.execute {
            gameDao.updateGame(game)
        }
    }

    override fun addGame(game: Game) {
        executor.execute {
            gameDao.addGame(game)
        }
    }

    override fun deleteGame(game: Game) {
        executor.execute { gameDao.deleteGame(game) }
    }

    //    stadium block
    override fun addStadium(stadium: Stadium) {
        executor.execute {
            stadiumDao.addStadium(stadium)
        }
    }

    override fun getStadiums(): List<Stadium> = stadiumDao.getStadiums()
    override fun getStadium(id: UUID): Flow<Stadium?> = stadiumDao.getStadium(id)
    override fun updateStadium(stadium: Stadium) {
        executor.execute {
            stadiumDao.updateStadium(stadium)
        }
    }
    override fun deleteStadium(stadium: Stadium) {
        executor.execute { stadiumDao.deleteStadium(stadium) }
    }

    //    league block
    override fun getLeague(id: UUID): Flow<League?> = leagueDao.getLeague(id)
    override fun getLeagues(): List<League> = leagueDao.getLeagues()
    override fun addLeague(league: League) {
        executor.execute {
            leagueDao.addLeague(league)
        }
    }
    override fun deleteLeague(league: League) {
        executor.execute { leagueDao.deleteLeague(league) }
    }

    override fun updateLeague(league: League) {
        executor.execute {
            leagueDao.updateLeague(league)
        }
    }

    //    team block
    override fun getTeam(id: UUID): Flow<Team?> = teamDao.getTeam(id)
    override fun getTeams(): List<Team> = teamDao.getTeams()
    override fun deleteTeam(team: Team) {
        executor.execute { teamDao.deleteTeam(team) }
    }

    override fun addTeam(team: Team) {
        executor.execute {
            teamDao.addTeam(team)
        }
    }

    //    referee block
    override fun getReferee(id: UUID): Flow<Referee?> = refereeDao.getReferee(id)
    override fun getReferees(): List<Referee> = refereeDao.getReferees()
    override fun addReferee(referee: Referee) {
        executor.execute { refereeDao.addReferee(referee) }
    }
    override fun deleteReferee(referee: Referee) {
        executor.execute { refereeDao.deleteReferee(referee) }
    }

    // weather
    override fun getWeathersList(): Call<WeatherResponse> = Common.weatherService.getWeatherForecast()
}