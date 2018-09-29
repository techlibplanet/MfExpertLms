package net.rmitsolutions.mfexpert.lms.viewmodels

import java.util.*

class CBMember {

    inner class CBMemberPersonalInfo {
        var firstName: String? = null
        var middleName: String? = null
        var lastName: String? = null
        var nameAsPerKYC: String? = null
        var gender: Byte = 0
        var martialStatus: Byte = 0
        var dobAsPerKYC: String? = null
        var age: Int? = 0
        var mobileNo: String? = null
        var ageUptoDate: String? = null
        var applicationDate: String? = null
        var sameAsCurrentAddress: Boolean = false
        var primaryKYCId: Int = 0
        var primaryKYCNo: String? = null
        var secondaryKYCId: Int = 0
        var secondaryKYCNo: String? = null
        var purposeId: Int = 0
        var cbMemberDate: String? = null
        var currentAddress : CBMemberCurrentAddress? = null
        var permanentAddress : CBMemberPermanentAddress? = null
        var cbMemberFamilyDetails : ArrayList<CbMemberFamilyDetails>? = null
        var cbMemberImage :String? = null
    }

    class CBMemberCurrentAddress {
        var houseNo: String? = null
        var street: String? = null
        var mandal: String? = null
        var city: String? = null
        var mobileNo: String? = null
        var district: Int = 0
        var pinCode: String? = null
    }

    class CBMemberPermanentAddress {
        var houseNo: String? = null
        var street: String? = null
        var mandal: String? = null
        var city: String? = null
        var mobileNo: String? = null
        var district: Int = 0
        var pinCode: String? = null
    }

    class CbMemberFamilyDetails {
        var priority: Int = 0
        var name: String? = null
        var relationId: Int = 0
        var dateOfBirth: String? = null
        var age: Int = 0
        var gender: Int = 0
        var occupationId: Int = 0
        var literacyId: Int = 0
    }


//    class Loan(val memberId:Int, val name:String, val loanDetails:Array<IdNameLong>)

}