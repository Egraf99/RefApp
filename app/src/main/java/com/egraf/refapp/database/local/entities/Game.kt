package com.egraf.refapp.database.local.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity
data class Game(
    @PrimaryKey
    var id: UUID = UUID.randomUUID(),
    @ColumnInfo(name = "date")
    var dateTime: GameDateTime = GameDateTime(GameDate(), GameTime()),
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
) : Parcelable
