package net.rmitsolutions.mfexpert.lms.network

import io.reactivex.Observable
import net.rmitsolutions.mfexpert.lms.viewmodels.CBMember
import net.rmitsolutions.mfexpert.lms.models.CommonResult
import retrofit2.http.*

interface ICBMember {

    @POST("CBMember/Add")
    fun addCBMember(@Header("Authorization") authToken: String, @Body cbMember : CBMember.CBMemberPersonalInfo): Observable<CommonResult>


}