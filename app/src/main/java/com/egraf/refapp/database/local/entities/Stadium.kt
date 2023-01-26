package com.egraf.refapp.database.local.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.egraf.refapp.ui.dialogs.search_entity.EmptyItem
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity
data class Stadium(
    @PrimaryKey
    override var id: UUID = EmptyItem.id,
    var name: String = "EmptyStadium"
) : com.egraf.refapp.database.local.entities.Entity(), Parcelable {
    override val shortName: String
        get() = name
    override val fullName: String
        get() = name
    override val title: String
        get() = shortName

    override fun setEntityName(text: String): Stadium {
        return this.apply { name = text.trim() }
    }

    companion object {
        operator fun invoke(name: String): Stadium = when {
            name.isBlank() -> Stadium()
            else -> Stadium(name = name, id = UUID.randomUUID())
        }
    }
}