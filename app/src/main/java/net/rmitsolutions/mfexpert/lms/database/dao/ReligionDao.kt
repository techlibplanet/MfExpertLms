package net.rmitsolutions.mfexpert.lms.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import net.rmitsolutions.mfexpert.lms.database.entities.KycDetails
import net.rmitsolutions.mfexpert.lms.database.entities.Religion

@Dao
interface ReligionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(religion: Religion)

    @Query("SELECT * FROM religion")
    fun getAllReligion(): List<Religion>

    @Query("SELECT name FROM religion")
    fun getAllReligionNames() : List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(religion: List<Religion>?)
}