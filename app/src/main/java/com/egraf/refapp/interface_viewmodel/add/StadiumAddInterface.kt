package com.egraf.refapp.interface_viewmodel.add

import com.egraf.refapp.database.local.entities.Stadium

interface StadiumAddInterface: AddInterface {
    fun addStadiumToDB(stadium: Stadium)
}