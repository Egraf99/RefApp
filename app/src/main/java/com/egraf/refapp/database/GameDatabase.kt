package com.egraf.refapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.egraf.refapp.Game

@Database(entities = [Game::class ], version = 1, exportSchema = false)
@TypeConverters(GameTypeConverters::class)
abstract class GameDatabase: RoomDatabase() {
    abstract fun gameDao(): GameDao
}