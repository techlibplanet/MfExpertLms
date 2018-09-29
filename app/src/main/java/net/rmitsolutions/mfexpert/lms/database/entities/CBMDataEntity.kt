package net.rmitsolutions.mfexpert.lms.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import net.rmitsolutions.mfexpert.lms.viewmodels.*

@Entity(tableName = "cbm_data")
class CBMDataEntity {
   @PrimaryKey
   var id:Long?=0

    var familyDetailsInfoList:MutableList<FamilyDetailModels>?= mutableListOf<FamilyDetailModels>()

    var familyDetailsInfo:FamilyDetailModels?=null
    var identificationInfoModel:IdentificationInfoModel?=null
    var personalInfoModel:PersonalInfoModel?=null
    var presentAddresInfo:AddressModel?=null
    var permanentAddresInfo:AddressModel?=null
}