package com.egraf.refapp.views.textInput

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import com.egraf.refapp.InputFragment
import com.egraf.refapp.dialogs.entity_add_dialog.StadiumAddDialog
import com.egraf.refapp.interface_viewmodel.all.StadiumInterface

private const val REQUEST_ADD_STADIUM = "requestAddStadium"

class StadiumETI(context: Context, attrs: AttributeSet? = null) :
    ETIWithEndButton(context, attrs), FragmentResultListener {

    fun init(fragment: Fragment, viewModel: StadiumInterface): StadiumETI {
        super.init()
        setParentFragmentManager(fragment)

        viewModel.getStadiumsFromDB().observe(fragment.viewLifecycleOwner) { stadiums ->
           setEntities(stadiums)
        }
        doWhenInfoClicked { stadium ->
            Toast.makeText(context, stadium.fullName, Toast.LENGTH_SHORT).show()
        }
        doWhenAddClicked { text ->
            StadiumAddDialog(viewModel)
                .putEntityName(text, REQUEST_ADD_STADIUM)
                .show(fragment.parentFragmentManager, REQUEST_ADD_STADIUM)
        }
        return this
    }

    override fun setParentFragmentManager(fragment: Fragment) {
        super.setParentFragmentManager(fragment)
        fragment.parentFragmentManager.setFragmentResultListener(
            REQUEST_ADD_STADIUM,
            fragment.viewLifecycleOwner,
            this
        )
    }

    override fun onLongClick(view: View) {
        super.onLongClick(view)
        InputFragment("Stadium", listOf()).show(parentFragment.parentFragmentManager, null)
    }

    override fun onFragmentResult(requestKey: String, result: Bundle) {
        when (requestKey) {
            REQUEST_ADD_STADIUM -> this.setText(StadiumAddDialog.getStadiumShortName(result))
        }
        super.onFragmentResult(requestKey, result)
    }
}