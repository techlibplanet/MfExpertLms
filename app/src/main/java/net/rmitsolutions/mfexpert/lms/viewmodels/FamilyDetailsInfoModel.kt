package net.rmitsolutions.mfexpert.lms.viewmodels

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "family_details")
class FamilyDetailsInfoModel (var name: String, var relation: String,var gender: String,
                              var dob: String,var age: String,var occupation: String,var literacy: String){
    @PrimaryKey(autoGenerate = true)
    var id:Long?=null
    var personInfoId:Long?=null
}