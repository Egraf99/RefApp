package com.egraf.refapp.ui.dialogs.add_new_game.team_choose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.egraf.refapp.GameRepository
import com.egraf.refapp.R
import com.egraf.refapp.database.entities.*
import com.egraf.refapp.databinding.TeamChooseBinding
import com.egraf.refapp.ui.dialogs.add_new_game.ChooserFragment
import com.egraf.refapp.ui.dialogs.add_new_game.Position
import com.egraf.refapp.ui.dialogs.entity_add_dialog.league.AddLeagueDialogFragment
import com.egraf.refapp.ui.dialogs.entity_add_dialog.league.InfoLeagueDialogFragment
import com.egraf.refapp.ui.dialogs.entity_add_dialog.team.AddTeamDialogFragment
import com.egraf.refapp.ui.dialogs.entity_add_dialog.team.InfoTeamDialogFragment
import com.egraf.refapp.ui.dialogs.search_entity.EmptyItem
import com.egraf.refapp.ui.dialogs.search_entity.SearchDialogFragment
import com.egraf.refapp.utils.close
import com.egraf.refapp.views.custom_views.GameComponent

private const val TAG = "AddGame"

class TeamChooseFragment : ChooserFragment(), FragmentResultListener {
    private val binding get() = _binding!!
    private var _binding: TeamChooseBinding? = null

    private val viewModel: TeamChooseViewModel by viewModels()

