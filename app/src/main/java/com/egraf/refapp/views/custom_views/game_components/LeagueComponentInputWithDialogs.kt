package com.egraf.refapp.views.custom_views.game_components

import android.content.Context
import android.util.AttributeSet
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentResultListener
import com.egraf.refapp.GameRepository
import com.egraf.refapp.R
import com.egraf.refapp.database.local.entities.League
import com.egraf.refapp.ui.dialogs.entity_add_dialog.league.AddLeagueDialogFragment
import com.egraf.refapp.ui.dialogs.entity_info_dialog.league.InfoLeagueDialogFragment
import com.egraf.refapp.utils.Resource
import com.egraf.refapp.views.custom_views.GameComponent
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import java.util.*

class LeagueComponentInputWithDialogs(context: Context, attrs: AttributeSet) :
    AbstractComponentInputWithDialogs<League>(
        context,
        attrs,
        REQUEST_SEARCH_LEAGUE,
        REQUEST_ADD_LEAGUE,
        REQUEST_INFO_LEAGUE,
        FRAGMENT_SEARCH_LEAGUE,
        FRAGMENT_INFO_LEAGUE,
        FRAGMENT_ADD_LEAGUE,
        { GameRepository.get().getLeagues() },
        { id, name -> GameComponent(League(id, name)) },
    ), FragmentResultListener {

    override val addDialogFragment: (String) -> DialogFragment =
        { title ->
            AddLeagueDialogFragment(
                title = context.getString(R.string.add_league),
                entityTitle = title,
                request = REQUEST_ADD_LEAGUE,
                functionSaveEntityInDB = addLeagueToDB
            )
        }

    override val infoDialogFragment = { id: UUID ->
        InfoLeagueDialogFragment(
            title = context.getString(R.string.league),
            componentId = id,
            deleteFunction = { GameRepository.get().deleteLeague(it) },
            request = REQUEST_INFO_LEAGUE
        )
    }

    private val addLeagueToDB: (String) -> StateFlow<Resource<Pair<UUID, String>>> =
        { leagueName ->
            val league = League(leagueName)
            flow {
                try {
                    GameRepository.get().addLeague(league)
                    emit(Resource.success(Pair(league.id, league.name)))
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
        const val REQUEST_SEARCH_LEAGUE = "RequestLeague"
        const val REQUEST_ADD_LEAGUE = "RequestAddLeague"
        const val REQUEST_INFO_LEAGUE = "RequestInfoLeague"

        const val FRAGMENT_SEARCH_LEAGUE = "FragmentSearchLeague"
        const val FRAGMENT_ADD_LEAGUE = "FragmentAddLeague"
        const val FRAGMENT_INFO_LEAGUE = "FragmentAddLeague"
    }
}