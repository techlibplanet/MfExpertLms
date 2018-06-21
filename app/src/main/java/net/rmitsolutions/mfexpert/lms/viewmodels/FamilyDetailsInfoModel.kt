package net.rmitsolutions.mfexpert.lms.viewmodels

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import net.rmitsolutions.mfexpert.lms.database.entities.BaseEntity
@Entity(tableName = "family_details")
class FamilyDetailsInfoModel (var name: String, var relation: String,var gender: String,
                              var dob: String,var age: String,var occupation: String,var literacy: String){
    @PrimaryKey(autoGenerate = true)
    var id:Long?=null
    var personInfoId:Long?=null
}