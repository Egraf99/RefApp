package com.egraf.refapp.ui.game_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.egraf.refapp.database.entities.*
import com.egraf.refapp.interface_viewmodel.all.LeagueInterface
import com.egraf.refapp.interface_viewmodel.all.RefereeInterface
import com.egraf.refapp.interface_viewmodel.all.StadiumInterface
import com.egraf.refapp.interface_viewmodel.all.TeamInterface
import com.egraf.refapp.ui.ViewModelWithGame
import java.util.*

class GameDetailViewModel : ViewModelWithGame(),
TeamInterface, LeagueInterface, StadiumInterface, RefereeInterface {
    private val gameIdLiveData = MutableLiveData<UUID>()

    val gameLiveData: LiveData<GameWithAttributes?> =
        Transformations.switchMap(gameIdLiveData) { gameId ->
            gameRepository.getGame(gameId)
        }

    fun loadGame(gameId: UUID) {
        gameIdLiveData.value = gameId
    }

    fun deleteGame(game: Game) {
        gameRepository.deleteGame(game)
    }

    override fun addTeamToDB(team: Team) {
        gameRepository.addTeam(team)
    }

    override fun getTeamsFromDB(): LiveData<List<Team>> {
        return gameRepository.getTeams()
    }

    override fun addLeagueToDB(league: League) {
        gameRepository.addLeague(league)
    }

    override fun addStadiumToDB(stadium: Stadium) {
        gameRepository.addStadium(stadium)
    }

    override fun getLeagueFromDB(): LiveData<List<League>> {
        return gameRepository.getLeagues()
    }

    override fun getStadiumsFromDB(): LiveData<List<Stadium>> {
        return gameRepository.getStadiums()
    }

    override fun addRefereeToDB(referee: Referee) {
        gameRepository.addReferee(referee)
    }

    override fun getRefereeFromDB(): LiveData<List<Referee>> {
        return gameRepository.getReferees()
    }
}