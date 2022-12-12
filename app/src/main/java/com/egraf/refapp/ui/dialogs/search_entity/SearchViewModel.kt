package com.egraf.refapp.ui.dialogs.search_entity

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.egraf.refapp.database.entities.Entity
import com.egraf.refapp.database.entities.Stadium
import com.egraf.refapp.ui.ViewModelWithGameRepo

private const val TAG = "SearchViewModel"

abstract class SearchViewModel<E: Entity> : ViewModelWithGameRepo() {
    var items = listOf<E>()
    val emptyItemList = listOf<Entity>(Entity.Companion.Empty)

    fun liveDataListStadium(): LiveData<List<Stadium>> = gameRepository.getStadiums()
    fun filterItems(str: String): List<E> =
        items.filter { it.shortName.lowercase().contains(str.lowercase()) }
}

class StadiumSearchViewModel: SearchViewModel<Stadium>()