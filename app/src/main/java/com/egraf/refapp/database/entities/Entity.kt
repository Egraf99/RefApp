package com.egraf.refapp.database.entities

import java.util.*

interface Entity {
    val id: UUID
    val shortName: String
    val fullName: String
    fun setEntityName(text: String): Entity
    override fun equals(other: Any?): Boolean

    companion object {
        object Empty: Entity {
            override val id: UUID = UUID.randomUUID()
            override val shortName: String = ""
            override val fullName: String = ""
            override fun setEntityName(text: String): Entity = this
            override fun equals(other: Any?): Boolean = other?.javaClass == this.javaClass
        }
    }
}