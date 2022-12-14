package com.egraf.refapp.ui.dialogs.search_entity

import androidx.lifecycle.liveData
import com.egraf.refapp.database.entities.Entity
import com.egraf.refapp.database.entities.Stadium
import com.egraf.refapp.ui.ViewModelWithGameRepo
import com.egraf.refapp.utils.Resource
import kotlinx.coroutines.Dispatchers

private const val TAG = "SearchViewModel"

abstract class SearchViewModel<E: Entity> : ViewModelWithGameRepo() {
    var items = listOf<E>()
    val itemsTriple = items.fold(listOf()) { acc: List<Triple<Int, Int, E>>, e: E ->
        acc + listOf(Triple(0, 0, e))
    }
    val emptyItemList = listOf<Entity>(Entity.Companion.Empty)

    fun getStadiums() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = gameRepository.getStadiums()))
        } catch (e: Exception) {
            emit(Resource.error(data = null, message = e.message ?: "Unknown error occurred!"))
        }
    }

    /**
     * Фильтрует [items] по заданной [str] и возвращает список List(Triple(start, end, item)), где:
     *      entity - Entity, в name которой присутствует [str];
     *      start - индекс превого совпадения с [str];
     *      end - индукс последнего совпадения с [str];
     */
    fun filterItems(items: List<E>, str: String): List<Triple<Int, Int, E>> =
        items.fold<E, List<Triple<Int, Int, E>>>(listOf()) { acc, e ->
            val startIndex = e.shortName.lowercase().indexOf(str.lowercase())
            if (startIndex == -1) {
                acc
            } else {
                acc + listOf(Triple(startIndex, startIndex + str.length, e))
            }
        }.sortedBy { it.first }
}

class StadiumSearchViewModel: SearchViewModel<Stadium>()