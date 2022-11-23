package com.egraf.refapp.dialogs.search_entity

import com.egraf.refapp.database.entities.Entity
import com.egraf.refapp.database.entities.League
import com.egraf.refapp.ui.ViewModelWithGameRepo

private const val TAG = "SearchViewModel"

val getTestList: () -> SearchList<League> = {
    SearchList(
        League(name = "Third"),
        League(name = "Second"),
        League(name = "Third"),
        League(name = "Some"),
        League(name = "Body"),
        League(name = "Was"),
        League(name = "Told"),
    )
}
val getUpdateTestList: () -> SearchList<League> = {
    SearchList(
        League(name = "Update"),
    )
}

class SearchViewModel : ViewModelWithGameRepo() {
    fun listItems(): List<Entity> = getTestList().toList()
    fun shortListItems(): List<Entity> = getUpdateTestList().toList()
}