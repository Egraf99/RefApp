package com.egraf.refapp.database.local

import androidx.room.TypeConverter
import com.egraf.refapp.database.local.entities.GameDateTime
import com.egraf.refapp.database.local.entities.toGameDateTime
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