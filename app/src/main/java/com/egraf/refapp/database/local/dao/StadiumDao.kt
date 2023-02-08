package com.egraf.refapp.database.local.dao

import androidx.room.*
import com.egraf.refapp.database.local.entities.Stadium
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

    @Query("UPDATE Stadium SET name=:title WHERE id=:stadiumId")
    fun updateStadiumTitle(stadiumId: UUID, title: String)

    @Delete
    fun deleteStadium(stadium: Stadium)
}