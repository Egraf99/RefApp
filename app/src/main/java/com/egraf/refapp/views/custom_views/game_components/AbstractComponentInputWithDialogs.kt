package com.egraf.refapp.views.custom_views.game_components

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.LifecycleOwner
import com.egraf.refapp.ui.dialogs.entity_add_dialog.team.AddTeamDialogFragment
import com.egraf.refapp.ui.dialogs.search_entity.EmptyItem
import com.egraf.refapp.ui.dialogs.search_entity.SearchDialogFragment
import com.egraf.refapp.ui.dialogs.search_entity.SearchItem
import com.egraf.refapp.utils.close
import com.egraf.refapp.views.custom_views.GameComponent
import com.egraf.refapp.views.custom_views.GameComponentViewWithIcon
import com.egraf.refapp.views.custom_views.Saving
import kotlinx.coroutines.CoroutineScope
import java.util.*

interface Component : Saving<UUID>, SearchItem

abstract class AbstractComponentInputWithDialogs<C : Component>(
    context: Context,
    attrs: AttributeSet,
    private val requestSearch: String,
    private val requestAdd: String,
    private val requestInfo: String,
    private val fragmentSearchTag: String,
    private val fragmentInfoTag: String,
    private val fragmentAddTag: String,
    getComponent: () -> List<C>,
    private val createComponent: (UUID, String) -> GameComponent<UUID, C>,
) : GameComponentViewWithIcon<UUID, C>(context, attrs),
    FragmentResultListener {
    private lateinit var fragmentManager: FragmentManager
    protected lateinit var coroutineScope: CoroutineScope
    protected lateinit var onUpdateItem: (UUID) -> Unit
    private val searchDialogFragment =
        SearchDialogFragment(
            this.title, this.icon,
            receiveSearchItems = getComponent,
            request = requestSearch
        )

    abstract val infoDialogFragment: (UUID) -> DialogFragment
    abstract val addDialogFragment: (String) -> DialogFragment

    fun bind(
        fragmentManager: FragmentManager,
        lifecycleOwner: LifecycleOwner,
        coroutineScope: CoroutineScope,
        onUpdateItem: (UUID) -> Unit = {},
    ) {
        this.fragmentManager = fragmentManager
        this.coroutineScope = coroutineScope
        this.onUpdateItem = onUpdateItem
        for (request in listOf(
            requestSearch, requestAdd, requestInfo
        )) {
            this.fragmentManager.setFragmentResultListener(
                request, lifecycleOwner, this
            )
        }
        this.setOnClickListener {
            searchDialogFragment.show(
                this.fragmentManager,
                fragmentSearchTag
            )
        }
        this.setOnInfoClickListener {
            infoDialogFragment(this.item.getOrThrow(IllegalStateException("Info button shouldn't be able when GameComponentView don't have item")).savedValue)
                .show(this.fragmentManager, fragmentInfoTag)
        }
    }


    override fun onFragmentResult(requestKey: String, result: Bundle) {
        when (requestKey) {
            requestSearch -> {
                val item = createComponent(
                    SearchDialogFragment.getId(result),
                    SearchDialogFragment.getTitle(result),
                ).filter { it.id != EmptyItem.id }
                when (SearchDialogFragment.getTypeOfResult(result)) {
                    SearchDialogFragment.Companion.ResultRequest.SEARCH_ITEM_RESULT_REQUEST -> {
                        this.item = item
                        searchDialogFragment.dismiss()
                        onUpdateItem(SearchDialogFragment.getId(result))
                    }
                    SearchDialogFragment.Companion.ResultRequest.INFO_RESULT_REQUEST -> {
                        infoDialogFragment(SearchDialogFragment.getId(result))
                            .show(fragmentManager, fragmentInfoTag)
                    }
                    SearchDialogFragment.Companion.ResultRequest.ADD_RESULT_REQUEST -> {
                        addDialogFragment(SearchDialogFragment.getTitle(result))
                            .show(fragmentManager, fragmentAddTag)
                    }
                }
            }
            requestAdd -> {
                fragmentManager.close(fragmentSearchTag, fragmentAddTag)
                this.item = createComponent(
                    AddTeamDialogFragment.getId(result),
                    AddTeamDialogFragment.getTitle(result),
                )

            }
            requestInfo -> { // удаление
                fragmentManager.close(fragmentInfoTag)
                val searchFragment = fragmentManager.findFragmentByTag(
                    fragmentSearchTag
                ) as SearchDialogFragment?
                searchFragment?.updateRecycleViewItems()
                this.item = GameComponent()
            }
        }
    }
}