package com.egraf.refapp.dialogs.search_entity

import androidx.lifecycle.LiveData
import com.egraf.refapp.database.entities.Stadium
import com.egraf.refapp.ui.ViewModelWithGameRepo
import java.text.FieldPosition

private const val TAG = "SearchViewModel"

internal fun List<SearchItem>.addNewItem(): List<SearchItem> = listOf(SearchItem.AddNewItem) + this

class SearchViewModel : ViewModelWithGameRepo() {
    private var mItems = listOf<SearchItem>()

    fun liveDataListStadium(): LiveData<List<Stadium>> = gameRepository.getStadiums()
    fun toSearchItem(items: List<Stadium>): List<SearchItem> =
        items.fold(listOf()) { acc, stadium ->
            acc + listOf(SearchItem.EntityItem(stadium.shortName))
        }

    fun filterItems(str: String): List<SearchItem> =
        mItems.filter {
            when (it) {
                is SearchItem.AddNewItem -> true
                is SearchItem.EntityItem -> it.shortName.lowercase().contains(str.lowercase())
            }
        }

    fun setItems(items: List<SearchItem>) {
        mItems = items
    }
}