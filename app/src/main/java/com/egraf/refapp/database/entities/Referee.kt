package com.egraf.refapp.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Referee(
    @PrimaryKey
    var id: UUID = UUID.randomUUID(),
    var firstName: String = "",
    var secondName: String = "",
    var thirdName: String = "",
): com.egraf.refapp.database.entities.Entity {
    override fun getEntityName(): String {
        return "$secondName $firstName"
    }
}