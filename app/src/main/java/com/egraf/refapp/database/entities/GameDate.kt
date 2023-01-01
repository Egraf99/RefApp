package com.egraf.refapp.database.entities

import android.os.Parcelable
import android.text.format.DateFormat
import com.egraf.refapp.views.custom_views.Saving
import kotlinx.android.parcel.Parcelize
import java.util.*

private const val DATE_FORMAT = "dd.MM.yyyy"

@Parcelize
class GameDate(val value: Date): Parcelable, Saving<Date> {
    override val title: String = DateFormat.format(DATE_FORMAT, value).toString()
    override val savedValue: Date = value
    override fun toString(): String = ""
    override fun equals(other: Any?): Boolean = (other as GameDate).value == this.value
    override fun hashCode(): Int = value.hashCode()

    companion object {
        operator fun invoke(): GameDate = GameDate(Date())
    }
}