package com.egraf.refapp.interface_viewmodel.add

import com.egraf.refapp.database.entities.Referee

interface RefereeAddInterface: AddInterface {
    fun addRefereeToDB(referee: Referee)
}