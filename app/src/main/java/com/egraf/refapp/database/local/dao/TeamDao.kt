package com.egraf.refapp.database.local.dao

import androidx.room.*
import com.egraf.refapp.database.local.entities.Team
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

    @Query("UPDATE Team SET name=:name WHERE id=:teamId")
    fun updateTeamName(teamId: UUID, name: String)

    @Delete
    fun deleteTeam(team: Team)
}