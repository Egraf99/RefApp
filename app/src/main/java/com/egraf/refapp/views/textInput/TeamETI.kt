package com.egraf.refapp.views.textInput

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import com.egraf.refapp.dialogs.entity_add_dialog.TeamAddDialog
import com.egraf.refapp.interface_viewmodel.all.TeamInterface

private const val REQUEST_ADD_HOME_TEAM = "requestAddHomeTeam"
private const val REQUEST_ADD_GUEST_TEAM = "requestAddGuestTeam"

class TeamETI(context: Context, attrs: AttributeSet? = null) :
    ETIWithEndButton(context, attrs), FragmentResultListener {
    enum class TypeTeam(val requestKey: String) {
        HOME_TEAM(REQUEST_ADD_HOME_TEAM),
        GUEST_TEAM(REQUEST_ADD_GUEST_TEAM)
    }
    private lateinit var typeTeam: TypeTeam

    fun init(fragment: Fragment, viewModel: TeamInterface, typeTeam: TypeTeam): TeamETI {
        super.init()
        this.typeTeam = typeTeam
        setParentFragmentManager(fragment)

        viewModel.getTeamsFromDB().observe(fragment.viewLifecycleOwner) { teams ->
            setEntities(teams)
        }
        doWhenInfoClicked { team ->
            Toast.makeText(context, team.fullName, Toast.LENGTH_SHORT).show()
        }
        doWhenAddClicked { text ->
            TeamAddDialog(viewModel)
                .putEntityName(text, typeTeam.requestKey)
                .show(fragment.parentFragmentManager, typeTeam.requestKey)
        }
        return this
    }

    override fun setParentFragmentManager(fragment: Fragment) {
        fragment.parentFragmentManager.setFragmentResultListener(
            typeTeam.requestKey,
            fragment.viewLifecycleOwner,
            this
        )
    }

    override fun onFragmentResult(requestKey: String, result: Bundle) {
        when (requestKey) {
            typeTeam.requestKey -> this.setText(TeamAddDialog.getTeamShortName(result))
        }
    }
}