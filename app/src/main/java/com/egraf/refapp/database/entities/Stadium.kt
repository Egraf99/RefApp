package com.egraf.refapp.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Stadium(
    @PrimaryKey
    var id: UUID = UUID.randomUUID(),
    var name: String = ""
)
