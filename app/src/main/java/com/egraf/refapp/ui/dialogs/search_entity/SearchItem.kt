package com.egraf.refapp.ui.dialogs.search_entity

import com.egraf.refapp.views.custom_views.Saving
import java.util.*

interface SearchItem {
    val title: String
    val description: String
        get() = TODO()
    val id: UUID

    companion object {
        operator fun invoke(title: String, id: UUID): SearchItem = when (id) {
            EmptyItem.id -> EmptyItem
            else -> object : SearchItem {
                override val title: String = title
                override val id: UUID = id
            }
        }

        operator fun invoke(title: String): SearchItem {
            var id: UUID
            do {
                id = UUID.randomUUID()
            } while (id == EmptyItem.id)
            return object : SearchItem {
                override val title: String = title
                override val id: UUID = id
            }
        }
    }
}

object EmptyItem : SearchItem, Saving<UUID> {
    override val id: UUID = UUID.randomUUID()
    override val savedValue: UUID = id
    override val title: String = "Empty Search Item"
    override fun toString(): String = "EmptyItem"
}

/** Первое совпадение при фильтрации **/
typealias FirstMatch = Int
/** Последнее совпадение при фильтрации **/
typealias LastMatch = Int

/**
 * Фильтрует по заданному [text] и возвращает список List(Triple(start, end, item)), где:
 *      entity - Entity, в name которой присутствует [str];
 *      start - индекс превого совпадения с [str];
 *      end - индекс последнего совпадения с [str];
 */
fun List<SearchItem>.filter(text: String): List<Triple<FirstMatch, LastMatch, SearchItem>> =
    this.fold<SearchItem, List<Triple<FirstMatch, LastMatch, SearchItem>>>(listOf()) { acc, e ->
        val startIndex = e.title.lowercase().indexOf(text.lowercase())
        if (startIndex == -1) {
            acc
        } else {
            acc + listOf(Triple(startIndex, startIndex + text.length, e))
        }
    }.sortedBy { it.first }