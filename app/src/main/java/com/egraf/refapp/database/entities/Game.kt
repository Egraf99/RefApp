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
) : com.egraf.refapp.database.entities.Entity {
    override val shortName: String
        get() = "id=$id homeTeamId=$homeTeamId guestTeamId=$guestTeamId"
    override val fullName: String
        get() =
            "id=$id homeTeamId=$homeTeamId guestTeamId=$guestTeamId stadiumId=$stadiumId leagueId=$leagueId isPaid=$isPaid isPassed=$isPassed chiefRefereeId=$chiefRefereeId firstRefereeId=$firstRefereeId secondRefereeId=$secondRefereeId reserveRefereeId=$reserveRefereeId inspectorId=$inspectorId"

    override fun setEntityName(text: String): Game {
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Game) return false
        return this.id == other.id &&
                this.homeTeamId == other.homeTeamId &&
                this.guestTeamId == other.guestTeamId &&
                this.stadiumId == other.stadiumId &&
                this.leagueId == other.leagueId &&
                this.isPaid == other.isPaid &&
                this.isPassed == other.isPassed &&
                this.chiefRefereeId == other.chiefRefereeId &&
                this.firstRefereeId == other.firstRefereeId &&
                this.secondRefereeId == other.secondRefereeId &&
                this.reserveRefereeId == other.reserveRefereeId &&
                this.inspectorId == other.inspectorId
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + date.hashCode()
        result = 31 * result + (homeTeamId?.hashCode() ?: 0)
        result = 31 * result + (guestTeamId?.hashCode() ?: 0)
        result = 31 * result + (stadiumId?.hashCode() ?: 0)
        result = 31 * result + (leagueId?.hashCode() ?: 0)
        result = 31 * result + isPaid.hashCode()
        result = 31 * result + isPassed.hashCode()
        result = 31 * result + (chiefRefereeId?.hashCode() ?: 0)
        result = 31 * result + (firstRefereeId?.hashCode() ?: 0)
        result = 31 * result + (secondRefereeId?.hashCode() ?: 0)
        result = 31 * result + (reserveRefereeId?.hashCode() ?: 0)
        result = 31 * result + (inspectorId?.hashCode() ?: 0)
        return result
    }
}
