package net.rmitsolutions.mfexpert.lms.viewmodels

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "address_info")
class AddressInformationModel(var route: String, var location: String, var landmark: String, var meetingPlace: String,var premisesOwnerName: String, var houseNo: String,
                              var streetName: String,var villageName: String,var pincode: String){
    @PrimaryKey(autoGenerate = true)
    var id:Long?=null
    var primaryInfoId:Long?=null


}