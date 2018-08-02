package net.rmitsolutions.mfexpert.lms.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import net.rmitsolutions.mfexpert.lms.database.entities.Caste
import net.rmitsolutions.mfexpert.lms.database.entities.Religion

@Dao
interface CasteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(caste: Caste)

    @Query("SELECT * FROM caste")
    fun getAllCaste(): List<Caste>

    @Query("SELECT name FROM caste")
    fun getAllCasteNames() : List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(caste: List<Caste>?)

}