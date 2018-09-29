package net.rmitsolutions.mfexpert.lms.repayment.adapter

import android.content.*
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.*
import net.rmitsolutions.mfexpert.lms.Constants
import net.rmitsolutions.mfexpert.lms.MfExpertApp
import net.rmitsolutions.mfexpert.lms.R
import net.rmitsolutions.mfexpert.lms.dependency.components.DaggerInjectActivityComponent
import net.rmitsolutions.mfexpert.lms.models.Globals
import net.rmitsolutions.mfexpert.lms.repayment.callback.RepaymentDialogTabListener
import net.rmitsolutions.mfexpert.lms.network.IRepayment
import net.rmitsolutions.mfexpert.lms.repayment.RepaymentDialogTabs
import net.rmitsolutions.mfexpert.lms.repayment.RepaymentModel
import net.rmitsolutions.mfexpert.lms.repayment.callback.RepaymentDetailsCallback
import net.rmitsolutions.mfexpert.lms.repayment.callback.RepaymentDetailsListener
import net.rmitsolutions.mfexpert.lms.viewmodels.Repayment
import org.jetbrains.anko.find
import javax.inject.Inject
class ClientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), RepaymentDialogTabListener, RepaymentDetailsListener {

    private var context: Context? = null
    private lateinit var prePaymentCheckBox: CheckBox
    private lateinit var editTextRepaymentAmount: EditText
    @Inject
    lateinit var repayService: IRepayment
    private var paidAmount = 0.0
    var mPosition = 0

    fun bindView(context: Context, clientViewModels: RepaymentModel, position: Int) {
        this.context = context
        val depComponent = DaggerInjectActivityComponent.builder()
                .applicationComponent(MfExpertApp.applicationComponent)
                .build()

        depComponent.injectClientViewHolder(this)
        val clientId = itemView.find<TextView>(R.id.client_id)
        val clientName = itemView.find<TextView>(R.id.client_name)
        editTextRepaymentAmount = itemView.find<EditText>(R.id.edit_text_repayment_amount)

        clientId.text = clientViewModels.memberCode
        clientName.text = clientViewModels.memberName
        editTextRepaymentAmount.setText((clientViewModels.pastDue + clientViewModels.currentDue + clientViewModels.otherCharges).toString())

        prePaymentCheckBox = itemView.find<CheckBox>(R.id.prepayment_checkbox)

        prePaymentCheckBox.setOnClickListener {
            // Repayment Details Callback to get Loan Data
            mPosition = position
            val callback = RepaymentDetailsCallback()
            callback.setListener(this)
            callback.fetchMemberLoanDetails(context, clientViewModels.id, repayService)
        }
    }

    override fun onSuccess(memberLoanDetails: Repayment.MemberLoanDetails, clientId: Long) {
        for (data in memberLoanDetails.loanDetails) {
            val preCloseTypes = Repayment.PreCloseTypes(0, "Select")
            data.preCloseTypes.add(0, preCloseTypes)
        }
        if (prePaymentCheckBox.isChecked) {
            val manager = (context as AppCompatActivity).supportFragmentManager
            val ft = manager.beginTransaction()
            val prev = manager.findFragmentByTag("dialog")
            if (prev != null) {
                ft.remove(prev)
            }
            ft.addToBackStack(null)

            val bundle = Bundle()
            bundle.putLong("ClientId", clientId)
            bundle.putSerializable("MemberLoanDetails", memberLoanDetails)

            // Create and show the dialog.
            val dialogFragment = RepaymentDialogTabs(this)
            dialogFragment.arguments = bundle
            dialogFragment.show(ft, "dialog")
        } else {
            //context.toast("CheckBox in not checked")
        }
    }

    // Add the Loans Data to repayment data list on press dialog Ok Button
    override fun onComplete(result: String, clientId: Long) {
        if (result == Constants.PREPAYMENT_CHECKED) {
            prePaymentCheckBox.isChecked = true
            val loanDetailsList = ArrayList<Repayment.LoanRepaymentDetail>()
            for (data in Repayment.RepaymentData.loanDetails) {
                val loanDetails = Repayment.LoanRepaymentDetail()
                loanDetails.loanId = data.loanId
                loanDetails.preClosureType = data.preCloseTypeId
                loanDetails.paidAmount = data.totalAmount
                loanDetails.adjustedAmount = data.adjustedAmount
                loanDetails.bankAccountNo = data.bankAccNo
                loanDetails.bankIFSC = data.bankIfsc
                loanDetails.bankName = data.bankName
                loanDetailsList.add(loanDetails)

            }
            for (repay in Repayment.RepaymentData.repaymentDataList) {
                if (repay.memberId == clientId) {
                    repay.repaymentType = 1
                    repay.paidAmount = editTextRepaymentAmount.text.toString().toDouble()
                    repay.isPreClosure = true
                    repay.loanRepaymentDetails = loanDetailsList
                    break
                }
            }
            Repayment.RepaymentData.loanDetails.clear()
        } else {
            prePaymentCheckBox.isChecked = false
        }
    }

    // On getting total amount from totalAmountTextFromDialog
    override fun onGettingTotalAmount(amount: Double) {
        paidAmount = amount
        editTextRepaymentAmount.setText(Globals.getRoundOffDecimalFormat(paidAmount).toString())
    }

}