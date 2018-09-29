package net.rmitsolutions.mfexpert.lms.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import net.rmitsolutions.mfexpert.lms.database.entities.SecondaryKYC

@Dao
interface SecondaryKycDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(secondaryKYC: SecondaryKYC)

    @Query("SELECT * FROM secondary_kyc")
    fun getAllSecondaryKyc(): List<SecondaryKYC>

    @Query("SELECT name FROM secondary_kyc")
    fun getAllSecondaryKycNames() : List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(district: List<SecondaryKYC>?)
}