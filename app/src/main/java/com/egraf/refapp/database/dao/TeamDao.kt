package com.egraf.refapp.database.dao

import androidx.room.*
import com.egraf.refapp.database.entities.Stadium
import com.egraf.refapp.database.entities.Team
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface TeamDao {
    @Query("SELECT * FROM Team WHERE id=(:id)")
    fun getTeam(id: UUID): Flow<Team?>

    @Query("SELECT * FROM Team ORDER BY name ASC")
    fun getTeams(): List<Team>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTeam(team: Team)

    @Delete
    fun deleteTeam(team: Team)
}