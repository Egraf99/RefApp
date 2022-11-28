package com.egraf.refapp.ui.dialogs.search_entity

import com.egraf.refapp.GameRepository
import com.egraf.refapp.database.entities.League
import com.egraf.refapp.database.source.FakeGameDataSource
import com.google.common.truth.Truth.assertThat
import org.junit.Before

import org.junit.Test

class SearchViewModelTest {
    private lateinit var vm: SearchViewModel
    private val l1 = League(name = "Some")
    private val l2 = League(name = "Body")
    private val l3 = League(name = "Was")
    private val l4 = League(name = "Told")
    private val l5 = League(name = "Me")

    @Before
    fun setup() {
        GameRepository.initialize(FakeGameDataSource())
        vm = SearchViewModel()
        vm.items = listOf(l1, l2, l3, l4, l5)
    }

    @Test
    fun filterItems() {
        assertThat(vm.filterItems(l1.name)).isEqualTo(listOf(l1))
    }
}