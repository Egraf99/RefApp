package com.egraf.refapp.views.custom_views.game_components

import android.content.Context
import android.util.AttributeSet
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentResultListener
import com.egraf.refapp.GameRepository
import com.egraf.refapp.R
import com.egraf.refapp.database.local.entities.Referee
import com.egraf.refapp.ui.dialogs.entity_add_dialog.referee.AddRefereeDialogFragment
import com.egraf.refapp.ui.dialogs.entity_info_dialog.referee.InfoRefereeDialogFragment
import com.egraf.refapp.ui.dialogs.search_entity.SearchItem
import com.egraf.refapp.utils.Resource
import com.egraf.refapp.views.custom_views.GameComponent
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import java.util.*

class ReserveRefereeComponentInputWithDialogs(context: Context, attrs: AttributeSet) :
    AbstractComponentInputWithDialogs<Referee>(
        context,
        attrs,
        REQUEST_SEARCH_RESERVE_REFEREE,
        REQUEST_ADD_RESERVE_REFEREE,
        REQUEST_INFO_RESERVE_REFEREE,
        FRAGMENT_SEARCH_RESERVE_REFEREE,
        FRAGMENT_INFO_RESERVE_REFEREE,
        FRAGMENT_ADD_RESERVE_REFEREE,
        { GameRepository.get().getReferees() },
        { id, name -> GameComponent(Referee(id, name)) },
    ), FragmentResultListener {

    override val addDialogFragment: (String) -> DialogFragment =
        { title ->
            AddRefereeDialogFragment(
                title = context.getString(R.string.add_referee),
                referee = Referee(title, SearchItem.randomId(), bySpace = true),
                request = REQUEST_ADD_RESERVE_REFEREE,
                functionSaveEntityInDB = addRefereeToDB
            )
        }

    override val infoDialogFragment = { id: UUID ->
        InfoRefereeDialogFragment(
            title = this.title,
            componentId = id,
            deleteRefereeFunction = { GameRepository.get().deleteReferee(it) },
            request = REQUEST_INFO_RESERVE_REFEREE
        )
    }

    private val addRefereeToDB: (Referee) -> StateFlow<Resource<Pair<UUID, String>>> =
        { referee ->
            flow {
                try {
                    GameRepository.get().addReferee(referee)
                    emit(Resource.success(Pair(referee.id, referee.shortName)))
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
        const val REQUEST_SEARCH_RESERVE_REFEREE = "RequestReserveReferee"
        const val REQUEST_ADD_RESERVE_REFEREE = "RequestAddReserveReferee"
        const val REQUEST_INFO_RESERVE_REFEREE = "RequestInfoReserveReferee"

        const val FRAGMENT_SEARCH_RESERVE_REFEREE = "FragmentSearchReserveReferee"
        const val FRAGMENT_ADD_RESERVE_REFEREE = "FragmentAddReserveReferee"
        const val FRAGMENT_INFO_RESERVE_REFEREE = "FragmentAddReserveReferee"
    }
}