package net.rmitsolutions.mfexpert.lms.viewmodels

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "Identification_info")
class IdentificationInfoModel (var primaryKYC: String, var docNo: String, var secondaryKYC: String, var secondaryDocNo: String, var loanPurpose: String, var latitude: String, var longitude: String, var timeStamp: String, var imageByteArray: ByteArray?){
    @PrimaryKey(autoGenerate = true)
    var id:Long?=null
    var personInfoId:Long?=null
}
