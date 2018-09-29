package net.rmitsolutions.mfexpert.lms.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import net.rmitsolutions.mfexpert.lms.viewmodels.*

@Dao
interface CbmDao {
   /* @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCBMDataEntity(cbmDataEntity: CBMDataEntity?);*/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPersonalDetailsInfo(personalDetailInfo:PersonalInfoModel):Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFamily(family:FamilyDetailsInfoModel):Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPresentadd(address:AddressModel):Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertidentificationDoc(identification:IdentificationInfoModel):Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFamilyDetailsList(family:MutableList<FamilyDetailsInfoModel>)





    @Query("Select * from Personal_info")
    fun getData():List<PersonInfoWithOtherDetails>
}