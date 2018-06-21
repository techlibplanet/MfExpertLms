package net.rmitsolutions.mfexpert.lms.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import net.rmitsolutions.mfexpert.lms.database.entities.SecondaryKYC

@Dao
interface SecondaryKycDao {
    @Insert
    fun insert(secondaryKYC: SecondaryKYC)

    @Query("SELECT * FROM districts")
    fun getAllSecondaryKyC(): List<SecondaryKYC>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(district: List<SecondaryKYC>?)
}