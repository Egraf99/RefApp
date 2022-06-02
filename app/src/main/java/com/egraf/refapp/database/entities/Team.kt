package com.egraf.refapp.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Team(
    @PrimaryKey
    var id: UUID = UUID.randomUUID(),
    var name: String = ""
): com.egraf.refapp.database.entities.Entity {
    override val shortName: String
        get() = name
    override val fullName: String
        get() = name

    override fun setEntityName(text: String): Team {
        return this.apply { name = text.trim() }
    }
}
