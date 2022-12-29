package com.egraf.refapp.database.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.egraf.refapp.ui.dialogs.search_entity.SearchItemInterface
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity
data class Stadium(
    @PrimaryKey
    override var id: UUID = UUID.randomUUID(),
    var name: String = ""
) : com.egraf.refapp.database.entities.Entity(), SearchItemInterface, Parcelable {
    override val shortName: String
        get() = name
    override val fullName: String
        get() = name
    override val title: String
        get() = shortName

    override fun setEntityName(text: String): Stadium {
        return this.apply { name = text.trim() }
    }
}
