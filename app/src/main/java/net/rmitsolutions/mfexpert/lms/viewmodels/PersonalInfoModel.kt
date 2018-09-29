package net.rmitsolutions.mfexpert.lms.viewmodels

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "Personal_info")
class PersonalInfoModel (var firstName: String, var middleName: String, var surName: String, var nameAsPerKYC: String, var gender: String,
                         var maritalStatus: String, var dob: String, var age: String, var ageAsOn: String, var mobile: String, @PrimaryKey(autoGenerate = true) var id: Long? = null){

   /* @Relation(parentColumn = "id", entityColumn = "personInfoId")
    var addressList:List<AddressModel>?=null
    *//*get()=  address
     set(value){
         this.address =value;
     }*//*
    @Relation(parentColumn = "id", entityColumn = "personInfoId")
    var familyDetails:List<FamilyDetailsInfoModel>?=null

    @Relation(parentColumn = "id", entityColumn = "personInfoId")
    var identificationInfo:List<IdentificationInfoModel>?=null*/
}


