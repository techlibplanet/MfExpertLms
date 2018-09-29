package net.rmitsolutions.mfexpert.lms.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import net.rmitsolutions.mfexpert.lms.database.entities.Banks

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