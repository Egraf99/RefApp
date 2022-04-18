package com.egraf.refapp.database.entities

import androidx.room.*
import androidx.room.ForeignKey.RESTRICT
import androidx.room.ForeignKey.SET_NULL
import java.util.*

@Entity
data class Game(
    @PrimaryKey
    var id: UUID = UUID.randomUUID(),
    var date: Date = Date(),
    var homeTeam: String = "",
    var guestTeam: String = "",
    var stadiumId: UUID = UUID.randomUUID(),
    var league: String = "",
    var isPaid: Boolean = false,
)
