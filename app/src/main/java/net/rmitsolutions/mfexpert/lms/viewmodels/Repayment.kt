package net.rmitsolutions.mfexpert.lms.viewmodels

import android.os.Parcelable
import android.view.View
import androidx.databinding.Bindable
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import java.util.*
import io.reactivex.Observable
import net.rmitsolutions.mfexpert.lms.databinding.LoanFourLayoutBinding
import net.rmitsolutions.mfexpert.lms.models.CommonResult
import net.rmitsolutions.mfexpert.lms.repayment.RepaymentModel
import java.io.Serializable
import kotlin.collections.ArrayList

class Repayment {

    object RepaymentData{
        var repaymentDataList = ArrayList<RepaymentDetail>()
        var loanDetails = mutableListOf<LoanDetails>()
    }



    // Member Loan Details
    class MemberLoanDetails(val memberCode: String, val memberName: String, val loanDetails: MutableList<LoanDetails>) : CommonResult(), Serializable

    class LoanDetails(val loanId: Long, val productId: Int, val productName: String, val loanAmount: Double,
                      val principleDue: Double, val interestDue: Double, val outstanding: Double, val penalCharges: Double,
                      val adjustedAmount: Double, var totalAmount: Double,
                      var bankAccNo: String, var bankName: String, var bankIfsc: String,
                      val recoveredAmount: Double,var preCloseTypeId : Int, val preCloseTypes: MutableList<PreCloseTypes>)

    class PreCloseTypes(val id: Int, val name: String)


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