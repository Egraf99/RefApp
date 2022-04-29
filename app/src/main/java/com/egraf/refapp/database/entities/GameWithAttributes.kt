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
    @Relation(parentColumn = "homeTeamId", entityColumn = "id")
    var homeTeam: Team? = null,
    @Relation(parentColumn = "guestTeamId", entityColumn = "id")
    var guestTeam: Team? = null,
    @Relation(parentColumn = "chiefRefereeId", entityColumn = "id")
    var chiefReferee: Referee? = null,
    @Relation(parentColumn = "firstRefereeId", entityColumn = "id")
    var firstReferee: Referee? = null,
    @Relation(parentColumn = "secondRefereeId", entityColumn = "id")
    var secondReferee: Referee? = null,
    @Relation(parentColumn = "reserveRefereeId", entityColumn = "id")
    var reserveReferee: Referee? = null,

) {
    override fun toString(): String {
        return "\nGame id is: ${game.id}" +
                "\nStadium name: ${stadium?.name}, id is: ${stadium?.id}" +
                "\nLeague name: ${league?.name}, id is: ${league?.id}" +
                "\nTeamHome name: ${homeTeam?.name}, id is: ${homeTeam?.id}" +
                "\nTeamGuest name: ${guestTeam?.name}, id is: ${guestTeam?.id}" +
                "\nChiefReferee name: ${chiefReferee?.secondName}, id is: ${chiefReferee?.id}" +
                "\nFirstReferee name: ${firstReferee?.secondName}, id is: ${firstReferee?.id}"
    }
}