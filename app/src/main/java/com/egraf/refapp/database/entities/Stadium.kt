package com.egraf.refapp.database.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
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
) : com.egraf.refapp.database.entities.Entity(), Saving<UUID>, SearchItem, Parcelable {
    override val shortName: String
        get() = name
    override val fullName: String
        get() = name
    override val title: String
        get() = shortName
    @Ignore override val savedValue: UUID = id

    override fun setEntityName(text: String): Stadium {
        return this.apply { name = text.trim() }
    }

    companion object {
        operator fun invoke(): GameComponent<UUID, Stadium> = GameComponent()
        operator fun invoke(id: UUID): GameComponent<UUID, Stadium> = GameComponent(Stadium(id))
        operator fun invoke(name: String): GameComponent<UUID, Stadium> =
            GameComponent(Stadium(UUID.randomUUID(), name))
        operator fun invoke(id: UUID, name: String = "EmptyStadium"): GameComponent<UUID, Stadium> =
            GameComponent(Stadium(id, name))
    }
}
