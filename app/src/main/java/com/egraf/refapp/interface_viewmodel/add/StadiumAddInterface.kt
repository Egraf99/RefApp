package com.egraf.refapp.interface_viewmodel.add

import androidx.lifecycle.LiveData
import com.egraf.refapp.database.entities.Stadium
import com.egraf.refapp.interface_viewmodel.add.AddInterface

interface StadiumAddInterface: AddInterface {
    fun addStadiumToDB(stadium: Stadium)
}