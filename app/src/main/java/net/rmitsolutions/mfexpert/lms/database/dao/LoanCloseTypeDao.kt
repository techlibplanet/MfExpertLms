package net.rmitsolutions.mfexpert.lms.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import net.rmitsolutions.mfexpert.lms.database.entities.KycDetails
import net.rmitsolutions.mfexpert.lms.database.entities.LoanCloseType

@Dao
interface LoanCloseTypeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(loanCloseType: LoanCloseType)

    @Query("SELECT * FROM loan_close_type")
    fun getAllLoanCloseType(): List<LoanCloseType>

    @Query("SELECT name FROM loan_close_type")
    fun getAllLoanCloseTypeNames() : List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(loanCloseType: List<LoanCloseType>?)
}