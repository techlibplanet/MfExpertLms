package net.rmitsolutions.mfexpert.lms.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
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