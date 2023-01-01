package com.egraf.refapp.database

import androidx.room.TypeConverter
import com.egraf.refapp.database.entities.GameDate
import com.egraf.refapp.database.entities.GameDateTime
import com.egraf.refapp.database.entities.GameTime
import com.egraf.refapp.database.entities.toGameDateTime
import java.time.Instant
import java.time.ZoneId
import java.util.*

class GameTypeConverters {
    @TypeConverter
    fun fromGameDateTime(date: GameDateTime?): Long? = date?.toMillis()

    @TypeConverter
    fun inGameDateTime(millisFromEpoch: Long?): GameDateTime? = millisFromEpoch?.toGameDateTime()

    @TypeConverter
    fun fromUUID(uuid: UUID?): String? {
        return uuid?.toString()
    }

    @TypeConverter
    fun inUUID(uuid:String?): UUID? {
        if (uuid != null)
            return UUID.fromString(uuid)
        return null
    }
}