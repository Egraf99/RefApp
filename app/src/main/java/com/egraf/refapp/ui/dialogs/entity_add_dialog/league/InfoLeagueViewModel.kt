package com.egraf.refapp.ui.dialogs.entity_add_dialog.league

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.egraf.refapp.GameRepository
import com.egraf.refapp.database.local.entities.League
import com.egraf.refapp.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*

class InfoLeagueViewModel(
    leagueId: UUID,
) : ViewModel() {
    var title: String = ""
    var league: League = League()
    var deleteFunction: (League) -> Unit = { GameRepository.get().deleteLeague(it) }
    private val _componentId = MutableStateFlow<Resource<League>>(Resource.loading())
    val flowResourceLeague: StateFlow<Resource<League>> = _componentId

    // Load data from a suspend fun and mutate state
    init {
        viewModelScope.launch {
            GameRepository.get().getLeague(leagueId).collect {
                _componentId.value = Resource.success(it ?: League())
            }
        }
    }
}

class GameComponentViewModelFactory(
    private val componentId: UUID,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return InfoLeagueViewModel(componentId) as T
    }
}