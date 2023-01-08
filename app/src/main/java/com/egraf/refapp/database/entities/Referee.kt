package com.egraf.refapp.database.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.egraf.refapp.ui.dialogs.search_entity.EmptyItem
import com.egraf.refapp.ui.dialogs.search_entity.SearchItem.Companion.randomId
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity
data class Referee(
    @PrimaryKey
    override var id: UUID = EmptyItem.id,
    var firstName: String = "",
    @ColumnInfo(name = "secondName")
    var middleName: String = "",
    @ColumnInfo(name = "thirdName")
    var lastName: String = "",
) : com.egraf.refapp.database.entities.Entity(), Parcelable {

    override val shortName: String
        get() = "$middleName $firstName"
    override val fullName: String
        get() = "$middleName $firstName $lastName"
    override val title: String
        get() = shortName

    override fun setEntityName(text: String): Referee {
        val fullName = text.trim().split(" ")
        if (fullName.isNotEmpty()) middleName = fullName[0]
        if (fullName.size > 1) firstName = fullName[1]
        if (fullName.size > 2) lastName = fullName[2]

        return this
    }

    companion object {
        /**
         * Pattern:
         *      f_..._ - for first name,
         *      m_..._ - for middle name,
         *      l_..._ - for last name.
         *
         * Example: "f_John_ m_Bence_ l_Doe_" return Referee(firstName=John, middleName=Bence, lastName=Doe)
         *
         * If pattern return empty strings for all three name, constructor return Referee with EmptyItem.id
         */
        operator fun invoke(pattern: String): Referee {
            val firstName = Regex("f_(.*?)_").find(pattern)?.groupValues?.get(1) ?: ""
            val middleName = Regex("m_(.*?)_").find(pattern)?.groupValues?.get(1) ?: ""
            val lastName = Regex("l_(.*?)_").find(pattern)?.groupValues?.get(1) ?: ""

            return if (firstName == "" && middleName == "" && lastName == "")
                Referee()
            else
                Referee(randomId(), firstName, middleName, lastName)
        }
    }
}

fun main() {
    println(Referee("f_Egor_ asdlgjlkg"))
}