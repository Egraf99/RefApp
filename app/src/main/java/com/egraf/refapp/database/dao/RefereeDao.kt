package com.egraf.refapp.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.egraf.refapp.database.entities.League
import com.egraf.refapp.database.entities.Referee
import com.egraf.refapp.database.entities.Stadium
import java.util.*

@Dao
interface RefereeDao {
    @Query("SELECT * FROM Referee")
    fun getReferees(): LiveData<List<Referee>>

    @Query("SELECT * FROM Referee WHERE id=(:id)")
    fun getReferee(id: UUID): LiveData<Referee>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addReferee(referee: Referee)

    @Update
    fun updateReferee(referee: Referee)


}