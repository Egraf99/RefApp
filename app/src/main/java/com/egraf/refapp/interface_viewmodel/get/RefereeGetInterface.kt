package com.egraf.refapp.interface_viewmodel.get

import com.egraf.refapp.database.local.entities.Referee

interface RefereeGetInterface: GetInterface {
    fun getRefereeFromDB(): List<Referee>
}