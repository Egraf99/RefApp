package com.egraf.refapp.ui.dialogs.search_entity

import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.egraf.refapp.ui.ViewModelWithGameRepo
import com.egraf.refapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "SearchViewModel"

class SearchViewModel: ViewModelWithGameRepo() {
    var items = listOf<SearchItem>()
    var filterItems = listOf<Triple<FirstMatch, LastMatch, SearchItem>>()

    var receiveItems: (() -> List<SearchItem>)? = null

    var text: String? = null
    var title: String? = null
    var icon: Drawable? = null

    private val _flowSearchItems =
        MutableStateFlow<Resource<List<SearchItem>>>(Resource.loading())
    val flowSearchItems: StateFlow<Resource<List<SearchItem>>> = _flowSearchItems

    fun startReceiveData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _flowSearchItems.value =
                    if (receiveItems == null) Resource.error("Function to get data is not defined") else {
                        val result = receiveItems!!()
                        Resource.success(result)
                    }
            } catch (e: Exception) {
                _flowSearchItems.value = Resource.error(e)
            }
        }
    }
}