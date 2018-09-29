package net.rmitsolutions.mfexpert.lms.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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