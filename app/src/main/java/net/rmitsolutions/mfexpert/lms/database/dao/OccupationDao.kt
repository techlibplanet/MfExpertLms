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

    @Query("SELECT * FROM districts")
    fun getAllOccupation(): List<Occupation>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(occupation: List<Occupation>?)
}