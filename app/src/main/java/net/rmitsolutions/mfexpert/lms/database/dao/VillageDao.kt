package net.rmitsolutions.mfexpert.lms.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import net.rmitsolutions.mfexpert.lms.viewmodels.VillageInfoModel

@Dao
interface VillageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVillageInfo(villageInfoModel: VillageInfoModel): Long

    @Query("Select village from Village_info")
    fun getVillagesInfo(): List<String>
}