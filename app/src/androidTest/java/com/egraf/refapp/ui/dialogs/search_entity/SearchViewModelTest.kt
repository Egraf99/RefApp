package com.egraf.refapp.ui.dialogs.search_entity

//import com.egraf.refapp.GameRepository
//import com.egraf.refapp.database.local.entities.Stadium
//import com.egraf.refapp.database.local.source.FakeGameDataSource
//import com.google.common.truth.Truth.assertThat
//import org.junit.Before
//
//import org.junit.Test
//
//class SearchViewModelTest {
//    private lateinit var vm: StadiumSearchViewModel
//    private val l1 = Stadium(name = "Some")
//    private val l2 = Stadium(name = "Body")
//    private val l3 = Stadium(name = "Was")
//    private val l4 = Stadium(name = "Told")
//    private val l5 = Stadium(name = "Me")
//
//    @Before
//    fun setup() {
//        GameRepository.initialize(FakeGameDataSource())
//        vm = StadiumSearchViewModel()
//        vm.items = listOf(l1, l2, l3, l4, l5)
//    }
//
//    @Test
//    fun filterItems() {
//        assertThat(vm.filterItems(l1.name)).isEqualTo(listOf(l1))
//    }
//}