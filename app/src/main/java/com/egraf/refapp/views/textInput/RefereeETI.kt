package com.egraf.refapp.views.textInput

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import com.egraf.refapp.ui.dialogs.entity_add_dialog.RefereeAddDialog
import com.egraf.refapp.interface_viewmodel.all.RefereeInterface

private const val TAG = "RefereeETI"
private const val REQUEST_ADD_CHIEF_REFEREE = "requestAddChiefReferee"
private const val REQUEST_ADD_FIRST_REFEREE = "requestAddFirstReferee"
private const val REQUEST_ADD_SECOND_REFEREE = "requestAddSecondReferee"
private const val REQUEST_ADD_RESERVE_REFEREE = "requestAddReserveReferee"
private const val REQUEST_ADD_INSPECTOR = "requestAddInspector"

class RefereeETI(context: Context, attrs: AttributeSet? = null) :
    ETIWithEndButton(context, attrs), FragmentResultListener {
    enum class TypeReferee(val requestKey: String) {
        CHIEF_REFEREE(REQUEST_ADD_CHIEF_REFEREE),
        FIRST_REFEREE(REQUEST_ADD_FIRST_REFEREE),
        SECOND_REFEREE(REQUEST_ADD_SECOND_REFEREE),
        RESERVE_REFEREE(REQUEST_ADD_RESERVE_REFEREE),
        INSPECTOR(REQUEST_ADD_INSPECTOR),
    }

    private lateinit var typeReferee: TypeReferee

    fun init(fragment: Fragment, viewModel: RefereeInterface, typeReferee: TypeReferee): RefereeETI {
        super.init()
        this.typeReferee = typeReferee
        setParentFragmentManager(fragment)

        viewModel.getRefereeFromDB().observe(fragment.viewLifecycleOwner) {referee ->
           setEntities(referee)
        }
        doWhenInfoClicked { referee ->
            Toast.makeText(context, referee.fullName, Toast.LENGTH_SHORT).show()
        }
        doWhenAddClicked { text ->
            RefereeAddDialog(viewModel)
                .putEntityName(text, typeReferee.requestKey)
                .show(fragment.parentFragmentManager, typeReferee.requestKey)
        }
        return this
    }

    override fun setParentFragmentManager(fragment: Fragment) {
        super.setParentFragmentManager(fragment)
        fragment.parentFragmentManager.setFragmentResultListener(
            typeReferee.requestKey,
            fragment.viewLifecycleOwner,
            this
        )
    }

    override fun onFragmentResult(requestKey: String, result: Bundle) {
        when(requestKey) {
            typeReferee.requestKey -> {
                this.setText(RefereeAddDialog.getRefereeShortName(result))
            }
        }
        super.onFragmentResult(requestKey, result)
    }
}