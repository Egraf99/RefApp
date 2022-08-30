package com.egraf.refapp.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.egraf.refapp.database.entities.League

@Dao
interface LeagueDao {
    @Query("SELECT * FROM League")
    fun getLeagues(): LiveData<List<League>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addLeague(league: League)

    @Update
    fun updateLeague(league: League)


}