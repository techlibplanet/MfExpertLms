package net.rmitsolutions.mfexpert.lms.viewmodels

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "center_address_info")
class CenterAddressInformationModel(var route: String, var location: String, var landmark: String, var meetingPlace: String, var premisesOwnerName: String, var houseNo: String,
                              var streetName: String, var villageName: String, var pincode: String, var latitude: String, var longitude: String, var timeStamp: String, var imageByteArray: ByteArray?){
    @PrimaryKey(autoGenerate = true)
    var id:Long?=null
    var primaryInfoId:Long?=null


}