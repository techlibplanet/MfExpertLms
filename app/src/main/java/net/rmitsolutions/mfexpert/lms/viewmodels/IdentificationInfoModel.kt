package net.rmitsolutions.mfexpert.lms.viewmodels

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Identification_info")
class IdentificationInfoModel (var primaryKYC: String, var docNo: String, var secondaryKYC: String, var secondaryDocNo: String, var loanPurpose: String, var latitude: String, var longitude: String, var timeStamp: String, var cbmImageByteArray: ByteArray?, var primaryKycImageByteArray : ByteArray?, var secondaryKycImageByteArray : ByteArray?){
    @PrimaryKey(autoGenerate = true)
    var id:Long?=null
    var personInfoId:Long?=null
}
