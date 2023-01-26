package com.egraf.refapp.database.local.entities

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
) : com.egraf.refapp.database.local.entities.Entity(), Parcelable {

    init {
        if (firstName.isBlank() && middleName.isBlank() && lastName.isBlank())
            id = EmptyItem.id
    }

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
         * ------- bySpace = false --------
         * Name:
         *      f_..._ - for first name,
         *      m_..._ - for middle name,
         *      l_..._ - for last name.
         *
         * Example: "f_John_ m_Bence_ l_Doe_" return Referee(firstName=John, middleName=Bence, lastName=Doe)
         *
         * If pattern return empty strings for all three name, constructor return Referee with EmptyItem.id
         * --------------------------------
         * ------- bySpace = true --------
         * When bySpace is true, name split by space symbols and:
         *      middleName give first value,       >
         *      firstName give second value,       >  if not exist give empty string
         *      lastName give thirdValue,          >
         *
         * Example: "Bence John Doe" return Referee(firstName=John, middleName=Bence, lastName=Doe)
         * --------------------------------
         */
        operator fun invoke(
            name: String,
            id: UUID = randomId(),
            bySpace: Boolean = false
        ): Referee {
            if (bySpace) {
                val namesList = name.split(" ")
                return Referee(
                    id,
                    namesList.getOrElse(1) { "" },
                    namesList.getOrElse(0) { "" },
                    namesList.getOrElse(2) { "" })
            }
            val firstName = Regex("f_(.*?)_").find(name)?.groupValues?.get(1) ?: ""
            val middleName = Regex("m_(.*?)_").find(name)?.groupValues?.get(1) ?: ""
            val lastName = Regex("l_(.*?)_").find(name)?.groupValues?.get(1) ?: ""

            return if (firstName == "" && middleName == "" && lastName == "")
                Referee()
            else
                Referee(id, firstName, middleName, lastName)
        }
    }
}