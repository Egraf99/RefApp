package com.egraf.refapp.ui.dialogs.search_entity

import com.egraf.refapp.database.entities.League
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class SearchListTest {
    private lateinit var l1: League
    private lateinit var l2: League
    private lateinit var l3: League

    @Before
    fun setup() {
        l1 = League(name = "Some")
        l2 = League(name = "Body")
        l3 = League(name = "Was")
    }

    // toString()
    @Test
    fun `Empty SearchList toString() should return NIL`() {
        val emptyList = SearchList<League>()
        assertThat(emptyList.toString()).isEqualTo("[NIL]")
    }
    @Test
    fun `Not empty SearchList toString() should return correct string`() {
        val notEmptyList = SearchList(l1, l2)
        assertThat(notEmptyList.toString()).isEqualTo("Some, Body, [NIL]")
    }

    // toList()
    @Test
    fun `Empty SearchList toList() should return empty list`() {
        val esl = SearchList<League>()
        assertThat(esl.toList()).isEqualTo(listOf<League>())
    }
    @Test
    fun `Not empty SearchList toList() should return list with same elements`() {
        val esl = SearchList(l1, l2)
        assertThat(esl.toList()).isEqualTo(listOf(l1, l2))
    }

    // List.toSearchList()
    @Test
    fun `List_toSearchList() should return SearchList with same elements`() {
        val ls = listOf(l1, l2)
        assertThat(ls.toSearchList()).isEqualTo(SearchList(l1, l2))
    }

    // equals()
    @Test
    fun `equals() should return true if all elements are same`() {
        val sl1 = SearchList(l1, l2)
        val sl2 = SearchList(l1, l2)
        assertThat(sl1 == sl2).isTrue()
    }
    @Test
    fun `equals() should return false if one sList are shorter than other`() {
        val sl1 = SearchList(l1)
        val sl2 = SearchList(l1, l2)
        assertThat(sl1 == sl2).isFalse()
    }
    @Test
    fun `equals() should return false if not all elements are same`() {
        val sl1 = SearchList(l1, l3)
        val sl2 = SearchList(l1, l2)
        assertThat(sl1 == sl2).isFalse()
    }
    @Test
    fun `equals() should return false if one sList is empty`() {
        val sl1 = SearchList<League>()
        val sl2 = SearchList(l1, l2)
        assertThat(sl1 == sl2).isFalse()
    }

    // filter()
    @Test
    fun `sList_filter() should return filtering sList by predicate`() {
        val sl = SearchList(l1, l2, l3)
        val slFilter = sl.filter { it.name != "Some" }
        val slCheck = SearchList(l2, l3)
        assertThat(slFilter).isEqualTo(slCheck)
    }
}