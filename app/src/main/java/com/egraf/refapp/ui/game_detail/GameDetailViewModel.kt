package com.egraf.refapp.ui.game_detail

import androidx.lifecycle.viewModelScope
import com.egraf.refapp.GameRepository
import com.egraf.refapp.database.local.entities.*
import com.egraf.refapp.interface_viewmodel.all.LeagueInterface
import com.egraf.refapp.interface_viewmodel.all.RefereeInterface
import com.egraf.refapp.interface_viewmodel.all.StadiumInterface
import com.egraf.refapp.interface_viewmodel.all.TeamInterface
import com.egraf.refapp.ui.ViewModelWithGame
import com.egraf.refapp.utils.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import java.util.*

class GameDetailViewModel: ViewModelWithGame(),
TeamInterface, LeagueInterface, StadiumInterface, RefereeInterface {
    private val gameIdFlow = MutableStateFlow<UUID>(UUID.randomUUID())

    @ExperimentalCoroutinesApi
    val flowGameWithAttributes: StateFlow<Resource<GameWithAttributes?>> =
        gameIdFlow.transformLatest { id ->
            GameRepository.get().getGame(id).collect() {
                emit(Resource.success(it))
            }
        }.stateIn(viewModelScope, WhileSubscribed(5000), Resource.loading())

    fun loadGame(gameId: UUID) {
        gameIdFlow.value = gameId
    }

    fun deleteGame(game: Game) {
        gameRepository.deleteGame(game)
    }

    override fun addTeamToDB(team: Team) {
        gameRepository.addTeam(team)
    }

    override fun getTeamsFromDB(): List<Team> {
        return gameRepository.getTeams()
    }

    override fun addLeagueToDB(league: League) {
        gameRepository.addLeague(league)
    }

    override fun addStadiumToDB(stadium: Stadium) {
        gameRepository.addStadium(stadium)
    }

    override fun getLeagueFromDB(): List<League> {
        return gameRepository.getLeagues()
    }

    override fun getStadiumsFromDB(): List<Stadium> {
        return gameRepository.getStadiums()
    }

    override fun addRefereeToDB(referee: Referee) {
        gameRepository.addReferee(referee)
    }

    override fun getRefereeFromDB(): List<Referee> {
        return gameRepository.getReferees()
    }
}