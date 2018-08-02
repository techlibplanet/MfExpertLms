package net.rmitsolutions.mfexpert.lms.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import net.rmitsolutions.mfexpert.lms.database.entities.District

@Dao
interface DistrictDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(district: District)

    @Query("SELECT * FROM districts")
    fun getAllDistrict(): List<District>

    @Query("SELECT name FROM districts")
    fun getDistrictNames() : List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(district: List<District>?)
}