package net.rmitsolutions.mfexpert.lms.network

import io.reactivex.Observable
import net.rmitsolutions.mfexpert.lms.models.BaseModel
import net.rmitsolutions.mfexpert.lms.repayment.RepaymentModel
import net.rmitsolutions.mfexpert.lms.repayment.RepaymentParamsModel
import net.rmitsolutions.mfexpert.lms.viewmodels.Repayment
import retrofit2.http.*

interface IRepayment {
    @POST("Repayment/GroupLoansList")
    fun getGroupLoansList(@Header("Authorization") authToken: String, @Body repayParams: RepaymentParamsModel): Observable<List<RepaymentModel>>

    @POST("Repayment/MemberLoanDetails")
    fun getMemberLoanDetails(@Header("Authorization") authToken: String, @Query("memberId") memberId: Long): Observable<Repayment.MemberLoanDetails>

    @POST("Repayment/Post")
    fun postRepaymentDetails(@Header("Authorization") authToken: String, @Body repaymentDetails: ArrayList<Repayment.RepaymentDetail>): Observable<BaseModel>


}