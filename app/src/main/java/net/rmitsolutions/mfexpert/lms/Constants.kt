package net.rmitsolutions.mfexpert.lms

import android.view.View
import net.rmitsolutions.mfexpert.lms.models.AccessToken
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object Constants {
   /* const val BASE_URL = "http://10.0.2.2:5000/"
    const val API_BASE_URL = "http://10.0.2.2:6002/api/"*/

    const val BASE_URL = "http://183.82.2.126:6002/"
//    const val API_BASE_URL = "http://183.82.2.126:6002/api/"
    const val API_BASE_URL = "http://183.82.2.126:7100/api/"

    const val CONNECTION_TIMEOUT: Long = 60
    const val READ_TIMEOUT: Long = 60

    const val GRANT_TYPE = "password"
    const val REFRESH_TOKEN_GRANT_TYPE = "refresh_token"

    const val CLIENT_ID = "lmsMobile"
    const val CLIENT_SECRET = "lmsMobile@rmit"

    const val ACCOUNT_SCOPE ="openid"
    const val API_SCOPE = "lmsApi"

    const val OFFLINE_ACCESS_SCOPE = "offline_access"

    var TOKEN_OBJECT_KEY = "TOKEN_OBJECT_KEY"

    const val TOKEN_REFRESH_STATUS = "TOKEN_REFRESH_STATUS"
    const val TOKEN_REFRESH_SUCCESS = "TOKEN_REFRESH_SUCCESS"
    const val TOKEN_REFRESH_FAILED = "TOKEN_REFRESH_FAILED"

    const val PREPAYMENT_CHECKED = "PREPAYMENT_CHECKBOX_CHECKED"
    const val PREPAYMENT_UNCHECKED = "PREPAYMENT_CHECKBOX_UNCHECKED"

    const val UNAUTHORIZED_CODE = "401"
    const val UNAUTHORIZED = "Unauthorized"

    const val SCAN_DOCUMENT = "SCAN_DOCUMENT"


    var ACCESS_TOKEN: String = ""
    var REFRESH_TOKEN : String = ""


    var accessToken: AccessToken? = null

    const val ARG_ACCOUNT_TYPE = "ACCOUNT_TYPE"
    const val ARG_AUTH_TYPE = "AUTH_TYPE"
    const val ARG_ACCOUNT_NAME = "ACCOUNT_NAME"
    const val ARG_IS_ADDING_NEW_ACCOUNT = "IS_ADDING_ACCOUNT"
    const val PARAM_USER_PASS = "REFRESH_TOKEN"

    const val ACCOUNT_TYPE = "net.rmitsolutions.mfexpert"
    const val ACCOUNT_NAME = "mfExpert"
    const val ACCOUNT_AUTHORITY_DISTRICTS = "net.rmitsolutions.mfexpert.districtsyncprovider"
    const val ACCOUNT_AUTHORITY_RELATIONS = "net.rmitsolutions.mfexpert.relationsyncprovider"
    const val ACCOUNT_AUTHORITY_OCCUPATIONS = "net.rmitsolutions.mfexpert.occupationsyncprovider"
    const val ACCOUNT_AUTHORITY_LITERACY = "net.rmitsolutions.mfexpert.literacysyncprovider"
    const val ACCOUNT_AUTHORITY_PRIMARY_KYC = "net.rmitsolutions.mfexpert.primarykycsyncprovider"
    const val ACCOUNT_AUTHORITY_SECONDARY_KYC = "net.rmitsolutions.mfexpert.secondarykycsyncprovider"
    const val ACCOUNT_AUTHORITY_LOAN_PURPOSE = "net.rmitsolutions.mfexpert.loanpurposesyncprovider"
    const val ACCOUNT_AUTHORITY_BANKS = "net.rmitsolutions.mfexpert.banksyncprovider"
    const val ACCOUNT_AUTHORITY_CASTE = "net.rmitsolutions.mfexpert.castesyncprovider"
    const val ACCOUNT_AUTHORITY_HOUSE_OWNERSHIP = "net.rmitsolutions.mfexpert.houseownershipsyncprovider"
    const val ACCOUNT_AUTHORITY_INCOME_PROOF = "net.rmitsolutions.mfexpert.incomeproofsyncprovider"
    const val ACCOUNT_AUTHORITY_RELIGION = "net.rmitsolutions.mfexpert.religionsyncprovider"



    const val AUTH_TOKEN_TYPE_FULL_ACCESS = "Full access"
    const val AUTH_TOKEN_TYPE_FULL_ACCESS_LABEL = "Full access to an mfExpert account"


    const val DISPLAY_FULL_DATE_FORMAT = "dd-MM-yyyy hh:mm:ss a"

    fun getFormatDate(): String? {
        val calendar = Calendar.getInstance()
        val date = calendar.time
        val formatter : DateFormat = SimpleDateFormat(DISPLAY_FULL_DATE_FORMAT)
        return formatter.format(date)
    }

    fun getDate(date : Date): String? {
        val formatter : DateFormat = SimpleDateFormat("yyyy-MM-dd")
        return formatter.format(date)
    }

    const val REQUEST_CHECK_SETTINGS = 0x1

    var districtIdPresent : Int = 0
    var familyDetailsRelationId : Int = 0
    var familyDetailsOccupationId : Int = 0
    var familyDetailsLiteracyId : Int = 0
    var primaryKycId : Int = 0
    var secondaryKycId : Int = 0
    var loanPurposeId : Int = 0

    var addParent : Boolean = false

    var zipCodePattern = "\\d{6}(-\\d{4})?"

    fun zipCodeValidate(zip : String): Boolean {
        return zip.matches(zipCodePattern.toRegex())
    }





}