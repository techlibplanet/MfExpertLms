package net.rmitsolutions.mfexpert.lms.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import net.rmitsolutions.mfexpert.lms.database.entities.Caste
import net.rmitsolutions.mfexpert.lms.database.entities.HouseOwnership

@Dao
interface HouseOwnershipDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(houseOwnership: HouseOwnership)

    @Query("SELECT * FROM house_ownership")
    fun getAllHouseOwnership(): List<HouseOwnership>

    @Query("SELECT name FROM house_ownership")
    fun getAllHouseOwnershipNames() : List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(houseOwnership: List<HouseOwnership>?)
}