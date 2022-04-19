package com.egraf.refapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.egraf.refapp.database.dao.GameDao
import com.egraf.refapp.database.dao.LeagueDao
import com.egraf.refapp.database.dao.StadiumDao
import com.egraf.refapp.database.entities.Game
import com.egraf.refapp.database.entities.League
import com.egraf.refapp.database.entities.Stadium
import java.util.*

@Database(entities = [Game::class, Stadium::class, League::class], version = 3, exportSchema = false)
@TypeConverters(GameTypeConverters::class)
abstract class GameDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao
    abstract fun stadiumDao(): StadiumDao
    abstract fun leagueDao(): LeagueDao
}

/**
 * С версии 1: Room с одном таблицей Game
 * К версии 2: отдельная таблица Stadium с внешним ключом id = Game.stadiumId
 */
val migration_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS Stadium " +
                    "(id TEXT PRIMARY KEY NOT NULL," +
                    "name TEXT NOT NULL)"
        )
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS Game_new " +
                    "(id TEXT PRIMARY KEY NOT NULL," +
                    "date INTEGER NOT NULL," +
                    "homeTeam TEXT NOT NULL," +
                    "guestTeam TEXT NOT NULL," +
                    "stadiumId TEXT NOT NULL," +
                    "league TEXT NOT NULL," +
                    "isPaid INTEGER NOT NULL)"
        )
        database.execSQL(

            "INSERT INTO Stadium(id, name) " +
                    // генерация рандомного UUID-string ключа
                    "SELECT DISTINCT lower(hex( randomblob(4)) || '-' || hex( randomblob(2))\n" +
                    "         || '-' || '4' || substr( hex( randomblob(2)), 2) || '-'\n" +
                    "         || substr('AB89', 1 + (abs(random()) % 4) , 1)  ||\n" +
                    "         substr(hex(randomblob(2)), 2) || '-' || hex(randomblob(6)))," +
                    " stadium FROM Game " +
                    "GROUP BY stadium"
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

/**
 * С версии 2: Room с одном таблицей Game и Stadium
 * К версии 3: отдельная таблица League с внешним ключом id = Game.leagueId
 */
val migration_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS League " +
                    "(id TEXT PRIMARY KEY NOT NULL," +
                    "name TEXT NOT NULL)"
        )
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS Game_new " +
                    "(id TEXT PRIMARY KEY NOT NULL," +
                    "date INTEGER NOT NULL," +
                    "homeTeam TEXT NOT NULL," +
                    "guestTeam TEXT NOT NULL," +
                    "stadiumId TEXT NOT NULL," +
                    "leagueId TEXT NOT NULL," +
                    "isPaid INTEGER NOT NULL)"
        )
        database.execSQL(
            "INSERT INTO League(id, name) " +
                    "SELECT DISTINCT lower(hex( randomblob(4)) || '-' || hex( randomblob(2))\n" +
                    "         || '-' || '4' || substr( hex( randomblob(2)), 2) || '-'\n" +
                    "         || substr('AB89', 1 + (abs(random()) % 4) , 1)  ||\n" +
                    "         substr(hex(randomblob(2)), 2) || '-' || hex(randomblob(6))) , league FROM Game " +
                    "GROUP BY league"
        )
        database.execSQL(
            "INSERT INTO Game_new(id, date, homeTeam, guestTeam, stadiumId, leagueId, isPaid) " +
                    "SELECT G.id, date, homeTeam, guestTeam, stadiumId, L.id, isPaid " +
                    "FROM Game G " +
                    "INNER JOIN League L ON G.league = L.name"
        )
        database.execSQL("DROP TABLE Game")
        database.execSQL("ALTER TABLE Game_new RENAME TO Game")
    }
}