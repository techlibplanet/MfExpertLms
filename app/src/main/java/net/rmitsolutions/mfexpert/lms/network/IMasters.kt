package net.rmitsolutions.mfexpert.lms.network

import io.reactivex.Observable
import net.rmitsolutions.mfexpert.lms.models.IdNameLong
import retrofit2.http.GET
import retrofit2.http.Header

/**
 * Created by Madhu on 24-Apr-2018.
 */
interface IMasters {
    @GET("Masters/GetRelations")
    fun getRelations(@Header("Authorization") authToken: String): Observable<List<IdNameLong>>
}