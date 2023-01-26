package com.egraf.refapp.interface_viewmodel.get

import com.egraf.refapp.database.local.entities.League

interface LeagueGetInterface: GetInterface {
    fun getLeagueFromDB(): List<League>
}