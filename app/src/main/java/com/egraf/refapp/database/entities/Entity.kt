package com.egraf.refapp.database.entities

import java.util.*

interface Entity {
    val id: UUID
    val shortName: String
    val fullName: String
    fun setEntityName(text: String): Entity
    override fun equals(other: Any?): Boolean
}