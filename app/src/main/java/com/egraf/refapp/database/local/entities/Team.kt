package com.egraf.refapp.database.local.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.egraf.refapp.ui.dialogs.search_entity.EmptyItem
import com.egraf.refapp.ui.dialogs.search_entity.SearchItem
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity
data class Team(
    @PrimaryKey
    override var id: UUID = EmptyItem.id,
    var name: String = ""
) : com.egraf.refapp.database.local.entities.Entity(), Parcelable {
    override val shortName: String
        get() = name
    override val fullName: String
        get() = name
    override val title: String
        get() = shortName

    override fun setEntityName(text: String): Team {
        return this.apply { name = text.trim() }
    }

    companion object {
        operator fun invoke(name: String): Team = when {
            name.isBlank() -> Team()
            else -> Team(name = name, id = SearchItem.randomId())
        }
    }
}
