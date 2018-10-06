package net.rmitsolutions.mfexpert.lms.viewmodels

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.ObservableField
import net.rmitsolutions.mfexpert.lms.models.CommonResult
import java.io.Serializable
import kotlin.collections.ArrayList

class Repayment {

    object RepaymentData {
        var repaymentDataList = ArrayList<RepaymentDetail>()
        var loanDetails = mutableListOf<LoanDetail>()
    }

    // Repayment Param Model
    class RepaymentParamsModel(val centerCode: String, val recoveryFor: String)

    class RepaymentModel {
        val id: Long? = null
        val memberCode: String? = null
        val memberName: String? = null
        val productName: String? = null
        val productId: Long? = null
        val pastDue: Double? = null
        val currentDue: Double? = null
        val otherCharges: Double? = null
        val paidAmount: Double? = null
        var total = ObservableField<Double>()
        var isChecked = ObservableField<Boolean>()
    }

    // Member Loan Details
    class MemberLoanDetails(val memberCode: String, val memberName: String, val loanDetails: MutableList<LoanDetail>) : CommonResult(), Serializable

    // Loan Details
    class LoanDetail : Serializable, BaseObservable() {
        val loanId: Long? = null
        val productId: Int? = null
        val productName: String? = null
        val loanAmount: Double? = 0.0
        val principleDue: Double? = 0.0
        val interestDue: Double? = 0.0
        val outstanding: Double? = 0.0
        val penalCharges: Double? = 0.0
        val adjustedAmount: Double? = 0.0
        var totalAmount = ObservableField<Double>()
        var bankAccNo: String? = null
        var bankName: String? = null
        var bankIfsc: String? = null
        val recoveredAmount: Double? = 0.0
        var preCloseTypeId: Int? = 0
        @Bindable
        var preCloseTypes = mutableListOf<PreCloseTypes>()
        @Bindable
        var preCloseTypeList = arrayListOf<String>()
    }

    // PreCloseTypes
    class PreCloseTypes(val id: Int, val name: String) : Serializable

    // Post Repayment Details
    class RepaymentDetail {
        var memberId: Long? = null
        var repaymentType: Int = 0
        var paidAmount: Double = 0.0
        var isPreClosure: Boolean = false
        var loanRepaymentDetails: MutableList<LoanRepaymentDetail>? = null
    }

    // Loan Repayment Details
    class LoanRepaymentDetail {
        var loanId: Long? = null
        var preClosureType: Int = 0
        var paidAmount: Double = 0.0
        var adjustedAmount: Double = 0.0
        var bankIFSC: String? = null
        var bankName: String? = null
        var bankAccountNo: String? = null
    }

    // Loan Prepayment type event handler
    interface RepaymentEventHandler {
        fun onPrepaymentTypeChanged(pos: Int)
    }

    // Repayment Dialog Model
    class RepaymentDialogParamsModel {
        var totalLoanAmounts = ObservableField<Double>()
    }
}