package net.rmitsolutions.mfexpert.lms.network

import io.reactivex.Observable
import net.rmitsolutions.mfexpert.lms.database.entities.*
import net.rmitsolutions.mfexpert.lms.models.IdNameLong
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface Masters {

    @GET("Masters/GetRelations")
    fun getRelations(@Header("Authorization") authToken: String) : Observable<List<IdNameLong>>

    @GET("Masters/GetDistricts")
    fun getDistricts(@Header("Authorization") authToken: String) : Observable<List<IdNameLong>>

    @GET("Masters/GetPrimaryKYCDetails")
    fun getPrimaryKycDetails(@Header("Authorization") authToken : String) : Observable<List<IdNameLong>>

    @GET("Masters/GetSecondaryKYCDetails")
    fun getSecondaryKycDetails(@Header("Authorization") authToken: String) : Observable<List<IdNameLong>>

    @GET("Masters/GetKYCDetails")
    fun getKycDetails(@Header("Authorization") authToken: String) : Observable<List<IdNameLong>>

    @GET("Masters/GetProducts")
    fun getProducts(@Header("Authorization") authToken: String) : Call<Observable<IdNameLong>>

    @GET("Masters/GetOccupations")
    fun getOccupations(@Header("Authorization") authToken: String) : Observable<List<IdNameLong>>

    @GET("Masters/GetLitericyTypes")
    fun getLiteracyTypes(@Header("Authorization") authToken: String) : Observable<List<IdNameLong>>

    @GET("Masters/GetPurposes")
    fun getPurposes(@Header("Authorization") authToken: String) : Observable<List<IdNameLong>>

    @GET("Masters/GetBanks")
    fun getBanks(@Header("Authorization") authToken: String) : Observable<List<IdNameLong>>

    @GET("Masters/GetNationalityDetails")
    fun getNationalityDetails(@Header("Authorization") authToken: String) : Observable<List<IdNameLong>>

    @GET("Masters/GetReligionDetails")
    fun getReligionDetails(@Header("Authorization") authToken: String) : Observable<List<IdNameLong>>

    @GET("Masters/GetCasteDetails")
    fun getCasteDetails(@Header("Authorization") authToken: String) : Observable<List<IdNameLong>>

    @GET("Masters/GetHouseOwnershipDetails")
    fun getHouseOwnershipDetails(@Header("Authorization") authToken: String) : Observable<List<IdNameLong>>

    @GET("Masters/GetIncomeProofDetails")
    fun getIncomeProofDetails(@Header("Authorization") authToken: String) : Observable<List<IdNameLong>>

    @GET("Masters/GetAssignCategory")
    fun getAssignCategory(@Header("Authorization") authToken: String) : Observable<List<IdNameLong>>

    @GET("Masters/GetLoanCloseType")
    fun getLoanCloseType(@Header("Authorization") authToken: String) : Observable<List<IdNameLong>>

    @GET("Masters/GetMemberRejectionReasons")
    fun getMemberRejectionReasons(@Header("Authorization") authToken: String) : Observable<List<IdNameLong>>

//    @GET("Masters/GetLoanRejectionReasons")
//    fun getLoanRejectionReasons(@Header("Authorization") authToken: String) : Observable<List<IdNameLong>>

    @GET("Masters/GetLoanRejectionReasons")
    fun getLoanRejectionReasons(@Header("Authorization") authToken: String) : Observable<List<IdNameLong>>



}