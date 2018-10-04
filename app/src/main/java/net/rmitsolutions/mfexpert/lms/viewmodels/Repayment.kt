package net.rmitsolutions.mfexpert.lms.viewmodels

import androidx.databinding.ObservableField
import net.rmitsolutions.mfexpert.lms.models.CommonResult
import java.io.Serializable
import kotlin.collections.ArrayList

class Repayment {

    object RepaymentData{
        var repaymentDataList = ArrayList<RepaymentDetail>()
        var loanDetails = mutableListOf<LoanDetails>()
    }


    // Repayment Param Model
    class RepaymentParamsModel(val centerCode:String, val recoveryFor:String)


    class RepaymentModel{
        val id: Long? = null
        val memberCode: String? = null
        val memberName: String? = null
        val productName: String? = null
        val productId: Long? = null
        val pastDue: Double? = null
        val currentDue: Double?=null
        val otherCharges: Double? = null
        val paidAmount: Double? = null
        var total = ObservableField<Double>()
    }



    // Member Loan Details
    class MemberLoanDetails(val memberCode: String, val memberName: String, val loanDetails: MutableList<LoanDetails>) : CommonResult(), Serializable

    class LoanDetails(val loanId: Long, val productId: Int, val productName: String, val loanAmount: Double,
                      val principleDue: Double, val interestDue: Double, val outstanding: Double, val penalCharges: Double,
                      val adjustedAmount: Double, var totalAmount: Double,
                      var bankAccNo: String, var bankName: String, var bankIfsc: String,
                      val recoveredAmount: Double,var preCloseTypeId : Int, val preCloseTypes: MutableList<PreCloseTypes>) : Serializable


    class LoanDetail : Serializable{
        val loanId: Long? = null
        val productId: Int? = null
        val productName: String? = null
        val loanAmount: Double? = null
        val principleDue: Double? = null
        val interestDue: Double? = null
        val outstanding: Double? = null
        val penalCharges: Double? = null
        val adjustedAmount: Double? = null
        var totalAmount = ObservableField<Double>()
        var bankAccNo: String? = null
        var bankName: String? = null
        var bankIfsc: String? = null
        val recoveredAmount: Double? = null
        var preCloseTypeId : Int? = null
        val preCloseTypes: MutableList<PreCloseTypes>? = null
    }

    class PreCloseTypes(val id: Int, val name: String) : Serializable


    // Post Repayment Details
    class RepaymentDetail {
        var memberId: Long? = null
        var repaymentType: Int = 0
        var paidAmount: Double = 0.0
        var isPreClosure: Boolean = false
        var loanRepaymentDetails: MutableList<LoanRepaymentDetail>? = null
    }

    class LoanRepaymentDetail {
        var loanId: Long? = null
        var preClosureType: Int = 0
        var paidAmount: Double = 0.0
        var adjustedAmount: Double = 0.0
        var bankIFSC: String? = null
        var bankName: String? = null
        var bankAccountNo: String? = null
    }

}