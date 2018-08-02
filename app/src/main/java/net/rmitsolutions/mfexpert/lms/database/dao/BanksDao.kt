package net.rmitsolutions.mfexpert.lms.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import net.rmitsolutions.mfexpert.lms.database.entities.Banks
import net.rmitsolutions.mfexpert.lms.database.entities.KycDetails

@Dao
interface BanksDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(banks: Banks)

    @Query("SELECT * FROM banks")
    fun getAllBanks(): List<Banks>

    @Query("SELECT name FROM banks")
    fun getAllBankNames() : List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(banks: List<Banks>?)
}