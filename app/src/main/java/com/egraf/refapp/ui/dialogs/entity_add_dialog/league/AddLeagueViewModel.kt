package com.egraf.refapp.ui.dialogs.entity_add_dialog.league

import androidx.lifecycle.ViewModel
import com.egraf.refapp.utils.Resource
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import java.util.*

class AddLeagueViewModel : ViewModel() {
    var title: String = ""
    var entityTitle: String = ""
    var saveInDBFun: (String) -> StateFlow<Resource<Pair<UUID, String>>> = {
        flow {
            emit(Resource.error("Didn't init saveInDB function"))
        } as StateFlow<Resource<Pair<UUID, String>>>
    }
}