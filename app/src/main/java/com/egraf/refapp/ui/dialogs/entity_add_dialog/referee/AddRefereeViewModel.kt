package com.egraf.refapp.ui.dialogs.entity_add_dialog.referee

import androidx.lifecycle.ViewModel
import com.egraf.refapp.database.local.entities.Referee
import com.egraf.refapp.utils.Resource
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import java.util.*

class AddRefereeViewModel : ViewModel() {
    var title: String = ""
    var referee: Referee = Referee()
    var saveInDBFun: (Referee) -> StateFlow<Resource<Pair<UUID, String>>> = {
        flow {
            emit(Resource.error("Didn't init saveInDB function"))
        } as StateFlow<Resource<Pair<UUID, String>>>
    }
}