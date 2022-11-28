package com.egraf.refapp.ui.dialogs.entity_add_dialog.stadium

import androidx.lifecycle.ViewModel
import com.egraf.refapp.database.entities.Stadium
import com.egraf.refapp.ui.ViewModelWithGameRepo

class StadiumAddViewModel: ViewModelWithGameRepo() {
    fun addStadiumToDB(stadium: Stadium) = gameRepository.addStadium(stadium)
}