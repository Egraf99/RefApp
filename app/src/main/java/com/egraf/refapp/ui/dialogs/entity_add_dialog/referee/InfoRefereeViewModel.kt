package com.egraf.refapp.ui.dialogs.entity_add_dialog.referee

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.egraf.refapp.GameRepository
import com.egraf.refapp.database.entities.Referee
import com.egraf.refapp.database.entities.Stadium
import com.egraf.refapp.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*

class InfoRefereeViewModel(
    refereeId: UUID,
) : ViewModel() {
    var title: String = ""
    var referee: Referee = Referee()
    var deleteFunction: (Referee) -> Unit = {}
    private val _componentId = MutableStateFlow<Resource<Referee>>(Resource.loading(null))
    val flowResourceReferee: StateFlow<Resource<Referee>> = _componentId

    // Load data from a suspend fun and mutate state
    init {
        viewModelScope.launch {
            GameRepository.get().getReferee(refereeId).collect {
                _componentId.value = Resource.success(it ?: Referee())
            }
        }
    }
}

class GameComponentViewModelFactory(
    private val componentId: UUID,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return InfoRefereeViewModel(componentId) as T
    }
}