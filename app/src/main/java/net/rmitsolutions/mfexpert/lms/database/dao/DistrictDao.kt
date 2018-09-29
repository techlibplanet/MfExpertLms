package net.rmitsolutions.mfexpert.lms.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import net.rmitsolutions.mfexpert.lms.database.entities.District

@Dao
interface DistrictDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(district: District)

    @Query("SELECT * FROM districts")
    fun getAllDistrict(): List<District>

    @Query("SELECT name FROM districts")
    fun getDistrictNames() : List<String>

    @Query("SELECT id FROM districts")
    fun getDistrictIds() : List<Int>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(district: List<District>?)
}