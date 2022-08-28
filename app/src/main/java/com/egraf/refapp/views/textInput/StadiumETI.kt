package com.egraf.refapp.views.textInput

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import com.egraf.refapp.dialogs.entity_add_dialog.LeagueAddDialog
import com.egraf.refapp.dialogs.entity_add_dialog.StadiumAddDialog
import com.egraf.refapp.interface_viewmodel.all.StadiumInterface

private const val REQUEST_ADD_STADIUM = "requestAddStadium"

class StadiumETI(context: Context, attrs: AttributeSet? = null) :
    ETIWithEndButton(context, attrs), FragmentResultListener {

    fun init(fragment: Fragment, viewModel: StadiumInterface) {
        super.init()
        viewModel.getStadiumsFromDB().observe(fragment.viewLifecycleOwner) {stadiums ->
           setEntities(stadiums)
        }
        whatDoWhenInfoClicked { stadium ->
            Toast.makeText(context, stadium.fullName, Toast.LENGTH_SHORT).show()
        }
        whatDoWhenAddClicked { text ->
            StadiumAddDialog(viewModel)
                .putEntityName(text, REQUEST_ADD_STADIUM)
                .show(fragment.parentFragmentManager, REQUEST_ADD_STADIUM)
        }
    }

    override fun setParentFragmentManager(fragment: Fragment) {
        fragment.parentFragmentManager.setFragmentResultListener(
            REQUEST_ADD_STADIUM,
            fragment.viewLifecycleOwner,
            this
        )
    }

    override fun onFragmentResult(requestKey: String, result: Bundle) {
        when (requestKey) {
            REQUEST_ADD_STADIUM -> this.setText(StadiumAddDialog.getStadiumShortName(result))
        }
    }
}