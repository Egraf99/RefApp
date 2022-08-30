package com.egraf.refapp.interface_viewmodel.add

import com.egraf.refapp.database.entities.Team

interface TeamAddInterface: AddInterface {
    fun addTeamToDB(team: Team)
}