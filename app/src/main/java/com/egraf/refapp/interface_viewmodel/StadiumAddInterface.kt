package com.egraf.refapp.interface_viewmodel

import android.os.Bundle
import androidx.lifecycle.LiveData
import com.egraf.refapp.database.entities.Stadium

interface StadiumAddInterface {
    fun addStadium(stadium: Stadium)
    fun addStadium(bundle: Bundle)
    fun getStadiums(): LiveData<List<Stadium>>
    fun getStadiumFromBundle(bundle: Bundle): Stadium
}