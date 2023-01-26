package com.egraf.refapp.interface_viewmodel.add

import com.egraf.refapp.database.local.entities.League

interface LeagueAddInterface: AddInterface {
    fun addLeagueToDB(league: League)
}