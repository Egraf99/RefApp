package com.egraf.refapp.ui.dialogs.search_entity

import android.graphics.drawable.Drawable
import androidx.lifecycle.liveData
import com.egraf.refapp.ui.ViewModelWithGameRepo
import com.egraf.refapp.utils.Resource
import kotlinx.coroutines.Dispatchers

private const val TAG = "SearchViewModel"

class SearchViewModel: ViewModelWithGameRepo() {
    var items = listOf<SearchItemInterface>()
    var filterItems = listOf<Triple<FirstMatch, LastMatch, SearchItemInterface>>()

    var text: String? = null
    var title: String? = null
    var icon: Drawable? = null
    val liveDataReceiveItems = liveData(Dispatchers.IO) {
        if (receiveItems == null) {
            emit(Resource.error(null, "Function to get data is not defined"))
            return@liveData
        }

        emit(Resource.loading(null))
        try {
            emit(Resource.success(receiveItems!!()))
        } catch (e: Exception) {
            emit(
                Resource.error(
                    data = null,
                    message = e.message ?: "Unknown error occurred trying get data"
                )
            )
        }
    }
    var receiveItems: (() -> List<SearchItemInterface>)? = null

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
