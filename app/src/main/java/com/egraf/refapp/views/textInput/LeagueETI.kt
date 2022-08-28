package com.egraf.refapp.views.textInput

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import com.egraf.refapp.dialogs.entity_add_dialog.LeagueAddDialog
import com.egraf.refapp.interface_viewmodel.all.LeagueInterface

private const val REQUEST_ADD_LEAGUE = "requestAddLeague"

class LeagueETI(context: Context, attrs: AttributeSet? = null) :
    ETIWithEndButton(context, attrs), FragmentResultListener {

    fun init(fragment: Fragment, viewModel: LeagueInterface) {
        super.init()
        viewModel.getLeagueFromDB().observe(fragment.viewLifecycleOwner) {leagues ->
           setEntities(leagues)
        }
        whatDoWhenInfoClicked { league ->
            Toast.makeText(context, league.fullName, Toast.LENGTH_SHORT).show()
        }
        whatDoWhenAddClicked { text ->
            LeagueAddDialog(viewModel)
                .putEntityName(text, REQUEST_ADD_LEAGUE)
                .show(fragment.parentFragmentManager, REQUEST_ADD_LEAGUE)
        }
    }

    override fun setParentFragmentManager(fragment: Fragment) {
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
    }
}