package com.egraf.refapp.dialogs.add_new_game

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.egraf.refapp.GameRepository
import com.egraf.refapp.R
import com.egraf.refapp.database.entities.Game
import com.egraf.refapp.database.entities.Stadium
import com.egraf.refapp.database.entities.Team
import com.egraf.refapp.dialogs.StadiumAddDialog
import com.egraf.refapp.dialogs.TeamAddDialog
import com.egraf.refapp.interface_viewmodel.StadiumAddInterface
import com.egraf.refapp.interface_viewmodel.TeamAddInterface

enum class AddGameDestination(val res: Int) {
    DATE_CHOOSE(0),
    TEAM_CHOOSE(R.id.action_choose_date_to_team),
    REFEREE_CHOSE(R.id.action_choose_team_to_referee),
    CLOSE(-1),
}

class AddNewGameViewModel: ViewModel(),
    TeamAddInterface, StadiumAddInterface {
    private val gameRepository = GameRepository.get()
    var currentPosition = 0
        private set
    val createdGame = Game()
    val destination: LiveData<AddGameDestination?> get() = _destination
    private val _destination = MutableLiveData<AddGameDestination?>(null)

    fun showNextFragment() {
        currentPosition += 1
        _destination.value = AddGameDestination.values()[currentPosition]
    }

    // game block
    fun addRandomGame() { gameRepository.addGame(Game()) }

    // team block
    override fun addTeam(team: Team) { gameRepository.addTeam(team) }
    override fun addTeam(bundle: Bundle) { gameRepository.addTeam( TeamAddDialog.getTeam(bundle) ) }
    override fun getTeams(): LiveData<List<Team>> { return gameRepository.getTeams() }
    override fun getTeamFromBundle(bundle: Bundle): Team { return TeamAddDialog.getTeam(bundle) }

    // stadium block
    override fun addStadium(stadium: Stadium) { gameRepository.addStadium(stadium) }
    override fun addStadium(bundle: Bundle) { gameRepository.addStadium( StadiumAddDialog.getStadium(bundle)) }
    override fun getStadiums(): LiveData<List<Stadium>> { return gameRepository.getStadiums() }
    override fun getStadiumFromBundle(bundle: Bundle): Stadium { return StadiumAddDialog.getStadium(bundle) }
}