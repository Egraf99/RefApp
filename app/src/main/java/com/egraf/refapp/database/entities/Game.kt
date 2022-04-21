package com.egraf.refapp.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Game(
    @PrimaryKey
    var id: UUID = UUID.randomUUID(),
    var date: Date = Date(),
    var homeTeam: String = "",
    var guestTeam: String = "",
    var stadiumId: UUID? = null,
    var leagueId: UUID? = null,
    var isPaid: Boolean = false,
)
