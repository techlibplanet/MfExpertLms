package net.rmitsolutions.mfexpert.lms.database.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import net.rmitsolutions.mfexpert.lms.viewmodels.*

@Entity(tableName = "cbm_data")
class CBMDataEntity {
   @PrimaryKey
   var id:Long?=0

    var familyDetailsInfoList:MutableList<FamilyDetailsInfoModel>?= mutableListOf<FamilyDetailsInfoModel>()

    var familyDetailsInfo:FamilyDetailsInfoModel?=null
    var identificationInfoModel:IdentificationInfoModel?=null
    var personalInfoModel:PersonalInfoModel?=null
    var presentAddresInfo:AddressModel?=null
    var permanentAddresInfo:AddressModel?=null
}