package net.rmitsolutions.mfexpert.lms.viewmodels

import androidx.room.Embedded
import androidx.room.Relation

class PersonInfoWithOtherDetails {
   @Embedded
   var pesonInfoDetails:PersonalInfoModel?=null;

    @Relation(parentColumn = "id", entityColumn = "personInfoId")
    var addressList:List<AddressModel>?=null

    @Relation(parentColumn = "id", entityColumn = "personInfoId")
    var familyDetails:List<FamilyDetailsInfoModel>?=null

    @Relation(parentColumn = "id", entityColumn = "personInfoId")
    var identificationInfo:List<IdentificationInfoModel>?=null

}