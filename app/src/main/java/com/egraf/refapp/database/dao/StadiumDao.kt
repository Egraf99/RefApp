package com.egraf.refapp.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.egraf.refapp.database.entities.Game
import com.egraf.refapp.database.entities.Stadium

@Dao
interface StadiumDao {
    @Query("SELECT * FROM Stadium")
    fun getStadiums(): LiveData<List<Stadium>>

    @Insert
    fun addStadium(stadium: Stadium)

    @Query("SELECT * FROM Stadium WHERE id=(:id)")
    fun getStadium(id: Long): LiveData<Stadium?>

    @Update
    fun updateStadium(stadium: Stadium)
}