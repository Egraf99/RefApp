package com.egraf.refapp.ui.dialogs.add_new_game.date_choice

import androidx.lifecycle.viewModelScope
import com.egraf.refapp.database.entities.Stadium
import com.egraf.refapp.ui.ViewModelWithGameRepo
import com.egraf.refapp.utils.Resource
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class DateChooseViewModel : ViewModelWithGameRepo() {
    val addStadiumToDB: (Stadium) -> StateFlow<Resource<Unit>> =
        { stadium ->
            flow {
                emit(Resource.success(gameRepository.addStadium(stadium)))
            }.stateIn(
                scope = viewModelScope,
                started = WhileSubscribed(5000), // Or Lazily because it's a one-shot
                initialValue = Resource.loading(null)
            )
        }
}