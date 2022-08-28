package com.egraf.refapp.views.textInput

import android.content.Context
import android.util.AttributeSet
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.egraf.refapp.dialogs.entity_add_dialog.StadiumAddDialog
import com.egraf.refapp.interface_viewmodel.StadiumAddInterface
import com.egraf.refapp.listeners.StadiumListener

class StadiumETI(context: Context, attrs: AttributeSet? = null) :
    ETIWithEndButton(context, attrs) {

    fun init(fragment: Fragment, viewModel: StadiumAddInterface) {
        super.init()
        viewModel.getStadiums().observe(fragment.viewLifecycleOwner) {stadiums ->
           setEntities(stadiums)
        }
        whatDoWhenInfoClicked { stadium ->
            Toast.makeText(context, stadium.fullName, Toast.LENGTH_SHORT).show()
        }
        whatDoWhenAddClicked { text ->
            StadiumAddDialog()
                .addName(StadiumListener.REQUEST_ADD_STADIUM_IN_DB, text)
                .show(fragment.parentFragmentManager, StadiumListener.REQUEST_ADD_STADIUM_IN_DB)
        }
    }
}