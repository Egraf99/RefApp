package com.egraf.refapp.listeners

import android.os.Bundle
import androidx.fragment.app.FragmentResultListener
import com.egraf.refapp.interface_viewmodel.TeamAddInterface

class TeamListener(val viewModel: TeamAddInterface): FragmentResultListener {
    override fun onFragmentResult(requestKey: String, result: Bundle) {
        when (requestKey) {
            REQUEST_ADD_TEAM_IN_DB -> viewModel.addTeam(result)
        }
    }

    companion object {
        const val REQUEST_ADD_TEAM_IN_DB = "AddTeamInDB"
    }
}