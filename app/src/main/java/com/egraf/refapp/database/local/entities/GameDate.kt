package com.egraf.refapp.database.local.entities

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

    operator fun compareTo(other: GameDateTime): Int =
        if (date != other.date) date.compareTo(other.date) else time.compareTo(other.time)

    operator fun compareTo(other: LocalDateTime): Int =
        this.compareTo(GameDateTime(GameDate(other.toLocalDate()), GameTime(other.toLocalTime())))

    override fun toString(): String = "$date $time"
    override fun hashCode(): Int {
        var result = date.hashCode()
        result = 31 * result + time.hashCode()
        return result
    }

    companion object {
        operator fun invoke(dateTime: LocalDateTime): GameDateTime = GameDateTime(
            GameDate(dateTime.toLocalDate()),
            GameTime(dateTime.toLocalTime())
        )

        operator fun invoke(): GameDateTime = GameDateTime(GameDate(), GameTime())
    }
}

fun Long.toGameDateTime(): GameDateTime {
    val date = Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault())
    return GameDateTime(GameDate(date.toLocalDate()), GameTime(date.toLocalTime()))
}

@Parcelize
class GameDate(val value: LocalDate, private val format: String = "dd.MM.yyyy") : Parcelable, Saving<LocalDate> {
    override val title: String = toString()
    override val savedValue: LocalDate = value
    override fun toString(): String = value.format(DateTimeFormatter.ofPattern(format))
    override fun equals(other: Any?): Boolean = (other as GameDate).value == this.value
    override fun hashCode(): Int = value.hashCode()


    operator fun compareTo(other: GameDate): Int =
        if (other.value == value) 0 else if (other.value < value) 1 else -1

    operator fun compareTo(other: LocalDate): Int =
        if (other == value) 0 else if (other < value) 1 else -1

    companion object {
        operator fun invoke(): GameDate = GameDate(LocalDate.now())
    }
}

@Parcelize
class GameTime(val value: LocalTime, private val format: String = "HH:mm") : Parcelable, Saving<LocalTime> {
    override val title: String = toString()
    override val savedValue: LocalTime = value
    override fun toString(): String = value.format(DateTimeFormatter.ofPattern(format))
    override fun equals(other: Any?): Boolean = (other as GameTime).value == this.value
    override fun hashCode(): Int = value.hashCode()
    operator fun compareTo(other: GameTime): Int =
        if (other.value == value) 0 else if (other.value < value) 1 else -1

    operator fun compareTo(other: LocalTime): Int =
        if (other == value) 0 else if (other < value) 1 else -1

    companion object {
        operator fun invoke(): GameTime = GameTime(LocalTime.now())
    }
}
