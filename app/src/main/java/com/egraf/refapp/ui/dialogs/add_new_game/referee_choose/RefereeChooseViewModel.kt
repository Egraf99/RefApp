package com.egraf.refapp.ui.dialogs.add_new_game.referee_choose

import androidx.lifecycle.viewModelScope
import com.egraf.refapp.database.local.entities.Referee
import com.egraf.refapp.ui.ViewModelWithGameRepo
import com.egraf.refapp.utils.Resource
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import java.util.*

class RefereeChooseViewModel : ViewModelWithGameRepo() {
    val addRefereeToDB: (Referee) -> StateFlow<Resource<Pair<UUID, String>>> =
        { referee ->
            flow {
                try {
                    gameRepository.addReferee(referee)
                    emit(Resource.success(Pair(referee.id, referee.shortName)))
                } catch (e: Exception) {
                    emit(Resource.error(null, e.message.toString()))
                }
            }.stateIn(
                scope = viewModelScope,
                started = WhileSubscribed(5000), // Or Lazily because it's a one-shot
                initialValue = Resource.loading(null)
            )
        }
}
