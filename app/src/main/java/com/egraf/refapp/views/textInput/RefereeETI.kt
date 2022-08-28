package com.egraf.refapp.views.textInput

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import com.egraf.refapp.R
import com.egraf.refapp.dialogs.entity_add_dialog.RefereeAddDialog
import com.egraf.refapp.interface_viewmodel.all.RefereeInterface

private const val REQUEST_ADD_REFEREE = "requestAddReferee"

class RefereeETI(context: Context, attrs: AttributeSet? = null) :
    ETIWithEndButton(context, attrs), FragmentResultListener {

    fun init(fragment: Fragment, viewModel: RefereeInterface) {
        super.init()
        viewModel.getRefereeFromDB().observe(fragment.viewLifecycleOwner) {referee ->
           setEntities(referee)
        }
        whatDoWhenInfoClicked { referee ->
            Toast.makeText(context, referee.fullName, Toast.LENGTH_SHORT).show()
        }
        whatDoWhenAddClicked { text ->
            RefereeAddDialog(viewModel)
                .putEntityName(text, REQUEST_ADD_REFEREE)
                .show(fragment.parentFragmentManager, REQUEST_ADD_REFEREE)
        }
    }

    override fun setParentFragmentManager(fragment: Fragment) {
        fragment.parentFragmentManager.setFragmentResultListener(
            REQUEST_ADD_REFEREE,
            fragment.viewLifecycleOwner,
            this
        )
    }

    override fun onFragmentResult(requestKey: String, result: Bundle) {
        when(requestKey) {
            REQUEST_ADD_REFEREE -> {
                this.setText(RefereeAddDialog.getRefereeShortName(result))
            }
        }
    }
}