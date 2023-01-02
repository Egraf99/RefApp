package com.egraf.refapp.database.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.egraf.refapp.ui.dialogs.search_entity.EmptyItem
import com.egraf.refapp.ui.dialogs.search_entity.SearchItem
import com.egraf.refapp.views.custom_views.GameComponent
import com.egraf.refapp.views.custom_views.Saving
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity
data class Stadium(
    @PrimaryKey
    override var id: UUID = EmptyItem.id,
    var name: String = "EmptyStadium"
) : com.egraf.refapp.database.entities.Entity(), Saving<UUID>, SearchItem, Parcelable {
    override val shortName: String
        get() = name
    override val fullName: String
        get() = name
    override val title: String
        get() = shortName
    @Ignore
    override val savedValue = id
    val isEmpty
        get() = id == EmptyItem.id

    override fun setEntityName(text: String): Stadium {
        return this.apply { name = text.trim() }
    }
}