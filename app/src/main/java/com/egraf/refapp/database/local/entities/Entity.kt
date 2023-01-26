package com.egraf.refapp.database.local.entities

import com.egraf.refapp.ui.dialogs.search_entity.EmptyItem
import com.egraf.refapp.ui.dialogs.search_entity.SearchItem
import com.egraf.refapp.views.custom_views.Saving
import java.util.*

abstract class Entity : SearchItem, Saving<UUID> {
    abstract override val id: UUID
    val isEmpty
        get() = id == EmptyItem.id
    override val savedValue: UUID
        get() = id
    abstract val shortName: String
    abstract val fullName: String
    abstract fun setEntityName(text: String): Entity
    fun getName(default: String = ""): String = if (isEmpty) default else shortName

    companion object {
        object Empty : Entity() {
            override val id: UUID = UUID.randomUUID()
            override val title: String = ""
            override val shortName: String = ""
            override val fullName: String = ""
            override fun setEntityName(text: String): Entity = this
            override fun equals(other: Any?): Boolean = other?.javaClass == this.javaClass
        }
    }
}