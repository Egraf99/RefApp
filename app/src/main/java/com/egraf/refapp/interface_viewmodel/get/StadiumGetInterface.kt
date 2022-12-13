package com.egraf.refapp.interface_viewmodel.get

import androidx.lifecycle.LiveData
import com.egraf.refapp.database.entities.Stadium

interface StadiumGetInterface: GetInterface {
    fun getStadiumsFromDB(): List<Stadium>
}