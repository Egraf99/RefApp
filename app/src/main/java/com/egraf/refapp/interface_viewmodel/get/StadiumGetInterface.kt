package com.egraf.refapp.interface_viewmodel.get

import com.egraf.refapp.database.local.entities.Stadium

interface StadiumGetInterface: GetInterface {
    fun getStadiumsFromDB(): List<Stadium>
}