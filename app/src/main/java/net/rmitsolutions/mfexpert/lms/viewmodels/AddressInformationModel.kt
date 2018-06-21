package net.rmitsolutions.mfexpert.lms.viewmodels

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "address_info")
class AddressInformationModel(var route: String, var location: String, var landmark: String, var meetingPlace: String,var premisesOwnerName: String, var houseNo: String,
                              var streetName: String,var villageName: String,var pincode: String){
    @PrimaryKey(autoGenerate = true)
    var id:Long?=null
    var primaryInfoId:Long?=null


}