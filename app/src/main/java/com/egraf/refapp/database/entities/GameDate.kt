package com.egraf.refapp.database.entities

import android.os.Parcelable
import com.egraf.refapp.views.custom_views.Saving
import kotlinx.android.parcel.Parcelize
import java.time.*
import java.time.format.DateTimeFormatter

@Parcelize
class GameDateTime(val date: GameDate, val time: GameTime) : Parcelable {
    fun toLocalDateTime(): LocalDateTime = date.value.atTime(time.value)
    fun toMillis(zone: ZoneId = ZoneId.systemDefault()) =
        this.toLocalDateTime().atZone(zone)?.toInstant()?.toEpochMilli()

    override fun equals(other: Any?): Boolean {
        return (other as GameDateTime).date == this.date && other.time == this.time
    }

    override fun hashCode(): Int {
        var result = date.hashCode()
        result = 31 * result + time.hashCode()
        return result
    }
}

fun Long.toGameDateTime(): GameDateTime {
    val date = Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault())
    return GameDateTime(GameDate(date.toLocalDate()), GameTime(date.toLocalTime()))
}

@Parcelize
class GameDate(val value: LocalDate, private val format: String = "dd.MM.yyyy") : Parcelable, Saving<LocalDate> {
    override val title: String = value.format(DateTimeFormatter.ofPattern(format))
    override val savedValue: LocalDate = value
    override fun toString(): String = ""
    override fun equals(other: Any?): Boolean = (other as GameDate).value == this.value
    override fun hashCode(): Int = value.hashCode()

    companion object {
        operator fun invoke(): GameDate = GameDate(LocalDate.now())
    }
}

@Parcelize
class GameTime(val value: LocalTime, private val format: String = "HH:mm") : Parcelable, Saving<LocalTime> {
    override val title: String = value.format(DateTimeFormatter.ofPattern(format))
    override val savedValue: LocalTime = value
    override fun toString(): String = ""
    override fun equals(other: Any?): Boolean = (other as GameTime).value == this.value
    override fun hashCode(): Int = value.hashCode()

    companion object {
        operator fun invoke(): GameTime = GameTime(LocalTime.now())
    }
}
