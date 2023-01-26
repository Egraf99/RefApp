package com.egraf.refapp.interface_viewmodel.add

import com.egraf.refapp.database.local.entities.Team

interface TeamAddInterface: AddInterface {
    fun addTeamToDB(team: Team)
}