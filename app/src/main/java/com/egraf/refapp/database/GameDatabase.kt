package com.egraf.refapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.egraf.refapp.database.dao.GameDao
import com.egraf.refapp.database.dao.StadiumDao
import com.egraf.refapp.database.entities.Game
import com.egraf.refapp.database.entities.Stadium

@Database(entities = [Game::class, Stadium::class], version = 2, exportSchema = false)
@TypeConverters(GameTypeConverters::class)
abstract class GameDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao
    abstract fun stadiumDao(): StadiumDao
}

val migration_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS Stadium " +
                    "(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    "name TEXT NOT NULL)"
        )
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS Game_new " +
                    "(id TEXT PRIMARY KEY NOT NULL," +
                    "date INTEGER NOT NULL," +
                    "homeTeam TEXT NOT NULL," +
                    "guestTeam TEXT NOT NULL," +
                    "stadiumId INTEGER NOT NULL," +
                    "league TEXT NOT NULL," +
                    "isPaid INTEGER NOT NULL)"
        )
        database.execSQL(
            "INSERT INTO Stadium(name)" +
                    "SELECT DISTINCT stadium FROM Game"
        )
        database.execSQL(
            "INSERT INTO Game_new(id, date, homeTeam, guestTeam, stadiumId, league, isPaid) " +
                    "SELECT G.id, date, homeTeam, guestTeam, S.id, league, isPaid " +
                    "FROM Game G " +
                    "INNER JOIN Stadium S ON G.stadium = S.name"
        )
        database.execSQL("DROP TABLE Game")
        database.execSQL("ALTER TABLE Game_new RENAME TO Game")
    }
}