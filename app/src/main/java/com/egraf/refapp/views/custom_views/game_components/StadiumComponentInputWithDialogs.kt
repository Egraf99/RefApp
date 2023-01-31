package com.egraf.refapp.views.custom_views.game_components

import android.content.Context
import android.util.AttributeSet
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentResultListener
import com.egraf.refapp.GameRepository
import com.egraf.refapp.R
import com.egraf.refapp.database.local.entities.Stadium
import com.egraf.refapp.ui.dialogs.entity_add_dialog.stadium.AddStadiumDialogFragment
import com.egraf.refapp.ui.dialogs.entity_add_dialog.stadium.InfoStadiumDialogFragment
import com.egraf.refapp.utils.Resource
import com.egraf.refapp.views.custom_views.GameComponent
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import java.util.*

class StadiumComponentInputWithDialogs(context: Context, attrs: AttributeSet) :
    AbstractComponentInputWithDialogs<Stadium>(
        context,
        attrs,
        REQUEST_SEARCH_STADIUM,
        REQUEST_ADD_STADIUM,
        REQUEST_INFO_STADIUM,
        FRAGMENT_SEARCH_STADIUM,
        FRAGMENT_INFO_STADIUM,
        FRAGMENT_ADD_STADIUM,
        { GameRepository.get().getStadiums() },
        { id, name -> GameComponent(Stadium(id, name)) },
    ), FragmentResultListener {

    override val addDialogFragment: (String) -> DialogFragment =
        { title ->
            AddStadiumDialogFragment(
                title = context.getString(R.string.add_stadium),
                entityTitle = title,
                request = REQUEST_ADD_STADIUM,
                functionSaveEntityInDB = addStadiumToDB
            )
        }

    override val infoDialogFragment = { id: UUID ->
        InfoStadiumDialogFragment(
            title = this.title,
            componentId = id,
            deleteStadiumFunction = { GameRepository.get().deleteStadium(it) },
            request = REQUEST_INFO_STADIUM
        )
    }

    private val addStadiumToDB: (String) -> StateFlow<Resource<Pair<UUID, String>>> =
        { stadiumName ->
            val stadium = Stadium(stadiumName)
            flow {
                try {
                    GameRepository.get().addStadium(stadium)
                    emit(Resource.success(Pair(stadium.id, stadium.name)))
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
        const val REQUEST_SEARCH_STADIUM = "RequestStadium"
        const val REQUEST_ADD_STADIUM = "RequestAddStadium"
        const val REQUEST_INFO_STADIUM = "RequestInfoStadium"

        const val FRAGMENT_SEARCH_STADIUM = "FragmentSearchStadium"
        const val FRAGMENT_ADD_STADIUM = "FragmentAddStadium"
        const val FRAGMENT_INFO_STADIUM = "FragmentAddStadium"
    }
}