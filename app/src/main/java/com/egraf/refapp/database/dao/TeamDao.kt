package com.egraf.refapp.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.egraf.refapp.database.entities.Team

@Dao
interface TeamDao {
    @Query("SELECT * FROM Team")
    fun getTeams(): LiveData<List<Team>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTeam(team: Team)

    @Delete
    fun deleteTeam(team: Team)
}