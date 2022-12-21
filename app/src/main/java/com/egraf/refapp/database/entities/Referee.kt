package com.egraf.refapp.database.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity
data class Referee(
    @PrimaryKey
    override var id: UUID = UUID.randomUUID(),
    var firstName: String = "",
    var secondName: String = "",
    var thirdName: String = "",
) : com.egraf.refapp.database.entities.Entity(), Parcelable {
    override val shortName: String
        get() = "${secondName.trim()} ${firstName.trim()}".trim()
    override val fullName: String
        get() = "${secondName.trim()} ${firstName.trim()} ${thirdName.trim()}".trim()
    override val title: String
        get() = shortName

    override fun setEntityName(text: String): Referee {
        val fullName = text.trim().split(" ")
        if (fullName.isNotEmpty()) secondName = fullName[0]
        if (fullName.size > 1) firstName = fullName[1]
        if (fullName.size > 2) thirdName = fullName[2]

        return this
    }
}