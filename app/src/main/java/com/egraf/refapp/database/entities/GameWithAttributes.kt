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
    @Relation(parentColumn = "inspectorId", entityColumn = "id")
    var inspector: Referee? = null,

) {
    override fun toString(): String {
        return "GWA: game id is: ${game.id}"
//                   +
//                " Stadium name: ${stadium?.name}, id is: ${stadium?.id}" +
//                " League name: ${league?.name}, id is: ${league?.id}" +
//                " TeamHome name: ${homeTeam?.name}, id is: ${homeTeam?.id}" +
//                " TeamGuest name: ${guestTeam?.name}, id is: ${guestTeam?.id}" +
//                " ChiefReferee name: ${chiefReferee?.middleName}, id is: ${chiefReferee?.id}" +
//                " FirstReferee name: ${firstReferee?.middleName}, id is: ${firstReferee?.id}" +
//                " isPaid: ${game.isPaid}" +
//                " isPassed: ${game.isPassed}"
    }
}