package com.egraf.refapp.database

import androidx.room.TypeConverter
import com.egraf.refapp.database.entities.GameDate
import java.util.*

class GameTypeConverters {
    @TypeConverter
    fun fromGameDate(date: GameDate?): Long? {
        return date?.savedValue?.time
    }

    @TypeConverter
    fun inGameDate(millisFromEpoch: Long?): GameDate? {
        return millisFromEpoch?.let {
            GameDate(Date(it))
        }
    }

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