package com.egraf.refapp.interface_viewmodel.get

import com.egraf.refapp.database.local.entities.Team

interface TeamGetInterface: GetInterface {
    fun getTeamsFromDB(): List<Team>
}