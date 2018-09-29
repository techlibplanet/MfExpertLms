package net.rmitsolutions.mfexpert.lms.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import net.rmitsolutions.mfexpert.lms.viewmodels.VillageInfoModel

@Dao
interface VillageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVillageInfo(villageInfoModel: VillageInfoModel): Long

    @Query("Select village from Village_info")
    fun getVillagesInfo(): List<String>
}