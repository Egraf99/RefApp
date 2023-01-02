package com.egraf.refapp.ui.dialogs.add_new_game

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.egraf.refapp.database.entities.League
import com.egraf.refapp.database.entities.Team
import com.egraf.refapp.databinding.TeamChooseBinding
import com.egraf.refapp.views.textInput.TeamETI

private const val TAG = "AddGame"
class TeamChooseFragment : ChooserFragment() {
    private val binding get() = _binding!!
    private var _binding: TeamChooseBinding? = null

    override fun putGameComponentsInSavedBundle(bundle: Bundle): Bundle {
        return bundle
    }

    override fun getGameComponentsFromSavedBundle(bundle: Bundle) {
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
                .whatDoWhenTextMatchedEntity { league ->
                    addNewGameViewModel.setLeague(league as League)
                }
                .whatDoWhenTextIsBlank {
                    addNewGameViewModel.setLeague(null)
                }
        }
        updateUI()
        return binding.root
    }

    private fun updateUI() {
        updateETI()
    }

    private fun updateETI() {
        Log.d(TAG, "TeamChooseFragment updateETI: ${addNewGameViewModel.gameWithAttributes.homeTeam}")
        binding.teamHomeLayout.setText(addNewGameViewModel.gameWithAttributes.homeTeam?.shortName ?: "")
        binding.teamGuestLayout.setText(addNewGameViewModel.gameWithAttributes.guestTeam?.shortName ?: "")
        binding.leagueLayout.setText(addNewGameViewModel.gameWithAttributes.league?.shortName ?: "")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}