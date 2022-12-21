package com.egraf.refapp.ui.dialogs.search_entity

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.egraf.refapp.ui.ViewModelWithGameRepo
import com.egraf.refapp.utils.Resource
import kotlinx.coroutines.Dispatchers

private const val TAG = "SearchViewModel"

class SearchViewModel: ViewModelWithGameRepo() {
    var items = listOf<SearchItemInterface>()
    lateinit var searchComponent: SearchComponent

    // listeners
    var onSearchItemClickListener: OnSearchItemClickListener? = null
    var onInfoClickListener: OnInfoClickListener? = null
    var onAddClickListener: OnAddClickListener? = null
}

//class SearchViewModelFactory(private val searchInterface: SearchInterface) :
//    ViewModelProvider.Factory {
//    @Suppress("UNCHECKED_CAST")
//    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
//        modelClass.isAssignableFrom(SearchViewModel::class.java) -> SearchViewModel(searchInterface) as T
//        else -> throw IllegalStateException("Unknown ViewModel class")
//    }
//}
