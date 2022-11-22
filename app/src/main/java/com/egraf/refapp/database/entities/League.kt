package com.egraf.refapp.database.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity
data class League(
    @PrimaryKey
    override var id: UUID = UUID.randomUUID(),
    var name: String = "",
): com.egraf.refapp.database.entities.Entity, Parcelable {
    override val shortName: String
        get() = name
    override val fullName: String
        get() = name

    override fun setEntityName(text: String): League {
        return this.apply { name = text.trim() }
    }
}