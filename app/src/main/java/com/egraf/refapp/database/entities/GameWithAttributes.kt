package com.egraf.refapp.database.entities

import androidx.room.Embedded
import androidx.room.Relation

class GameWithAttributes(
    @Embedded
    var game: Game,
    @Relation(parentColumn = "stadiumId", entityColumn = "id")
    var stadium: Stadium? = null,
    @Relation(parentColumn = "leagueId", entityColumn = "id")
    var league: League? = null,
) {
    override fun toString(): String {
        return "\nGame id is: ${game.id}" +
                "\nStadium name: ${stadium?.name}, id is: ${stadium?.id}" +
                "\nLeague name: ${league?.name}, id is: ${league?.id}"
    }
}