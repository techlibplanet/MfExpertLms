package net.rmitsolutions.mfexpert.lms.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import net.rmitsolutions.mfexpert.lms.database.entities.KycDetails
import net.rmitsolutions.mfexpert.lms.database.entities.PrimaryKYC

@Dao
interface KycDetailsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(kycDetails: KycDetails)

    @Query("SELECT * FROM kyc_details")
    fun getAllKycDetails(): List<KycDetails>

    @Query("SELECT name FROM kyc_details")
    fun getAllKycDetailNames() : List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(kycDetails: List<KycDetails>?)
}