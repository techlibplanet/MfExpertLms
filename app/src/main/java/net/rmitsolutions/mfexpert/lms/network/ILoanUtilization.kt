package net.rmitsolutions.mfexpert.lms.network

import io.reactivex.Observable
import net.rmitsolutions.mfexpert.lms.models.BaseModel
import net.rmitsolutions.mfexpert.lms.models.IdNameLong
import net.rmitsolutions.mfexpert.lms.viewmodels.LoanUtilizationSaveModel
import net.rmitsolutions.mfexpert.lms.viewmodels.Loans
import retrofit2.http.*

interface ILoanUtilization {

    @GET("LoanUtilization/GetMemberLoanDetail/{code}")
    fun getMemberLoanDetail(@Header("Authorization") authToken: String ,@Path("code") memberId: String) : Observable<Loans.Loan>

    @GET("LoanUtilization/GetLastVerificationDate/{loanId}")
    fun getLastVerificationDate(@Header("Authorization") authToken: String, @Path("loanId") loanId: String) : Observable<BaseModel>


    @POST("LoanUtilization")
    fun postLoanUtilizationData(@Header("Authorization") authToken: String ,@Body() loanUtilizationSaveModel: LoanUtilizationSaveModel) : Observable<Loans.LoanUtilizationResponse>




}