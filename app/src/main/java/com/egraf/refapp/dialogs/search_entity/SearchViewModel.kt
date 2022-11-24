package com.egraf.refapp.dialogs.search_entity

import androidx.lifecycle.LiveData
import com.egraf.refapp.database.entities.Stadium
import com.egraf.refapp.ui.ViewModelWithGameRepo

private const val TAG = "SearchViewModel"

class SearchViewModel : ViewModelWithGameRepo() {
    private var mItems = SearchList<Stadium>()

    fun liveDataListStadium(): LiveData<List<Stadium>> = gameRepository.getStadiums()
    fun filterItems(str: String): SearchList<Stadium> =
        mItems.filter { it.shortName.lowercase().contains(str.lowercase()) }

    fun setItems(items: List<Stadium>) {
        mItems = items.toSearchList()
    }
}