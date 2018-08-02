package net.rmitsolutions.mfexpert.lms

import net.openid.appauth.ResponseTypeValues
import net.rmitsolutions.appauthwebview.AppAuthWebViewData
import net.rmitsolutions.mfexpert.lms.models.AccessToken
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object Constants {
   /* const val BASE_URL = "http://10.0.2.2:5000/"
    const val API_BASE_URL = "http://10.0.2.2:6002/api/"*/

    const val BASE_URL = "http://183.82.2.126:6002/"
//    const val API_BASE_URL = "http://183.82.2.126:6002/api/"
    const val API_BASE_URL = "http://183.82.2.126:7002/api/"

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

    const val TOKEN_REFRESH_SUCCESS = "SUCCESS"
    const val TOKEN_REFRESH_FAILED = "FAILED"

    val UNAUTHORIZED_CODE = 401

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
    const val ACCOUNT_AUTHORITY_KYC_DETAILS = "net.rmitsolutions.mfexpert.kycdetailssyncprovider"
    const val ACCOUNT_AUTHORITY_ASSIGN_CATEGORY = "net.rmitsolutions.mfexpert.assigncategorysyncprovider"
    const val ACCOUNT_AUTHORITY_BANKS = "net.rmitsolutions.mfexpert.banksyncprovider"
    const val ACCOUNT_AUTHORITY_CASTE = "net.rmitsolutions.mfexpert.castesyncprovider"
    const val ACCOUNT_AUTHORITY_HOUSE_OWNERSHIP = "net.rmitsolutions.mfexpert.houseownershipsyncprovider"
    const val ACCOUNT_AUTHORITY_INCOME_PROOF = "net.rmitsolutions.mfexpert.incomeproofsyncprovider"
    const val ACCOUNT_AUTHORITY_LOAN_CLOSE_TYPE = "net.rmitsolutions.mfexpert.loanclosetypesyncprovider"
    const val ACCOUNT_AUTHORITY_LOAN_REJECTION = "net.rmitsolutions.mfexpert.loanrejectionsyncprovider"
    const val ACCOUNT_AUTHORITY_MEMBER_REJECTION = "net.rmitsolutions.mfexpert.memberrejectionsyncprovider"
    const val ACCOUNT_AUTHORITY_NATIONALITY = "net.rmitsolutions.mfexpert.nationalitysyncprovider"
    const val ACCOUNT_AUTHORITY_PRODUCTS = "net.rmitsolutions.mfexpert.productsyncprovider"
    const val ACCOUNT_AUTHORITY_RELIGION = "net.rmitsolutions.mfexpert.religionsyncprovider"



    const val AUTH_TOKEN_TYPE_FULL_ACCESS = "Full access"
    const val AUTH_TOKEN_TYPE_FULL_ACCESS_LABEL = "Full access to an mfExpert account"



    fun getAppAuthWebViewData(): AppAuthWebViewData {
        val data = AppAuthWebViewData()
        data.clientId = "lmsMobile"
        data.authorizationEndpointUri = BASE_URL + "connect/authorize"
        data.clientSecret = ""
        data.discoveryUri = "n"
        data.redirectLoginUri = BASE_URL + "Home/LoginSuccess"
        data.redirectLogoutUri = BASE_URL + "Home/LogoutSuccess"
        data.scope = "openid profile offline_access lmsApi"
        data.tokenEndpointUri = BASE_URL + "connect/token"
        data.endSessionEndpointUri = BASE_URL + "connect/endsession"
        data.registrationEndpointUri = "n"
        data.responseType = ResponseTypeValues.CODE
        data.isGenerateCodeVerifier = true
        return data
    }

    const val DISPLAY_FULL_DATE_FORMAT = "dd-MM-yyyy hh:mm:ss a"

    fun getFormatDate(): String? {
        val calendar = Calendar.getInstance()
        val date = calendar.time
        val formatter : DateFormat = SimpleDateFormat(DISPLAY_FULL_DATE_FORMAT)
        return formatter.format(date)
    }



}