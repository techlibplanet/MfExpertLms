package net.rmitsolutions.mfexpert.lms.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import net.rmitsolutions.mfexpert.lms.database.entities.Caste

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