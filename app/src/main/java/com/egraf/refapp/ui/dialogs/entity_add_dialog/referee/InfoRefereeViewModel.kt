package com.egraf.refapp.ui.dialogs.entity_add_dialog.referee

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.egraf.refapp.GameRepository
import com.egraf.refapp.database.local.entities.Referee
import com.egraf.refapp.ui.dialogs.entity_info_dialog.InfoViewModel
import com.egraf.refapp.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*

class InfoRefereeViewModel(
    private val refereeId: UUID,
) : InfoViewModel() {
    var referee: Referee = Referee()
    var deleteFunction: (Referee) -> Unit = { GameRepository.get().deleteReferee(it) }
    private val _componentId = MutableStateFlow<Resource<Referee>>(Resource.loading())
    val flowResourceReferee: StateFlow<Resource<Referee>> = _componentId
    fun deleteReferee() = deleteFunction(referee)

    fun updateRefereeFirstName(firstName: String) = GameRepository.get().updateRefereeFirstName(refereeId, firstName)
    fun updateRefereeMiddleName(middleName: String) = GameRepository.get().updateRefereeMiddleName(refereeId, middleName)
    fun updateRefereeLastName(lastName: String) = GameRepository.get().updateRefereeLastName(refereeId, lastName)

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