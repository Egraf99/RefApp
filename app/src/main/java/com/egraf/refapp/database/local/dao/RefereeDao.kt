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

    @Query("UPDATE Referee SET firstName=:firstName WHERE id=:refereeId")
    fun updateRefereeFirstName(refereeId: UUID, firstName: String)

    @Query("UPDATE Referee SET secondName=:middleName WHERE id=:refereeId")
    fun updateRefereeMiddleName(refereeId: UUID, middleName: String)

    @Query("UPDATE Referee SET thirdName=:lastName WHERE id=:refereeId")
    fun updateRefereeLastName(refereeId: UUID, lastName: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addReferee(referee: Referee)

    @Delete
    fun deleteReferee(referee: Referee)

    @Update
    fun updateReferee(referee: Referee)


}