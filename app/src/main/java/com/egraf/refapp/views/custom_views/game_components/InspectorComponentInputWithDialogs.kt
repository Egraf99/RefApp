package com.egraf.refapp.views.custom_views.game_components

import android.content.Context
import android.util.AttributeSet
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentResultListener
import com.egraf.refapp.GameRepository
import com.egraf.refapp.R
import com.egraf.refapp.database.local.entities.Referee
import com.egraf.refapp.ui.dialogs.entity_add_dialog.referee.AddRefereeDialogFragment
import com.egraf.refapp.ui.dialogs.entity_add_dialog.referee.InfoRefereeDialogFragment
import com.egraf.refapp.ui.dialogs.search_entity.SearchItem
import com.egraf.refapp.utils.Resource
import com.egraf.refapp.views.custom_views.GameComponent
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import java.util.*

class InspectorComponentInputWithDialogs(context: Context, attrs: AttributeSet) :
    AbstractComponentInputWithDialogs<Referee>(
        context,
        attrs,
        REQUEST_SEARCH_INSPECTOR,
        REQUEST_ADD_INSPECTOR,
        REQUEST_INFO_INSPECTOR,
        FRAGMENT_SEARCH_INSPECTOR,
        FRAGMENT_INFO_INSPECTOR,
        FRAGMENT_ADD_INSPECTOR,
        { GameRepository.get().getReferees() },
        { id, name -> GameComponent(Referee(id, name)) },
    ), FragmentResultListener {

    override val addDialogFragment: (String) -> DialogFragment =
        { title ->
            AddRefereeDialogFragment(
                title = context.getString(R.string.add_referee),
                referee = Referee(title, SearchItem.randomId(), bySpace = true),
                request = REQUEST_ADD_INSPECTOR,
                functionSaveEntityInDB = addRefereeToDB
            )
        }

    override val infoDialogFragment = { id: UUID ->
        InfoRefereeDialogFragment(
            title = this.title,
            componentId = id,
            deleteRefereeFunction = { GameRepository.get().deleteReferee(it) },
            request = REQUEST_INFO_INSPECTOR
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
        const val REQUEST_SEARCH_INSPECTOR = "RequestInspector"
        const val REQUEST_ADD_INSPECTOR = "RequestAddInspector"
        const val REQUEST_INFO_INSPECTOR = "RequestInfoInspector"

        const val FRAGMENT_SEARCH_INSPECTOR = "FragmentSearchInspector"
        const val FRAGMENT_ADD_INSPECTOR = "FragmentAddInspector"
        const val FRAGMENT_INFO_INSPECTOR = "FragmentAddInspector"
    }
}