package com.egraf.refapp.ui.game_detail

import androidx.lifecycle.ViewModel
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

class GameDetailViewModel: ViewModel()  {
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

    fun deleteGame() {
        GameRepository.get().deleteGame(gameIdFlow.value)
    }

    fun updateHomeTeam(teamId: UUID) = GameRepository.get().updateHomeTeamInGame(gameId=gameIdFlow.value, teamId=teamId)
    fun updateGuestTeam(teamId: UUID) = GameRepository.get().updateGuestTeamInGame(gameId=gameIdFlow.value, teamId=teamId)
}