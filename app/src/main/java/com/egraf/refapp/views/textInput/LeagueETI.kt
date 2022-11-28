package com.egraf.refapp.views.textInput

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import com.egraf.refapp.ui.dialogs.entity_add_dialog.LeagueAddDialog
import com.egraf.refapp.interface_viewmodel.all.LeagueInterface

private const val REQUEST_ADD_LEAGUE = "requestAddLeague"
private const val TAG = "LeagueETI"

class LeagueETI(context: Context, attrs: AttributeSet? = null) :
    ETIWithEndButton(context, attrs), FragmentResultListener {
    fun init(fragment: Fragment, viewModel: LeagueInterface): LeagueETI {
        super.init()
        setParentFragmentManager(fragment)

        viewModel.getLeagueFromDB().observe(fragment.viewLifecycleOwner) {leagues ->
           setEntities(leagues)
        }
        doWhenInfoClicked { league ->
            Toast.makeText(context, league.fullName, Toast.LENGTH_SHORT).show()
        }
        doWhenAddClicked { text ->
            LeagueAddDialog(viewModel)
                .putEntityName(text, REQUEST_ADD_LEAGUE)
                .show(fragment.parentFragmentManager, REQUEST_ADD_LEAGUE)
        }
        return this
    }

    override fun onLongClick(view: View) {
        super.onLongClick(view)
        Log.d(TAG, "longClick")
    }

    override fun setParentFragmentManager(fragment: Fragment) {
        super.setParentFragmentManager(fragment)
        fragment.parentFragmentManager.setFragmentResultListener(
            REQUEST_ADD_LEAGUE,
            fragment.viewLifecycleOwner,
            this
        )
    }

    override fun onFragmentResult(requestKey: String, result: Bundle) {
        when (requestKey) {
            REQUEST_ADD_LEAGUE -> this.setText(LeagueAddDialog.getLeagueShortName(result))
        }
        super.onFragmentResult(requestKey, result)
    }
}