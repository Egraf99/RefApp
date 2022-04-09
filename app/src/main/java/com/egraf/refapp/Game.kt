package com.egraf.refapp

import java.util.*

data class Game(
    val oldId: Int = 0,
    var id: UUID = UUID.randomUUID(),
    var date: Date = Date(),
    var homeTeam: String = "",
    var guestTeam: String = "",
    var stadium: String = "",
    var league: String = "",
    var isPaid: Boolean = false
)
