package com.egraf.refapp.ui.dialogs.entity_add_dialog.stadium

import androidx.lifecycle.ViewModel
import com.egraf.refapp.utils.Resource
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import java.util.*

class EntityAddViewModel : ViewModel() {
    var title: String = ""
    var entityTitle: String? = null
    var saveInDBFun: (String) -> StateFlow<Resource<Pair<UUID, String>>> = {
        flow {
            emit(Resource.error(null, "Didn't init saveInDB function"))
        } as StateFlow<Resource<Pair<UUID, String>>>
    }
}