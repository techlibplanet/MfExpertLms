package net.rmitsolutions.mfexpert.lms.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import net.rmitsolutions.mfexpert.lms.database.entities.Occupation

@Dao
interface OccupationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(occupation: Occupation)

    @Query("SELECT * FROM occupations")
    fun getAllOccupation(): List<Occupation>

    @Query("SELECT name FROM occupations")
    fun getAllOccupationNames() : List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(occupation: List<Occupation>?)
}