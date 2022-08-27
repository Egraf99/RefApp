package com.egraf.refapp.interface_viewmodel

import android.os.Bundle
import androidx.lifecycle.LiveData
import com.egraf.refapp.database.entities.Team

interface TeamAddInterface {
    fun addTeam(team: Team)
    fun addTeam(bundle: Bundle)
    fun getTeams(): LiveData<List<Team>>
    fun getTeamFromBundle(bundle: Bundle): Team
}