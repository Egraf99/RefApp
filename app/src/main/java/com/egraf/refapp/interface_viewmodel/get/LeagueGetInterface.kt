package com.egraf.refapp.interface_viewmodel.get

import androidx.lifecycle.LiveData
import com.egraf.refapp.database.entities.League

interface LeagueGetInterface: GetInterface {
    fun getLeagueFromDB(): List<League>
}