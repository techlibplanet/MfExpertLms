package net.rmitsolutions.mfexpert.lms.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import net.rmitsolutions.mfexpert.lms.viewmodels.CenterAddressInformationModel
import net.rmitsolutions.mfexpert.lms.viewmodels.CenterPrimaryInformationModel


@Dao
interface CenterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPrimaryInformationDetails(primaryInformationModel: CenterPrimaryInformationModel):Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAddressInformationModel(addressInformationModel: CenterAddressInformationModel):Long

    @Query("Select centerName from center_primary_info")
    fun getCentersInfo(): List<String>
}