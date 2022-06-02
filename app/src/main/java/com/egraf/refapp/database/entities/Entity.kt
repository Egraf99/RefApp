package com.egraf.refapp.database.entities

interface Entity {
    val shortName: String
    val fullName: String
    fun setEntityName(text: String): Entity
}