package com.egraf.refapp.ui.dialogs.search_entity

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.egraf.refapp.database.entities.Entity
import com.egraf.refapp.database.entities.Stadium
import com.egraf.refapp.ui.ViewModelWithGameRepo
import com.egraf.refapp.utils.Resource
import kotlinx.coroutines.Dispatchers

private const val TAG = "SearchViewModel"

abstract class SearchViewModel<E: Entity> : ViewModelWithGameRepo() {
    var items = listOf<E>()
    val emptyItemList = listOf<Entity>(Entity.Companion.Empty)

    fun getStadiums() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = gameRepository.getStadiums()))
        } catch (e: Exception) {
            emit(Resource.error(data = null, message = e.message ?: "Unknown error occurred!"))
        }
    }
    fun filterItems(items: List<E>, str: String): List<E> {
        this.items = items
        return filterItems(str)
    }

    fun filterItems(str: String): List<E> =
        items.filter { it.shortName.lowercase().contains(str.lowercase()) }
}

class StadiumSearchViewModel: SearchViewModel<Stadium>()