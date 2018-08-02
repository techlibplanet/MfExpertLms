package net.rmitsolutions.mfexpert.lms.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import net.rmitsolutions.mfexpert.lms.database.entities.KycDetails
import net.rmitsolutions.mfexpert.lms.database.entities.LoanRejectionReasons

@Dao
interface LoanRejectionReasonsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(loanRejectionReasons: LoanRejectionReasons)

    @Query("SELECT * FROM loan_rejection_reasons")
    fun getAllLoanRejectionReasons(): List<LoanRejectionReasons>

    @Query("SELECT name FROM loan_rejection_reasons")
    fun getAllLoanRejectionReasonNames() : List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(loanRejectionReasons: List<LoanRejectionReasons>?)
}