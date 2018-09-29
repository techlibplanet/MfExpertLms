package net.rmitsolutions.mfexpert.lms.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import net.rmitsolutions.mfexpert.lms.database.entities.LoanPurpose

@Dao
interface LoanPurposeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(loanPurpose: LoanPurpose)

    @Query("SELECT * FROM loan_purpose")
    fun getAllLoanPurpose(): List<LoanPurpose>

    @Query("SELECT name FROM loan_purpose")
    fun getAllLoanPurposeNames() : List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(district: List<LoanPurpose>?)
}