    override fun putGameComponentsInSavedBundle(bundle: Bundle): Bundle {
        return bundle.apply {
            putParcelable(
                HOME_TEAM_VALUE,
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        for (request in listOf(
            REQUEST_SEARCH_HOME_TEAM,
            REQUEST_ADD_HOME_TEAM,
            REQUEST_SEARCH_GUEST_TEAM,
            REQUEST_ADD_GUEST_TEAM,
            REQUEST_SEARCH_LEAGUE,
            REQUEST_ADD_LEAGUE,
        ))
            parentFragmentManager.setFragmentResultListener(request, viewLifecycleOwner, this)
    }

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
        binding.homeTeamView.apply {
            setOnClickListener {
                SearchDialogFragment(
                    this.title, this.icon,
                    receiveSearchItems = { GameRepository.get().getTeams() },
                    request = REQUEST_SEARCH_HOME_TEAM
                ).show(parentFragmentManager, FRAGMENT_SEARCH_HOME_TEAM)
            }
            setOnInfoClickListener {
                InfoTeamDialogFragment(
                    this.title,
                    componentId = (this.item.getOrThrow(IllegalStateException("Info button shouldn't be able when GameComponentView don't have item")) as Team).savedValue
                ).show(parentFragmentManager, FRAGMENT_INFO_HOME_TEAM)
            }
        }
        binding.guestTeamView.apply {
            setOnClickListener {
                SearchDialogFragment(
                    this.title, this.icon,
                    receiveSearchItems = { GameRepository.get().getTeams() },
                    request = REQUEST_SEARCH_GUEST_TEAM
                ).show(parentFragmentManager, FRAGMENT_SEARCH_GUEST_TEAM)
            }
            setOnInfoClickListener {
                InfoTeamDialogFragment(
                    this.title,
                    componentId = (this.item.getOrThrow(IllegalStateException("Info button shouldn't be able when GameComponentView don't have item")) as Team).savedValue
                ).show(parentFragmentManager, FRAGMENT_INFO_GUEST_TEAM)
            }
        }
        binding.leagueView.apply {
            setOnClickListener {
                SearchDialogFragment(
                    this.title, this.icon,
                    receiveSearchItems = { GameRepository.get().getLeagues() },
                    request = REQUEST_SEARCH_LEAGUE
                ).show(parentFragmentManager, FRAGMENT_SEARCH_LEAGUE)
            }
            setOnInfoClickListener {
                InfoLeagueDialogFragment(
                    this.title,
                    componentId = (this.item.getOrThrow(IllegalStateException("Info button shouldn't be able when GameComponentView don't have item")) as League).savedValue
                ).show(parentFragmentManager, FRAGMENT_INFO_LEAGUE)
            }
        }
    }

    override fun onFragmentResult(requestKey: String, result: Bundle) {
        when (requestKey) {
            REQUEST_SEARCH_HOME_TEAM -> {
                val item = GameComponent(
                    Team(
                        SearchDialogFragment.getId(result),
                        SearchDialogFragment.getTitle(result),
                    )
                ).filter { it.id != EmptyItem.id }
                when (SearchDialogFragment.getTypeOfResult(result)) {
                    SearchDialogFragment.Companion.ResultRequest.SEARCH_ITEM_RESULT_REQUEST -> {
                        binding.homeTeamView.item = item
                        parentFragmentManager.close(FRAGMENT_SEARCH_HOME_TEAM)
                    }
                    SearchDialogFragment.Companion.ResultRequest.INFO_RESULT_REQUEST -> {
                        InfoTeamDialogFragment(
                            title = getString(R.string.team),
                            componentId = SearchDialogFragment.getId(result),
                        ).show(parentFragmentManager, FRAGMENT_INFO_HOME_TEAM)
                    }
                    SearchDialogFragment.Companion.ResultRequest.ADD_RESULT_REQUEST -> {
                        AddTeamDialogFragment(
                            title = getString(R.string.add_team),
                            entityTitle = SearchDialogFragment.getTitle(result),
                            request = REQUEST_ADD_HOME_TEAM,
                            functionSaveEntityInDB = viewModel.addTeamToDB
                        ).show(parentFragmentManager, FRAGMENT_ADD_HOME_TEAM)
                    }
                }
            }
            REQUEST_ADD_HOME_TEAM -> {
                parentFragmentManager.close(FRAGMENT_SEARCH_HOME_TEAM, FRAGMENT_ADD_HOME_TEAM)
                binding.homeTeamView.item =
                    GameComponent(
                        Team(
                            AddTeamDialogFragment.getId(result),
                            AddTeamDialogFragment.getTitle(result),
                        )
                    )
            }
            REQUEST_SEARCH_GUEST_TEAM -> {
                val item = GameComponent(
                    Team(
                        SearchDialogFragment.getId(result),
                        SearchDialogFragment.getTitle(result),
                    )
                ).filter { it.id != EmptyItem.id }
                when (SearchDialogFragment.getTypeOfResult(result)) {
                    SearchDialogFragment.Companion.ResultRequest.SEARCH_ITEM_RESULT_REQUEST -> {
                        binding.guestTeamView.item = item
                        parentFragmentManager.close(FRAGMENT_SEARCH_GUEST_TEAM)
                    }
                    SearchDialogFragment.Companion.ResultRequest.INFO_RESULT_REQUEST -> {
                        InfoTeamDialogFragment(
                            title = getString(R.string.team),
                            componentId = SearchDialogFragment.getId(result),
                        ).show(parentFragmentManager, FRAGMENT_INFO_GUEST_TEAM)
                    }
                    SearchDialogFragment.Companion.ResultRequest.ADD_RESULT_REQUEST -> {
                        AddTeamDialogFragment(
                            title = getString(R.string.add_team),
                            entityTitle = SearchDialogFragment.getTitle(result),
                            request = REQUEST_ADD_GUEST_TEAM,
                            functionSaveEntityInDB = viewModel.addTeamToDB
                        ).show(parentFragmentManager, FRAGMENT_ADD_GUEST_TEAM)
                    }
                }
            }
            REQUEST_ADD_GUEST_TEAM -> {
                parentFragmentManager.close(FRAGMENT_SEARCH_GUEST_TEAM, FRAGMENT_ADD_GUEST_TEAM)
                binding.guestTeamView.item =
                    GameComponent(
                        Team(
                            AddTeamDialogFragment.getId(result),
                            AddTeamDialogFragment.getTitle(result),
                        )
                    )
            }
            REQUEST_SEARCH_LEAGUE -> {
                val item = GameComponent(
                    League(
                        SearchDialogFragment.getId(result),
                        SearchDialogFragment.getTitle(result),
                    )
                ).filter { it.id != EmptyItem.id }
                when (SearchDialogFragment.getTypeOfResult(result)) {
                    SearchDialogFragment.Companion.ResultRequest.SEARCH_ITEM_RESULT_REQUEST -> {
                        binding.leagueView.item = item
                        parentFragmentManager.close(FRAGMENT_SEARCH_LEAGUE)
                    }
                    SearchDialogFragment.Companion.ResultRequest.INFO_RESULT_REQUEST -> {
                        InfoLeagueDialogFragment(
                            title = getString(R.string.league),
                            componentId = SearchDialogFragment.getId(result),
                        ).show(parentFragmentManager, FRAGMENT_INFO_LEAGUE)
                    }
                    SearchDialogFragment.Companion.ResultRequest.ADD_RESULT_REQUEST -> {
                        AddLeagueDialogFragment(
                            title = getString(R.string.add_league),
                            entityTitle = SearchDialogFragment.getTitle(result),
                            request = REQUEST_ADD_LEAGUE,
                            functionSaveEntityInDB = viewModel.addLeagueToDB
                        ).show(parentFragmentManager, FRAGMENT_ADD_LEAGUE)
                    }
                }
            }
            REQUEST_ADD_LEAGUE -> {
                parentFragmentManager.close(FRAGMENT_SEARCH_LEAGUE, FRAGMENT_ADD_LEAGUE)
                binding.leagueView.item =
                    GameComponent(
                        League(
                            AddLeagueDialogFragment.getId(result),
                            AddLeagueDialogFragment.getTitle(result),
                        )
                    )
            }
        }
    }

    companion object {
        private const val REQUEST_SEARCH_HOME_TEAM = "RequestHomeTeam"
        private const val REQUEST_ADD_HOME_TEAM = "RequestAddHomeTeam"
        private const val REQUEST_SEARCH_GUEST_TEAM = "RequestGuestTeam"
        private const val REQUEST_ADD_GUEST_TEAM = "RequestAddGuestTeam"
        private const val REQUEST_SEARCH_LEAGUE = "RequestLeague"
        private const val REQUEST_ADD_LEAGUE = "RequestAddLeague"

        private const val FRAGMENT_SEARCH_HOME_TEAM = "FragmentSearchHomeTeam"
        private const val FRAGMENT_ADD_HOME_TEAM = "FragmentAddHomeTeam"
        private const val FRAGMENT_INFO_HOME_TEAM = "FragmentAddHomeTeam"
        private const val FRAGMENT_SEARCH_GUEST_TEAM = "FragmentSearchGuestTeam"
        private const val FRAGMENT_ADD_GUEST_TEAM = "FragmentAddGuestTeam"
        private const val FRAGMENT_INFO_GUEST_TEAM = "FragmentAddGuestTeam"
        private const val FRAGMENT_SEARCH_LEAGUE = "FragmentSearchLeague"
        private const val FRAGMENT_ADD_LEAGUE = "FragmentAddLeague"
        private const val FRAGMENT_INFO_LEAGUE = "FragmentAddLeague"
    }
}