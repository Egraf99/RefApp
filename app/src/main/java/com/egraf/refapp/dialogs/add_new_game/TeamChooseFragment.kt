package com.egraf.refapp.dialogs.add_new_game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.egraf.refapp.databinding.TeamChooseBinding
import com.egraf.refapp.views.textInput.TeamETI

class TeamChooseFragment : Fragment() {
    private val binding get() = _binding!!
    private var _binding: TeamChooseBinding? = null
    private val addNewGameViewModel: AddNewGameViewModel by lazy {
        ViewModelProvider(this)[AddNewGameViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TeamChooseBinding.inflate(inflater).apply {
            teamHomeLayout.init(
                this@TeamChooseFragment,
                addNewGameViewModel,
                TeamETI.TypeTeam.HOME_TEAM
            )
            teamGuestLayout.init(
                this@TeamChooseFragment,
                addNewGameViewModel,
                TeamETI.TypeTeam.GUEST_TEAM
            )
            leagueLayout.init(this@TeamChooseFragment, addNewGameViewModel)
        }
        updateUI()
        return binding.root
    }

    private fun updateUI() {
        updateETI()
    }

    private fun updateETI() {
        binding.teamGuestLayout.setText(addNewGameViewModel.createdGame.homeTeam?.shortName ?: "")
        binding.teamGuestLayout.setText(addNewGameViewModel.createdGame.guestTeam?.shortName ?: "")
        binding.leagueLayout.setText(addNewGameViewModel.createdGame.league?.shortName ?: "")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}