package com.egraf.refapp.database.local.entities

import android.os.Bundle
import androidx.room.Embedded
import androidx.room.Relation
import com.egraf.refapp.utils.*

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

    fun toBundle(): Bundle = Bundle().apply {
        this.putPaid(game.isPaid)
        this.putPassed(game.isPassed)
        this.putDate(game.dateTime.date)
        this.putTime(game.dateTime.time)
        this.putStadium(stadium)
        this.putLeague(league)
        this.putHomeTeam(homeTeam)
        this.putGuestTeam(guestTeam)
        this.putChiefReferee(chiefReferee)
        this.putFirstAssistant(firstReferee)
        this.putSecondAssistant(secondReferee)
        this.putReserveReferee(reserveReferee)
        this.putInspector(inspector)
    }
}