package com.egraf.refapp.dialogs.add_new_game

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.egraf.refapp.database.entities.Team
import com.egraf.refapp.databinding.TeamChooseBinding
import com.egraf.refapp.views.textInput.TeamETI

private const val TAG = "AddGame"
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
                .whatDoWhenTextMatchedEntity { team ->
                    addNewGameViewModel.setHomeTeam(team as Team?)
                }
                .whatDoWhenTextIsBlank {
                    addNewGameViewModel.setHomeTeam(null)
                }
            teamGuestLayout.init(
                this@TeamChooseFragment,
                addNewGameViewModel,
                TeamETI.TypeTeam.GUEST_TEAM
            )
                .whatDoWhenTextMatchedEntity { team ->
                    addNewGameViewModel.setGuestTeam(team as Team?)
                }
                .whatDoWhenTextIsBlank {
                    addNewGameViewModel.setGuestTeam(null)
                }
            leagueLayout.init(this@TeamChooseFragment, addNewGameViewModel)
        }
        updateUI()
        return binding.root
    }

    private fun updateUI() {
        updateETI()
    }

    private fun updateETI() {
        Log.d(TAG, "updateETI: ${addNewGameViewModel.gameWithAttributes.game}")
        binding.teamGuestLayout.setText(addNewGameViewModel.gameWithAttributes.homeTeam?.shortName ?: "")
        binding.teamGuestLayout.setText(addNewGameViewModel.gameWithAttributes.guestTeam?.shortName ?: "")
        binding.leagueLayout.setText(addNewGameViewModel.gameWithAttributes.league?.shortName ?: "")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}