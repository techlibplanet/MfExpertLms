package net.rmitsolutions.mfexpert.lms.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import net.rmitsolutions.mfexpert.lms.viewmodels.GroupModel

@Dao
interface GroupDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGroupInfo(groupInfoModel: GroupModel): Long
}