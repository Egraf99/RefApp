package com.egraf.refapp.ui.dialogs.add_new_game.stadium_choose

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.egraf.refapp.database.local.entities.GameDate
import com.egraf.refapp.database.local.entities.GameTime
import com.egraf.refapp.database.local.entities.Stadium
import com.egraf.refapp.ui.ViewModelWithGameRepo
import com.egraf.refapp.utils.Resource
import com.egraf.refapp.views.custom_views.GameComponent
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import java.util.*

class StadiumChooseViewModel : ViewModelWithGameRepo() {
    init {
        Log.d("12345", "init viewModel")
    }
    var stadium = GameComponent(Stadium())
    var date = GameComponent(GameDate())
    var time = GameComponent(GameTime())
    var pass = false
    var paid = false
    val addStadiumToDB: (String) -> StateFlow<Resource<Pair<UUID, String>>> =
        { stadiumName ->
            val stadium = Stadium(stadiumName)
            flow {
                try {
                    gameRepository.addStadium(stadium)
                    emit(Resource.success(Pair(stadium.id, stadium.name)))
                } catch (e: Exception) {
                    emit(Resource.error(e))
                }
            }.stateIn(
                scope = viewModelScope,
                started = WhileSubscribed(5000), // Or Lazily because it's a one-shot
                initialValue = Resource.loading()
            )
        }
}
