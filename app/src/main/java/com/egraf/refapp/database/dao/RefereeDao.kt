package com.egraf.refapp.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.egraf.refapp.database.entities.League
import com.egraf.refapp.database.entities.Referee
import com.egraf.refapp.database.entities.Stadium

@Dao
interface RefereeDao {
    @Query("SELECT * FROM Referee")
    fun getReferees(): LiveData<List<Referee>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addReferee(referee: Referee)

    @Update
    fun updateReferee(referee: Referee)


}