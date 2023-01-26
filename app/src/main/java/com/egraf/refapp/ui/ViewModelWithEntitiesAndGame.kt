package com.egraf.refapp.ui

import com.egraf.refapp.database.local.entities.League
import com.egraf.refapp.database.local.entities.Referee
import com.egraf.refapp.database.local.entities.Stadium
import com.egraf.refapp.database.local.entities.Team
import com.egraf.refapp.interface_viewmodel.all.LeagueInterface
import com.egraf.refapp.interface_viewmodel.all.RefereeInterface
import com.egraf.refapp.interface_viewmodel.all.StadiumInterface
import com.egraf.refapp.interface_viewmodel.all.TeamInterface

open class ViewModelWithEntitiesAndGame : ViewModelWithGame(),
    TeamInterface, StadiumInterface, LeagueInterface, RefereeInterface {
    // team block
    override fun addTeamToDB(team: Team) {
        gameRepository.addTeam(team)
    }

    override fun getTeamsFromDB(): List<Team> {
        return gameRepository.getTeams()
    }

    // stadium block
    override fun addStadiumToDB(stadium: Stadium) {
        gameRepository.addStadium(stadium)
    }

    override fun getStadiumsFromDB(): List<Stadium> {
        return gameRepository.getStadiums()
    }

    // league block
    override fun addLeagueToDB(league: League) {
        gameRepository.addLeague(league)
    }

    override fun getLeagueFromDB(): List<League> {
        return gameRepository.getLeagues()
    }

    // referee block
    override fun addRefereeToDB(referee: Referee) {
        gameRepository.addReferee(referee)
    }

    override fun getRefereeFromDB(): List<Referee> {
        return gameRepository.getReferees()
    }
}