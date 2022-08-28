package com.egraf.refapp.views.textInput

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import com.egraf.refapp.dialogs.entity_add_dialog.TeamAddDialog
import com.egraf.refapp.interface_viewmodel.all.TeamInterface

private const val REQUEST_ADD_TEAM = "requestAddTeam"

class TeamETI(context: Context, attrs: AttributeSet? = null) :
    ETIWithEndButton(context, attrs), FragmentResultListener {

    fun init(fragment: Fragment, viewModel: TeamInterface) {
        super.init()
        setParentFragmentManager(fragment)

        viewModel.getTeamsFromDB().observe(fragment.viewLifecycleOwner) { teams ->
            setEntities(teams)
        }
        whatDoWhenInfoClicked { team ->
            Toast.makeText(context, team.fullName, Toast.LENGTH_SHORT).show()
        }
        whatDoWhenAddClicked { text ->
            TeamAddDialog(viewModel)
                .putEntityName(text, REQUEST_ADD_TEAM)
                .show(fragment.parentFragmentManager, REQUEST_ADD_TEAM)
        }
    }

    override fun setParentFragmentManager(fragment: Fragment) {
        fragment.parentFragmentManager.setFragmentResultListener(
            REQUEST_ADD_TEAM,
            fragment.viewLifecycleOwner,
            this
        )
    }

    override fun onFragmentResult(requestKey: String, result: Bundle) {
        when (requestKey) {
            REQUEST_ADD_TEAM -> this.setText(TeamAddDialog.getTeamShortName(result))
        }
    }
}