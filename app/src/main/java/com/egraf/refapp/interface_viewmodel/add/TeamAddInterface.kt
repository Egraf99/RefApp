package com.egraf.refapp.interface_viewmodel.add

import android.os.Bundle
import androidx.lifecycle.LiveData
import com.egraf.refapp.database.entities.Team
import com.egraf.refapp.interface_viewmodel.add.AddInterface

interface TeamAddInterface: AddInterface {
    fun addTeamToDB(team: Team)
}