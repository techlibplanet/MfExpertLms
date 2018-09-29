package net.rmitsolutions.mfexpert.lms.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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