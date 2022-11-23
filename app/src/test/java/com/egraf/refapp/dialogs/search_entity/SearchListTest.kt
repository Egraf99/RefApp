package com.egraf.refapp.dialogs.search_entity

import com.egraf.refapp.database.entities.League
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class SearchListTest {

    // toString()
    @Test
    fun `Empty SearchList toString() should return NIL`() {
        val emptyList = SearchList<League>()
        assertThat(emptyList.toString()).isEqualTo("[NIL]")
    }
    @Test
    fun `Not empty SearchList toString() should return correct string`() {
        val notEmptyList = SearchList(League(name = "Some"), League(name = "Body"))
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
        val l1 = League(name = "Some")
        val l2 = League(name = "Body")
        val esl = SearchList(l1, l2)
        assertThat(esl.toList()).isEqualTo(listOf(l1, l2))
    }
}