package com.egraf.refapp.ui.dialogs.add_new_game.team_choose

import androidx.lifecycle.viewModelScope
import com.egraf.refapp.database.entities.League
import com.egraf.refapp.database.entities.Team
import com.egraf.refapp.ui.ViewModelWithGameRepo
import com.egraf.refapp.utils.Resource
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import java.util.*

class TeamChooseViewModel : ViewModelWithGameRepo() {
    val addTeamToDB: (String) -> StateFlow<Resource<Pair<UUID, String>>> =
        { teamName ->
            val team = Team(name = teamName)
            flow {
                try {
                    gameRepository.addTeam(team)
                    emit(Resource.success(Pair(team.id, team.name)))
                } catch (e: Exception) {
                    emit(Resource.error(null, e.message.toString()))
                }
            }.stateIn(
                scope = viewModelScope,
                started = WhileSubscribed(5000), // Or Lazily because it's a one-shot
                initialValue = Resource.loading(null)
            )
        }

    val addLeagueToDB: (String) -> StateFlow<Resource<Pair<UUID, String>>> =
        { teamName ->
            val league = League(name = teamName)
            flow {
                try {
                    gameRepository.addLeague(league)
                    emit(Resource.success(Pair(league.id, league.name)))
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
