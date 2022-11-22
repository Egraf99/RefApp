package com.egraf.refapp.database.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity
data class Game(
    @PrimaryKey
    override var id: UUID = UUID.randomUUID(),
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
) : com.egraf.refapp.database.entities.Entity, Parcelable {
    override val shortName: String
        get() = "id=$id homeTeamId=$homeTeamId guestTeamId=$guestTeamId"
    override val fullName: String
        get() =
            "id=$id homeTeamId=$homeTeamId guestTeamId=$guestTeamId stadiumId=$stadiumId leagueId=$leagueId isPaid=$isPaid isPassed=$isPassed chiefRefereeId=$chiefRefereeId firstRefereeId=$firstRefereeId secondRefereeId=$secondRefereeId reserveRefereeId=$reserveRefereeId inspectorId=$inspectorId"

    override fun setEntityName(text: String): Game {
        return this
    }
}
