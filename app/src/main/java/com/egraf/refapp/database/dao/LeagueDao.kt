package com.egraf.refapp.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import com.egraf.refapp.database.entities.League

@Dao
interface LeagueDao {
    @Insert
    fun addLeague(league: League)

    @Update
    fun updateLeague(league: League)
}