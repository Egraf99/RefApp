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
) : com.egraf.refapp.database.entities.Entity {
    override fun getEntityName(): String {
        return "$secondName $firstName"
    }

    override fun setEntityName(text: String): Referee {
        val fullName = text.trim().split(" ")
        if (fullName.isNotEmpty()) secondName = fullName[0]
        if (fullName.size > 1) firstName = fullName[1]
        if (fullName.size > 2) thirdName = fullName[2]

        return this
    }
}