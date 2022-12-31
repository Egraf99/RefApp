package com.egraf.refapp.ui.dialogs.entity_add_dialog.stadium

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.egraf.refapp.GameRepository
import com.egraf.refapp.database.entities.Stadium
import com.egraf.refapp.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*

class GameComponentInfoViewModel(
    stadiumId: UUID,
) : GameComponentDialogViewModel() {
    private val _componentId = MutableStateFlow<Resource<Stadium>>(Resource.loading(null))
    val componentId: StateFlow<Resource<Stadium>> = _componentId

    // Load data from a suspend fun and mutate state
    init {
        viewModelScope.launch {
            GameRepository.get().getStadium(stadiumId).collect() {
                _componentId.value = Resource.success(it ?: Stadium())
            }
        }
    }
}

class GameComponentViewModelFactory(
    private val componentId: UUID,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GameComponentInfoViewModel(componentId) as T
    }
}