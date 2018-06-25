package net.rmitsolutions.mfexpert.lms.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import net.rmitsolutions.mfexpert.lms.database.entities.Occupation

@Dao
interface OccupationDao {

    @Insert
    fun insert(occupation: Occupation)

    @Query("SELECT * FROM occupations")
    fun getAllOccupation(): List<Occupation>

    @Query("SELECT name FROM occupations")
    fun getAllOccupationNames() : List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(occupation: List<Occupation>?)
}