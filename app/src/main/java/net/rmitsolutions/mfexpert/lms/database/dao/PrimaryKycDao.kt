package net.rmitsolutions.mfexpert.lms.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import net.rmitsolutions.mfexpert.lms.database.entities.PrimaryKYC

@Dao
interface PrimaryKycDao {

    @Insert
    fun insert(primaryKYC: PrimaryKYC)

    @Query("SELECT * FROM primary_kyc")
    fun getAllPrimaryKyc(): List<PrimaryKYC>

    @Query("SELECT name FROM primary_kyc")
    fun getAllPrimaryKycNames() : List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(district: List<PrimaryKYC>?)
}