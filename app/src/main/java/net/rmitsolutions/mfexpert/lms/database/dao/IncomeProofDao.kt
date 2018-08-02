package net.rmitsolutions.mfexpert.lms.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import net.rmitsolutions.mfexpert.lms.database.entities.IncomeProof
import net.rmitsolutions.mfexpert.lms.database.entities.KycDetails

@Dao
interface IncomeProofDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(incomeProof: IncomeProof)

    @Query("SELECT * FROM income_proof")
    fun getAllIncomeProof(): List<IncomeProof>

    @Query("SELECT name FROM income_proof")
    fun getAllIncomeProofNames() : List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(incomeProof: List<IncomeProof>?)
}