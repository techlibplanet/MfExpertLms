package net.rmitsolutions.mfexpert.lms.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import net.rmitsolutions.mfexpert.lms.database.entities.IncomeProof

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