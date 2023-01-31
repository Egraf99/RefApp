package com.egraf.refapp.views.custom_views.game_components

import android.content.Context
import android.util.AttributeSet
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentResultListener
import com.egraf.refapp.GameRepository
import com.egraf.refapp.R
import com.egraf.refapp.database.local.entities.Team
import com.egraf.refapp.ui.dialogs.entity_add_dialog.team.AddTeamDialogFragment
import com.egraf.refapp.ui.dialogs.entity_add_dialog.team.InfoTeamDialogFragment
import com.egraf.refapp.utils.Resource
import com.egraf.refapp.views.custom_views.GameComponent
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import java.util.*

class GuestTeamComponentInputWithDialogs(context: Context, attrs: AttributeSet) :
    AbstractComponentInputWithDialogs<Team>(
        context,
        attrs,
        REQUEST_SEARCH_GUEST_TEAM,
        REQUEST_ADD_GUEST_TEAM,
        REQUEST_INFO_GUEST_TEAM,
        FRAGMENT_SEARCH_GUEST_TEAM,
        FRAGMENT_INFO_GUEST_TEAM,
        FRAGMENT_ADD_GUEST_TEAM,
        { GameRepository.get().getTeams() },
        { id, name -> GameComponent(Team(id, name)) },
    ), FragmentResultListener {

    override val addDialogFragment: (String) -> DialogFragment =
        { title ->
            AddTeamDialogFragment(
                title = context.getString(R.string.add_team),
                entityTitle = title,
                request = REQUEST_ADD_GUEST_TEAM,
                functionSaveEntityInDB = addTeamToDB
            )
        }

    override val infoDialogFragment = { id: UUID ->
        InfoTeamDialogFragment(
            title = context.getString(R.string.team),
            componentId = id,
            deleteFunction = { GameRepository.get().deleteTeam(it) },
            request = REQUEST_INFO_GUEST_TEAM
        )
    }

    private val addTeamToDB: (String) -> StateFlow<Resource<Pair<UUID, String>>> =
        { teamName ->
            val team = Team(teamName)
            flow {
                try {
                    GameRepository.get().addTeam(team)
                    emit(Resource.success(Pair(team.id, team.name)))
                } catch (e: Exception) {
                    emit(Resource.error(e))
                }
            }.stateIn(
                scope = coroutineScope,
                started = SharingStarted.WhileSubscribed(5000), // Or Lazily because it's a one-shot
                initialValue = Resource.loading()
            )
        }

    companion object {
        const val REQUEST_SEARCH_GUEST_TEAM = "RequestGuestTeam"
        const val REQUEST_ADD_GUEST_TEAM = "RequestAddGuestTeam"
        const val REQUEST_INFO_GUEST_TEAM = "RequestInfoGuestTeam"

        const val FRAGMENT_SEARCH_GUEST_TEAM = "FragmentSearchGuestTeam"
        const val FRAGMENT_ADD_GUEST_TEAM = "FragmentAddGuestTeam"
        const val FRAGMENT_INFO_GUEST_TEAM = "FragmentAddGuestTeam"
    }
}