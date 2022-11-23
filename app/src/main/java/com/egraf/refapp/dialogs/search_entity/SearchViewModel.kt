package com.egraf.refapp.dialogs.search_entity

import androidx.lifecycle.ViewModel
import com.egraf.refapp.database.entities.League

class SearchViewModel : ViewModel() {
    val getTestList: () -> List<League> = {
        listOf(
            League(name = "Third"),
            League(name = "Second"),
            League(name = "Third"),
            League(name = "Some"),
            League(name = "Body"),
            League(name = "Was"),
            League(name = "Told"),
        )
    }
    val getUpdateTestList: () -> List<League> = {
        listOf(
            League(name = "Update"),
        )
    }

}