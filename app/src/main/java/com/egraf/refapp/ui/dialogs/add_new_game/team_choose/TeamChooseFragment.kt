package com.egraf.refapp.ui.dialogs.add_new_game.team_choose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.egraf.refapp.R
import com.egraf.refapp.database.local.entities.League
import com.egraf.refapp.database.local.entities.Team
import com.egraf.refapp.databinding.TeamChooseBinding
import com.egraf.refapp.ui.dialogs.add_new_game.ChooserFragment
import com.egraf.refapp.ui.dialogs.add_new_game.Position
import com.egraf.refapp.views.custom_views.GameComponent

private const val TAG = "AddGame"

class TeamChooseFragment : ChooserFragment() {
    private val binding get() = _binding!!
    private var _binding: TeamChooseBinding? = null

//    private val viewModel: TeamChooseViewModel by viewModels()

    override fun putGameComponentsInSavedBundle(bundle: Bundle): Bundle {
        return super.putGameComponentsInSavedBundle(bundle).apply {
            putParcelable(
                HOME_TEAM_VALUE,
                binding.homeTeamView.item.getOrElse { Team() }
            )
            putParcelable(
                GUEST_TEAM_VALUE,
                binding.guestTeamView.item.getOrElse { Team() }
            )
            putParcelable(
                LEAGUE_VALUE,
                binding.leagueView.item.getOrElse { League() }
            )
        }
    }

    override fun getGameComponentsFromSavedBundle(bundle: Bundle) {
        binding.homeTeamView.item =
            GameComponent(bundle.getParcelable<Team>(HOME_TEAM_VALUE)).filter { !it.isEmpty }

        binding.guestTeamView.item =
            GameComponent(bundle.getParcelable<Team>(GUEST_TEAM_VALUE)).filter { !it.isEmpty }

        binding.leagueView.item =
            GameComponent(bundle.getParcelable<League>(LEAGUE_VALUE)).filter { !it.isEmpty }
    }

    override fun showNextFragment() {
        val bundle = putComponentsInArguments()
        findNavController().navigate(R.id.action_choose_team_to_referee, bundle)
    }

    override fun showPreviousFragment() {
        putComponentsInArguments()
        findNavController().popBackStack()
    }

    override val nextPosition: Position = Position.LAST
    override val previousPosition: Position = Position.FIRST

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TeamChooseBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        binding.homeTeamView.bind(
            this.parentFragmentManager, viewLifecycleOwner, viewLifecycleOwner.lifecycleScope
        )
        binding.guestTeamView.bind(
            this.parentFragmentManager, viewLifecycleOwner, viewLifecycleOwner.lifecycleScope
        )
        binding.leagueView.bind(
            this.parentFragmentManager, viewLifecycleOwner, viewLifecycleOwner.lifecycleScope
        )
    }
}