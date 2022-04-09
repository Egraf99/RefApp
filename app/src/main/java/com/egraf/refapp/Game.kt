package com.egraf.refapp

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Game(
    val oldId: Int = 0,
    @PrimaryKey
    var id: UUID = UUID.randomUUID(),
    var date: Date = Date(),
    var homeTeam: String = "",
    var guestTeam: String = "",
    var stadium: String = "",
    var league: String = "",
    var isPaid: Boolean = false
)
