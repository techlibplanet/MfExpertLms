package net.rmitsolutions.mfexpert.lms.repayment.adapter

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import net.rmitsolutions.mfexpert.lms.Constants
import net.rmitsolutions.mfexpert.lms.MfExpertApp
import net.rmitsolutions.mfexpert.lms.databinding.RepaymentActivityBinding
import net.rmitsolutions.mfexpert.lms.dependency.components.DaggerInjectActivityComponent
import net.rmitsolutions.mfexpert.lms.models.Globals
import net.rmitsolutions.mfexpert.lms.network.IRepayment
import net.rmitsolutions.mfexpert.lms.repayment.RepaymentDialog
import net.rmitsolutions.mfexpert.lms.repayment.callback.RepaymentDetailsCallback
import net.rmitsolutions.mfexpert.lms.repayment.callback.RepaymentDetailsListener
import net.rmitsolutions.mfexpert.lms.repayment.callback.RepaymentDialogListener
import net.rmitsolutions.mfexpert.lms.viewmodels.ClientHandler
import net.rmitsolutions.mfexpert.lms.viewmodels.Repayment
import javax.inject.Inject

class ClientViewHolder(val dataBinding: RepaymentActivityBinding) : RecyclerView.ViewHolder(dataBinding.root), RepaymentDetailsListener, RepaymentDialogListener {

    @Inject
    lateinit var repayService: IRepayment
    private lateinit var context: Context

    fun bind(context: Context, holder: ClientViewHolder, model: Repayment.RepaymentModel) {
        this.context = context
        val depComponent = DaggerInjectActivityComponent.builder()
                .applicationComponent(MfExpertApp.applicationComponent)
                .build()
        depComponent.injectClientHolder(this)

        dataBinding.repaymentModel = model
        RepaymentDialog.ViewDialog.repaymentActivityVm = dataBinding.repaymentModel
        val total = model.pastDue!! + model.currentDue!! + model.otherCharges!!
        dataBinding.repaymentModel?.total?.set(total)

        dataBinding.clientHandler = (object : ClientHandler{
            override fun onClick(view: View) {
                val repaymentDetailsCallback = RepaymentDetailsCallback()
                repaymentDetailsCallback.setListener(this@ClientViewHolder)
                repaymentDetailsCallback.fetchMemberLoanDetails(context, model.id!!, repayService)
            }
        })
    }


    override fun onSuccess(memberLoanDetails: Repayment.MemberLoanDetails, id: Long) {
        for (data in memberLoanDetails.loanDetails) {
            val preCloseTypes = Repayment.PreCloseTypes(0, "Select")
            data.preCloseTypes.add(0, preCloseTypes)
        }
        if (dataBinding.repaymentModel?.isChecked?.get()!!) {
            val manager = (context as AppCompatActivity).supportFragmentManager
            val ft = manager.beginTransaction()
            val prev = manager.findFragmentByTag("dialog")
            if (prev != null) {
                ft.remove(prev)
            }
            ft.addToBackStack(null)

            val bundle = Bundle()
            bundle.putLong("ClientId", id)
            bundle.putSerializable("MemberLoanDetails", memberLoanDetails)

            // Create and show the dialog.
            val dialogFragment = RepaymentDialog(this)
            dialogFragment.arguments = bundle
            dialogFragment.show(ft, "dialog")
        } else {
            //context.toast("CheckBox in not checked")
        }
    }

    // Add the Loans Data to repayment data list on press dialog Ok Button
    override fun onComplete(result: String, clientId: Long) {
        if (result == Constants.PREPAYMENT_CHECKED) {
            dataBinding.repaymentModel?.isChecked?.set(true)
            val loanDetailsList = ArrayList<Repayment.LoanRepaymentDetail>()
            for (data in Repayment.RepaymentData.loanDetails) {
                val loanDetails = Repayment.LoanRepaymentDetail()
                loanDetails.loanId = data.loanId
                loanDetails.preClosureType = data.preCloseTypeId!!
                loanDetails.paidAmount = data.totalAmount.get()!!
                loanDetails.adjustedAmount = data.adjustedAmount!!
                loanDetails.bankAccountNo = data.bankAccNo
                loanDetails.bankIFSC = data.bankIfsc
                loanDetails.bankName = data.bankName
                loanDetailsList.add(loanDetails)

            }
            setRepaymentData(clientId, true, loanDetailsList)
        } else {
            dataBinding.repaymentModel?.isChecked?.set(false)
            setRepaymentData(clientId, false, null)
        }
    }

    private fun setRepaymentData(clientId: Long, isPreClosure: Boolean, loanDetailsList: ArrayList<Repayment.LoanRepaymentDetail>?){
        for (repay in Repayment.RepaymentData.repaymentDataList) {
            if (repay.memberId == clientId) {
                repay.repaymentType = 1
                repay.paidAmount = dataBinding.repaymentModel?.total?.get()!!
                repay.isPreClosure = isPreClosure
                if (isPreClosure){
                    repay.loanRepaymentDetails = loanDetailsList
                }
                break
            }
        }
        Repayment.RepaymentData.loanDetails.clear()
    }

    // On getting total amount from totalAmountTextFromDialog
    override fun onGettingTotalAmount(amount: Double) {
        dataBinding.repaymentModel?.total?.set(Globals.getRoundOffDecimalFormat(amount))
    }
}