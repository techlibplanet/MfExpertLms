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

    @GET("Masters/GetOccupations")
    fun getOccupations(@Header("Authorization") authToken: String) : Call<List<Occupation>>

    @GET("Masters/GetLitericyTypes")
    fun getLiteracyTypes(@Header("Authorization") authToken: String) : Call<List<Literacy>>

    @GET("Masters/GetPurposes")
    fun getPurposes(@Header("Authorization") authToken: String) : Call<List<LoanPurpose>>

    @GET("Masters/GetBanks")
    fun getBanks(@Header("Authorization") authToken: String) : Call<List<Banks>>

    @GET("Masters/GetReligionDetails")
    fun getReligionDetails(@Header("Authorization") authToken: String) : Call<List<Religion>>

    @GET("Masters/GetCasteDetails")
    fun getCasteDetails(@Header("Authorization") authToken: String) : Call<List<Caste>>

    @GET("Masters/GetHouseOwnershipDetails")
    fun getHouseOwnershipDetails(@Header("Authorization") authToken: String) : Call<List<HouseOwnership>>

    @GET("Masters/GetIncomeProofDetails")
    fun getIncomeProofDetails(@Header("Authorization") authToken: String) : Call<List<IncomeProof>>

    @GET("Masters/GetRelations")
    fun getRelation(@Header("Authorization") authToken: String) : Observable<List<IdNameLong>>

    @GET("Masters/GetDistricts")
    fun getDistrict(@Header("Authorization") authToken: String) : Observable<List<IdNameLong>>



}