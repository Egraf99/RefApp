package com.egraf.refapp.database.entities

import com.egraf.refapp.ui.dialogs.search_entity.SearchItemInterface
import java.util.*

abstract class Entity: SearchItemInterface {
    abstract override val id: UUID
    abstract val shortName: String
    abstract val fullName: String
    abstract fun setEntityName(text: String): Entity

    companion object {
        object Empty: Entity() {
            override val id: UUID = UUID.randomUUID()
            override val title: String = ""
            override val shortName: String = ""
            override val fullName: String = ""
            override fun setEntityName(text: String): Entity = this
            override fun equals(other: Any?): Boolean = other?.javaClass == this.javaClass
        }
    }
}