package com.egraf.refapp.ui.dialogs.add_new_game.referee_choose

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.egraf.refapp.database.entities.League
import com.egraf.refapp.database.entities.Referee
import com.egraf.refapp.database.entities.Team
import com.egraf.refapp.databinding.RefereeChooseBinding
import com.egraf.refapp.ui.dialogs.add_new_game.ChooserFragment
import com.egraf.refapp.ui.dialogs.add_new_game.Position
import com.egraf.refapp.views.textInput.RefereeETI

class RefereeChooseFragment: ChooserFragment() {
    private val binding get() = _binding!!
    private var _binding: RefereeChooseBinding? = null

    override fun putGameComponentsInSavedBundle(bundle: Bundle): Bundle {
        return bundle.apply {
            putParcelable(
                CHIEF_REFEREE_VALUE,
                binding.homeTeamView.item
                    .getOrElse { Team() } as Team
            )
            putParcelable(
                GUEST_TEAM_VALUE,
                binding.guestTeamView.item
                    .getOrElse { Team() } as Team
            )
            putParcelable(
                LEAGUE_VALUE,
                binding.leagueView.item
                    .getOrElse { League() } as League
            )
        }
    }

    override fun getGameComponentsFromSavedBundle(bundle: Bundle) {
    }

    override fun showNextFragment() {
    }

    override fun showPreviousFragment() {
        findNavController().popBackStack()
    }

    override val nextPosition: Position = Position.DISMISS
    override val previousPosition: Position = Position.MIDDLE

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RefereeChooseBinding.inflate(inflater).apply {
        }
        updateUI()
        return binding.root
    }

    private fun updateUI() {
        updateETI()
    }

    private fun updateETI() {
//        binding.chiefRefereeLayout.setText(addNewGameViewModel.gameWithAttributes.chiefReferee?.shortName ?: "")
//        binding.firstRefereeLayout.setText(addNewGameViewModel.gameWithAttributes.firstReferee?.shortName ?: "")
//        binding.secondRefereeLayout.setText(addNewGameViewModel.gameWithAttributes.secondReferee?.shortName ?: "")
//        binding.reserveRefereeLayout.setText(addNewGameViewModel.gameWithAttributes.reserveReferee?.shortName ?: "")
//        binding.inspectorLayout.setText(addNewGameViewModel.gameWithAttributes.inspector?.shortName ?: "")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val REQUEST_SEARCH_CHIEF_REFEREE = "RequestHomeTeam"
        private const val REQUEST_ADD_CHIEF_REFEREE = "RequestAddHomeTeam"
        private const val REQUEST_SEARCH_GUEST_TEAM = "RequestGuestTeam"
        private const val REQUEST_ADD_GUEST_TEAM = "RequestAddGuestTeam"
        private const val REQUEST_SEARCH_LEAGUE = "RequestLeague"
        private const val REQUEST_ADD_LEAGUE = "RequestAddLeague"

        private const val FRAGMENT_SEARCH_CHIEF_REFEREE = "FragmentSearchHomeTeam"
        private const val FRAGMENT_ADD_CHIEF_REFEREE = "FragmentAddHomeTeam"
        private const val FRAGMENT_INFO_CHIEF_REFEREE = "FragmentAddHomeTeam"
        private const val FRAGMENT_SEARCH_GUEST_TEAM = "FragmentSearchGuestTeam"
        private const val FRAGMENT_ADD_GUEST_TEAM = "FragmentAddGuestTeam"
        private const val FRAGMENT_INFO_GUEST_TEAM = "FragmentAddGuestTeam"
        private const val FRAGMENT_SEARCH_LEAGUE = "FragmentSearchLeague"
        private const val FRAGMENT_ADD_LEAGUE = "FragmentAddLeague"
        private const val FRAGMENT_INFO_LEAGUE = "FragmentAddLeague"
    }
}