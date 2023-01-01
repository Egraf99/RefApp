package com.egraf.refapp.ui.dialogs.add_new_game.date_choice

import androidx.lifecycle.viewModelScope
import com.egraf.refapp.GameRepository
import com.egraf.refapp.database.entities.Stadium
import com.egraf.refapp.ui.ViewModelWithGameRepo
import com.egraf.refapp.utils.Resource
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import java.util.*

class DateChooseViewModel : ViewModelWithGameRepo() {
    val addStadiumToDB: (String) -> StateFlow<Resource<Pair<UUID, String>>> =
        { stadiumName ->
            val stadium = Stadium(name = stadiumName)
            flow {
                try {
                    gameRepository.addStadium(stadium)
                    emit(Resource.success(Pair(stadium.id, stadium.name)))
                } catch (e: Exception) {
                    emit(Resource.error(null, e.message.toString()))
                }
            }.stateIn(
                scope = viewModelScope,
                started = WhileSubscribed(5000), // Or Lazily because it's a one-shot
                initialValue = Resource.loading(null)
            )
        }

    val getStadiumNameFromDB: (UUID) -> Flow<Stadium?> = { GameRepository.get().getStadium(it) }
}
