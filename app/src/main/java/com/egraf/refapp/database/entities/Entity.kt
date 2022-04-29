package com.egraf.refapp.database.entities

interface Entity {
    fun getEntityName(): String?
    fun setEntityName(text: String): Entity
}