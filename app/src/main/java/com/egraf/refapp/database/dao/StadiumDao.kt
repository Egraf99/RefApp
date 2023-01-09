package com.egraf.refapp.database.dao

import androidx.room.*
import com.egraf.refapp.database.entities.Stadium
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface StadiumDao {
    @Query("SELECT * FROM Stadium ORDER BY name ASC")
    fun getStadiums(): List<Stadium>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addStadium(stadium: Stadium)

    @Query("SELECT * FROM Stadium WHERE id=(:id)")
    fun getStadium(id: UUID): Flow<Stadium?>

    @Update
    fun updateStadium(stadium: Stadium)

    @Delete
    fun deleteStadium(stadium: Stadium)
}