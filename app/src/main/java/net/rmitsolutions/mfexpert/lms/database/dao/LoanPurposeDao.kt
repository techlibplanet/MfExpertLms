package net.rmitsolutions.mfexpert.lms.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import net.rmitsolutions.mfexpert.lms.database.entities.LoanPurpose

@Dao
interface LoanPurposeDao {
    @Insert
    fun insert(loanPurpose: LoanPurpose)

    @Query("SELECT * FROM districts")
    fun getAllLoanPurpose(): List<LoanPurpose>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(district: List<LoanPurpose>?)
}