package net.rmitsolutions.mfexpert.lms.viewmodels

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "address")
class AddressModel (var houseNo: String, var street: String, var mandal: String, var city: String, var phone: String,
                    var district: String, var pincode: String, var districtId : Int){
    companion object {
        public var PRESENT_ADDRESS = 1
        public var PERMANENT_ADDRESS = 2
    }

    @PrimaryKey(autoGenerate = true)
    var id:Long?=null;
    var addressType:Int=PRESENT_ADDRESS;
    var permanentAddressSameAsPresent:Boolean?=null

    fun copyData(data:AddressModel){
        this.houseNo=data.houseNo
        this.street=data.street
        this.mandal=data.mandal
        this.city=data.city
        this.phone=data.phone
        this.district=data.district
        this.pincode=data.pincode
        this.districtId = data.districtId
    }
    var personInfoId:Long?=null


}