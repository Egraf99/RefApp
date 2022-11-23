package com.egraf.refapp.dialogs.search_entity

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.egraf.refapp.database.entities.Entity

private const val TAG = "SearchViewModel"

class SearchViewModel(private val items: SearchList<Entity>) : ViewModel() {
    fun listItems(): List<Entity> = items.toList()
}

class SearchViewModelFactory(private val items: SearchList<Entity>): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(items) as T
        }
        throw IllegalArgumentException("ViewModel class not found")
    }

}