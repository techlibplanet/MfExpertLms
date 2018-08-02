package net.rmitsolutions.mfexpert.lms.network

import io.reactivex.Observable
import net.rmitsolutions.mfexpert.lms.database.entities.*
import net.rmitsolutions.mfexpert.lms.models.IdNameLong
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

/**
 * Created by Madhu on 24-Apr-2018.
 */
interface IMasters {

    @GET("Masters/GetRelations")
    fun getRelations(@Header("Authorization") authToken: String): Call<List<Relation>>

    @GET("Masters/GetDistricts")
    fun getDistricts(@Header("Authorization") authToken : String) : Call<List<District>>

    @GET("Masters/GetPrimaryKYCDetails")
    fun getPrimaryKycDetails(@Header("Authorization") authToken : String) : Call<List<PrimaryKYC>>

    @GET("Masters/GetSecondaryKYCDetails")
    fun getSecondaryKycDetails(@Header("Authorization") authToken: String) : Call<List<SecondaryKYC>>

    @GET("Masters/GetKYCDetails")
    fun getKycDetails(@Header("Authorization") authToken: String) : Call<List<KycDetails>>

    @GET("Masters/GetProducts")
    fun getProducts(@Header("Authorization") authToken: String) : Call<List<Products>>

    @GET("Masters/GetOccupations")
    fun getOccupations(@Header("Authorization") authToken: String) : Call<List<Occupation>>

    @GET("Masters/GetLitericyTypes")
    fun getLiteracyTypes(@Header("Authorization") authToken: String) : Call<List<Literacy>>

    @GET("Masters/GetPurposes")
    fun getPurposes(@Header("Authorization") authToken: String) : Call<List<LoanPurpose>>

    @GET("Masters/GetBanks")
    fun getBanks(@Header("Authorization") authToken: String) : Call<List<Banks>>

    @GET("Masters/GetNationalityDetails")
    fun getNationalityDetails(@Header("Authorization") authToken: String) : Call<List<Nationality>>

    @GET("Masters/GetReligionDetails")
    fun getReligionDetails(@Header("Authorization") authToken: String) : Call<List<Religion>>

    @GET("Masters/GetCasteDetails")
    fun getCasteDetails(@Header("Authorization") authToken: String) : Call<List<Caste>>

    @GET("Masters/GetHouseOwnershipDetails")
    fun getHouseOwnershipDetails(@Header("Authorization") authToken: String) : Call<List<HouseOwnership>>

    @GET("Masters/GetIncomeProofDetails")
    fun getIncomeProofDetails(@Header("Authorization") authToken: String) : Call<List<IncomeProof>>

    @GET("Masters/GetAssignCategory")
    fun getAssignCategory(@Header("Authorization") authToken: String) : Call<List<AssignCategory>>

    @GET("Masters/GetLoanCloseType")
    fun getLoanCloseType(@Header("Authorization") authToken: String) : Call<List<LoanCloseType>>

    @GET("Masters/GetMemberRejectionReasons")
    fun getMemberRejectionReasons(@Header("Authorization") authToken: String) : Call<List<MemberRejectionReasons>>

//    @GET("Masters/GetLoanRejectionReasons")
//    fun getLoanRejectionReasons(@Header("Authorization") authToken: String) : Observable<List<IdNameLong>>

    @GET("Masters/GetLoanRejectionReasons")
    fun getLoanRejectionReasons(@Header("Authorization") authToken: String) : Call<List<LoanRejectionReasons>>


    @GET("Masters/GetRelations")
    fun getRelation(@Header("Authorization") authToken: String) : Observable<List<IdNameLong>>









}