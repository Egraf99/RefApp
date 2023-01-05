package com.egraf.refapp.ui.game_list

//import androidx.arch.core.executor.testing.InstantTaskExecutorRule
//import com.egraf.refapp.GameRepository
//import com.egraf.refapp.database.entities.Team
//import com.egraf.refapp.database.source.FakeGameDataSource
//import com.egraf.refapp.getOrAwaitValue
//import com.google.common.truth.Truth.assertThat
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//
//class GameListViewModelTest {
//    private lateinit var viewModel: GameListViewModel
//
//    @get:Rule
//    val instantTaskExecutorRule = InstantTaskExecutorRule()
//
//    @Before
//    fun setup() {
//        GameRepository.initialize(FakeGameDataSource())
//        viewModel = GameListViewModel()
//    }
//
//    @Test
//    fun `getFive() should return 5`() {
//        val five = viewModel.getFive()
//        assertThat(five).isEqualTo(5)
//    }
//
//    @Test
//    fun `addTeam() should add receive team to DB`() {
//        val team = Team(name="Тест")
//        viewModel.addTeam(team)
//
//        val fakeTeam = viewModel.teamsListLiveData.getOrAwaitValue()
//
//        assertThat(fakeTeam).contains(team)
//    }
//}