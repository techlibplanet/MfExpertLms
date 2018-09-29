package net.rmitsolutions.mfexpert.lms.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import net.rmitsolutions.mfexpert.lms.database.entities.PrimaryKYC

@Dao
interface PrimaryKycDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(primaryKYC: PrimaryKYC)

    @Query("SELECT * FROM primary_kyc")
    fun getAllPrimaryKyc(): List<PrimaryKYC>

    @Query("SELECT name FROM primary_kyc")
    fun getAllPrimaryKycNames() : List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(district: List<PrimaryKYC>?)
}