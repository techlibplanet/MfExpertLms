package net.rmitsolutions.mfexpert.lms.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import io.reactivex.internal.operators.flowable.FlowableWithLatestFromMany
import net.rmitsolutions.mfexpert.lms.database.entities.LoanPurpose

@Dao
interface LoanPurposeDao {
    @Insert
    fun insert(loanPurpose: LoanPurpose)

    @Query("SELECT * FROM loan_purpose")
    fun getAllLoanPurpose(): List<LoanPurpose>

    @Query("SELECT name FROM loan_purpose")
    fun getAllLoanPurposeNames() : List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(district: List<LoanPurpose>?)
}