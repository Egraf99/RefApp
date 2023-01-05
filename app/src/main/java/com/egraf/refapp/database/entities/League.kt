package com.egraf.refapp.database.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.egraf.refapp.ui.dialogs.search_entity.EmptyItem
import com.egraf.refapp.ui.dialogs.search_entity.SearchItem
import com.egraf.refapp.views.custom_views.Saving
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity
data class League(
    @PrimaryKey
    override var id: UUID = EmptyItem.id,
    var name: String = "EmptyLeague",
) : com.egraf.refapp.database.entities.Entity(), Parcelable {
    override val shortName: String
        get() = name
    override val fullName: String
        get() = name
    override val title
        get() = shortName

    override fun setEntityName(text: String): League {
        return this.apply { name = text.trim() }
    }

    override fun toString(): String = name
}