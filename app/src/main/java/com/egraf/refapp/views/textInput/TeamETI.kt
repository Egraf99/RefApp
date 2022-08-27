package com.egraf.refapp.views.textInput

import android.content.Context
import android.util.AttributeSet
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.egraf.refapp.dialogs.TeamAddDialog
import com.egraf.refapp.interface_viewmodel.TeamAddInterface
import com.egraf.refapp.listeners.TeamListener

class TeamETI(context: Context, attrs: AttributeSet? = null) :
    ETIWithEndButton(context, attrs) {

    fun init(fragment: Fragment, viewModel: TeamAddInterface) {
        super.init()
        viewModel.getTeams().observe(fragment.viewLifecycleOwner) {teams ->
           setEntities(teams)
        }
        whatDoWhenInfoClicked { team ->
            Toast.makeText(context, team.fullName, Toast.LENGTH_SHORT).show()
        }
        whatDoWhenAddClicked { text ->
            TeamAddDialog()
                .addName(TeamListener.REQUEST_ADD_TEAM_IN_DB, text)
                .show(fragment.parentFragmentManager, TeamListener.REQUEST_ADD_TEAM_IN_DB)
        }
    }
}