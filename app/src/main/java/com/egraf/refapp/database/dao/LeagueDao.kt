package com.egraf.refapp.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.egraf.refapp.database.entities.League
import com.egraf.refapp.database.entities.Stadium
import java.util.*

@Dao
interface LeagueDao {
    @Query("SELECT * FROM League")
    fun getLeagues(): LiveData<List<League>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addLeague(league: League)

    @Query("SELECT * FROM League WHERE id=(:id)")
    fun getLeague(id: UUID): LiveData<League>

    @Update
    fun updateLeague(league: League)


}