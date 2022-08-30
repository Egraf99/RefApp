package com.egraf.refapp.interface_viewmodel.get

import androidx.lifecycle.LiveData
import com.egraf.refapp.database.entities.Team

interface TeamGetInterface: GetInterface {
    fun getTeamsFromDB(): LiveData<List<Team>>
}