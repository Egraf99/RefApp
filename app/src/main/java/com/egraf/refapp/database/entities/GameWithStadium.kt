package com.egraf.refapp.database.entities

import androidx.room.Embedded
import androidx.room.Relation

class GameWithStadium(
    @Embedded
    var game: Game,
    @Relation(parentColumn = "stadiumId", entityColumn = "id")
    var stadium: Stadium
)