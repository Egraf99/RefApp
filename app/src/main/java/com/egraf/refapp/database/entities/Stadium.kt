package com.egraf.refapp.database.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.egraf.refapp.ui.dialogs.search_entity.SearchItem
import com.egraf.refapp.views.custom_views.GameComponent
import com.egraf.refapp.views.custom_views.Saving
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity
data class Stadium(
    @PrimaryKey
    override var id: UUID = UUID.randomUUID(),
    var name: String = "EmptyStadium"
) : com.egraf.refapp.database.entities.Entity(), Saving, SearchItem, Parcelable {
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
        operator fun invoke(): GameComponent<Stadium> = GameComponent()
        operator fun invoke(id: UUID): GameComponent<Stadium> = GameComponent(Stadium(id))
        operator fun invoke(name: String): GameComponent<Stadium> =
            GameComponent(Stadium(UUID.randomUUID(), name))
        operator fun invoke(id: UUID, name: String = "EmptyStadium"): GameComponent<Stadium> =
            GameComponent(Stadium(id, name))
    }
}
