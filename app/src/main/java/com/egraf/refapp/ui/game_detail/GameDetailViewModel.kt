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

    fun updateHomeTeam(teamId: UUID) =
        GameRepository.get().updateHomeTeamInGame(gameId = gameIdFlow.value, teamId = teamId)

    fun updateGuestTeam(teamId: UUID) =
        GameRepository.get().updateGuestTeamInGame(gameId = gameIdFlow.value, teamId = teamId)

    fun updateStadium(stadiumId: UUID) =
        GameRepository.get().updateStadiumInGame(gameId = gameIdFlow.value, stadiumId = stadiumId)

    fun updateLeague(leagueId: UUID) =
        GameRepository.get().updateLeagueInGame(gameId = gameIdFlow.value, leagueId = leagueId)

    fun updateChiefReferee(refereeId: UUID) = GameRepository.get()
        .updateChiefRefereeInGame(gameId = gameIdFlow.value, refereeId = refereeId)

    fun updateFirstAssistant(refereeId: UUID) = GameRepository.get()
        .updateFirstAssistantInGame(gameId = gameIdFlow.value, refereeId = refereeId)

    fun updateSecondAssistant(refereeId: UUID) = GameRepository.get()
        .updateSecondAssistantInGame(gameId = gameIdFlow.value, refereeId = refereeId)

    fun updateReserveReferee(refereeId: UUID) = GameRepository.get()
        .updateReserveRefereeInGame(gameId = gameIdFlow.value, refereeId = refereeId)

    fun updateInspector(refereeId: UUID) =
        GameRepository.get().updateInspectorInGame(gameId = gameIdFlow.value, refereeId = refereeId)

    fun updateDateTime(gdt: GameDateTime) =
        GameRepository.get().updateDateTimeInGame(gameId = gameIdFlow.value, dt = gdt)

    fun updatePassedGame(isPassed: Boolean) =
        GameRepository.get().updatePassedGame(gameId = gameIdFlow.value, isPassed = isPassed)

    fun updatePaidGame(isPaid: Boolean) =
        GameRepository.get().updatePaidGame(gameId = gameIdFlow.value, isPaid = isPaid)
}