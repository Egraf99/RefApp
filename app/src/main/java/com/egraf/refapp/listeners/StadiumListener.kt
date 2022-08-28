package com.egraf.refapp.listeners

import android.os.Bundle
import androidx.fragment.app.FragmentResultListener
import com.egraf.refapp.interface_viewmodel.StadiumAddInterface

class StadiumListener(val viewModel: StadiumAddInterface): FragmentResultListener {
    override fun onFragmentResult(requestKey: String, result: Bundle) {
        when (requestKey) {
            REQUEST_ADD_STADIUM_IN_DB -> viewModel.addStadium(result)
        }
    }

    companion object {
        const val REQUEST_ADD_STADIUM_IN_DB = "AddTeamInDB"
    }
}