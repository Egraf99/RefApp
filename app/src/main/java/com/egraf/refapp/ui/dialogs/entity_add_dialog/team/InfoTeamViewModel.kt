package com.egraf.refapp.ui.dialogs.entity_add_dialog.team

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.egraf.refapp.GameRepository
import com.egraf.refapp.database.local.entities.Team
import com.egraf.refapp.ui.dialogs.entity_info_dialog.InfoViewModel
import com.egraf.refapp.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*

class InfoTeamViewModel(
    private val teamId: UUID,
) : InfoViewModel() {
    var team: Team = Team()
    var deleteFunction: (Team) -> Unit = { GameRepository.get().deleteTeam(it) }
    private val _componentId = MutableStateFlow<Resource<Team>>(Resource.loading())
    val flowResourceTeam: StateFlow<Resource<Team>> = _componentId
    fun updateTeamName(name: String) = GameRepository.get().updateTeamName(teamId, name)
    fun deleteTeam() {
        deleteFunction(team)
    }

    // Load data from a suspend fun and mutate state
    init {
        viewModelScope.launch {
            GameRepository.get().getTeam(teamId).collect {
                _componentId.value = Resource.success(it ?: Team())
            }
        }
    }
}

class GameComponentViewModelFactory(
    private val componentId: UUID,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return InfoTeamViewModel(componentId) as T
    }
}