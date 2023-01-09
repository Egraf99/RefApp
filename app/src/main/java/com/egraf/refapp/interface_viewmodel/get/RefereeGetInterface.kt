package com.egraf.refapp.interface_viewmodel.get

import androidx.lifecycle.LiveData
import com.egraf.refapp.database.entities.Referee

interface RefereeGetInterface: GetInterface {
    fun getRefereeFromDB(): List<Referee>
}