package com.egraf.refapp.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Game(
    @PrimaryKey
    var id: UUID = UUID.randomUUID(),
    var date: Date = Date(),
    var homeTeamId: UUID? = null,
    var guestTeamId: UUID? = null,
    var stadiumId: UUID? = null,
    var leagueId: UUID? = null,
    var isPaid: Boolean = false,
    var isPassed: Boolean = false,
    var chiefRefereeId: UUID? = null,
    var firstRefereeId: UUID? = null,
    var secondRefereeId: UUID? = null,
    var reserveRefereeId: UUID? = null,
    var inspectorId: UUID? = null
): com.egraf.refapp.database.entities.Entity {
    override fun getEntityName(): String {
        return "$id $homeTeamId $guestTeamId $stadiumId $leagueId"
    }

    override fun setEntityName(text: String): Game {
        return this
    }
}
