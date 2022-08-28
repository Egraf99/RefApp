package com.egraf.refapp.interface_viewmodel.add

import com.egraf.refapp.database.entities.League
import com.egraf.refapp.interface_viewmodel.add.AddInterface

interface LeagueAddInterface: AddInterface {
    fun addLeagueToDB(league: League)
}