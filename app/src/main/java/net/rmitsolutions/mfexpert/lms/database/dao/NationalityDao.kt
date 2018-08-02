package net.rmitsolutions.mfexpert.lms.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import net.rmitsolutions.mfexpert.lms.database.entities.KycDetails
import net.rmitsolutions.mfexpert.lms.database.entities.Nationality

@Dao
interface NationalityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(nationality: Nationality)

    @Query("SELECT * FROM nationality")
    fun getAllNationality(): List<Nationality>

    @Query("SELECT name FROM nationality")
    fun getAllNationalityNames() : List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(nationality: List<Nationality>?)
}