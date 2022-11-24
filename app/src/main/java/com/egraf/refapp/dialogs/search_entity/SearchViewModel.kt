package com.egraf.refapp.dialogs.search_entity

import androidx.lifecycle.LiveData
import com.egraf.refapp.database.entities.Entity
import com.egraf.refapp.database.entities.Stadium
import com.egraf.refapp.ui.ViewModelWithGameRepo

private const val TAG = "SearchViewModel"

class SearchViewModel : ViewModelWithGameRepo() {
    var items = listOf<Entity>()
    val emptyItemList = listOf<Entity>(Entity.Companion.Empty)

    fun liveDataListStadium(): LiveData<List<Stadium>> = gameRepository.getStadiums()
    fun filterItems(str: String): List<Entity> =
        items.filter { it.shortName.lowercase().contains(str.lowercase()) }
}