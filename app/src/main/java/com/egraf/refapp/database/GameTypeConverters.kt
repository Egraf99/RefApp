package com.egraf.refapp.database

import androidx.room.TypeConverter
import java.util.*

class GameTypeConverters {
    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun inDate(millisFromEpoch: Long?): Date? {
        return millisFromEpoch?.let {
            Date(it)
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