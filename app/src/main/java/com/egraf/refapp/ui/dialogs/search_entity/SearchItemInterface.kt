package com.egraf.refapp.ui.dialogs.search_entity

import java.util.*

interface SearchItemInterface {
    val title: String
    val id: UUID
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
fun List<SearchItemInterface>.filter(text: String): List<Triple<FirstMatch, LastMatch, SearchItemInterface>> =
    this.fold<SearchItemInterface, List<Triple<FirstMatch, LastMatch, SearchItemInterface>>>(listOf()) { acc, e ->
        val startIndex = e.title.lowercase().indexOf(text.lowercase())
        if (startIndex == -1) {
            acc
        } else {
            acc + listOf(Triple(startIndex, startIndex + text.length, e))
        }
    }.sortedBy { it.first }

fun List<SearchItemInterface>.toTriple(): List<Triple<FirstMatch, LastMatch, SearchItemInterface>> =
    this.fold(listOf()) { acc: List<Triple<FirstMatch, LastMatch, SearchItemInterface>>, s: SearchItemInterface ->
        acc + listOf(Triple(0, 0, s))
    }

