package com.egraf.refapp.database.local.dao

import androidx.room.*
import com.egraf.refapp.database.local.entities.Referee
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface RefereeDao {
    @Query("SELECT * FROM Referee WHERE id=(:id)")
    fun getReferee(id: UUID): Flow<Referee?>

    @Query("SELECT * FROM Referee ORDER BY secondName, firstName, thirdName")
    fun getReferees(): List<Referee>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addReferee(referee: Referee)

    @Delete
    fun deleteReferee(referee: Referee)

    @Update
    fun updateReferee(referee: Referee)


}