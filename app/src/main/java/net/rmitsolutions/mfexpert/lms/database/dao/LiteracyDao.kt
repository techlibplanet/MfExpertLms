package net.rmitsolutions.mfexpert.lms.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import net.rmitsolutions.mfexpert.lms.database.entities.Literacy
@Dao
interface LiteracyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(literacy: Literacy)

    @Query("SELECT * FROM literacy")
    fun getAllLiteracy(): List<Literacy>

    @Query("SELECT name FROM literacy")
    fun getAllLiteracyNames() : List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(literacy: List<Literacy>?)
}