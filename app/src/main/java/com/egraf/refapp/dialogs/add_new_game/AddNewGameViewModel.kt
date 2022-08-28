package com.egraf.refapp.dialogs.add_new_game

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.egraf.refapp.GameRepository
import com.egraf.refapp.R
import com.egraf.refapp.database.entities.*
import com.egraf.refapp.dialogs.entity_add_dialog.StadiumAddDialog
import com.egraf.refapp.dialogs.entity_add_dialog.TeamAddDialog
import com.egraf.refapp.interface_viewmodel.add.StadiumAddInterface
import com.egraf.refapp.interface_viewmodel.add.TeamAddInterface
import com.egraf.refapp.interface_viewmodel.all.LeagueInterface
import com.egraf.refapp.interface_viewmodel.all.RefereeInterface
import com.egraf.refapp.interface_viewmodel.all.StadiumInterface
import com.egraf.refapp.interface_viewmodel.all.TeamInterface

enum class AddGameDestination(val res: Int) {
    DATE_CHOOSE(0),
    TEAM_CHOOSE(R.id.action_choose_date_to_team),
    REFEREE_CHOSE(R.id.action_choose_team_to_referee),
    CLOSE(-1),
}

class AddNewGameViewModel: ViewModel(),
    TeamInterface, StadiumInterface, LeagueInterface, RefereeInterface {
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
    override fun addTeamToDB(team: Team) { gameRepository.addTeam(team) }
    override fun getTeamsFromDB(): LiveData<List<Team>> { return gameRepository.getTeams() }

    // stadium block
    override fun addStadiumToDB(stadium: Stadium) { gameRepository.addStadium(stadium) }
    override fun getStadiumsFromDB(): LiveData<List<Stadium>> { return gameRepository.getStadiums() }

    // league block
    override fun addLeagueToDB(league: League) { gameRepository.addLeague(league) }
    override fun getLeagueFromDB(): LiveData<List<League>> { return gameRepository.getLeagues() }

    // referee block
    override fun addRefereeToDB(referee: Referee) { gameRepository.addReferee(referee) }
    override fun getRefereeFromDB(): LiveData<List<Referee>> { return gameRepository.getReferees() }
}