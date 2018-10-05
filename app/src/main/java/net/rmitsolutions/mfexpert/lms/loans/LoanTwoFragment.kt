package net.rmitsolutions.mfexpert.lms.loans

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import net.rmitsolutions.mfexpert.lms.R
import net.rmitsolutions.mfexpert.lms.databinding.LoanTwoLayoutBinding
import net.rmitsolutions.mfexpert.lms.helpers.toast
import net.rmitsolutions.mfexpert.lms.models.Globals
import net.rmitsolutions.mfexpert.lms.repayment.callback.TotalAmountCallback
import net.rmitsolutions.mfexpert.lms.repayment.RepaymentDialog
import net.rmitsolutions.mfexpert.lms.viewmodels.Repayment


class LoanTwoFragment : Fragment() {

    private var listener: OnFragmentInteractionListener? = null
    private lateinit var memberLoanDetails: Repayment.LoanDetail
    private lateinit var repaymentDialog: RepaymentDialog
    private lateinit var totalAmountCallback: TotalAmountCallback
    lateinit var dataBindingLoanTwo: LoanTwoLayoutBinding
    private var total = 0.0
    private var mView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repaymentDialog = RepaymentDialog()
        totalAmountCallback = TotalAmountCallback()
        totalAmountCallback.setListener(repaymentDialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        dataBindingLoanTwo = DataBindingUtil.inflate(inflater, R.layout.loan_two_layout, container, false)
        val view = dataBindingLoanTwo.root

        dataBindingLoanTwo.loanTwoVm = memberLoanDetails

        // Assigning view model for validation
        RepaymentDialog.ViewDialog.loanTwoVm = dataBindingLoanTwo.loanTwoVm

        // Add total and set total to loanTotalAmount
        total = memberLoanDetails.principleDue!! + memberLoanDetails.interestDue!! +
                memberLoanDetails.penalCharges!! + memberLoanDetails.adjustedAmount!!
        dataBindingLoanTwo.loanTwoVm!!.totalAmount.set(total)

        Repayment.RepaymentData.loanDetails.add(dataBindingLoanTwo.loanTwoVm!!)

        disableBankDetails(true)
        setPreCloseTypes()
        dataBindingLoanTwo.eventHandler = (object : Repayment.RepaymentEventHandler {
            override fun onPrepaymentTypeChanged(pos: Int) {
                if (pos < 0) {
                    dataBindingLoanTwo.loanTwoVm?.preCloseTypeId = 0
                    return
                }
                dataBindingLoanTwo.loanTwoVm?.preCloseTypeId = memberLoanDetails.preCloseTypes[pos].id

                when (memberLoanDetails.preCloseTypes[pos].name) {
                    "Select" -> {
                        disableBankDetails(true)
                        addTotalOnSelect()
                    }
                    "NPP" -> {
                        disableBankDetails(true)
                        addTotalOnOtherSelection()
                    }
                    "MDT" -> {
                        disableBankDetails(false)
                        addTotalOnOtherSelection()
                    }
                    "HDT" -> {
                        disableBankDetails(false)
                        addTotalOnOtherSelection()
                    }
                    else -> {
                        toast("Invalid Selection")
                    }
                }
            }
        })
        return view
    }

    private fun setPreCloseTypes() {
        for (data in memberLoanDetails.preCloseTypes) {
            dataBindingLoanTwo.loanTwoVm?.preCloseTypeList?.add(data.name)
        }
    }

    private fun addTotalOnSelect() {
        total = memberLoanDetails.principleDue!! + memberLoanDetails.interestDue!! + memberLoanDetails.penalCharges!! + memberLoanDetails.adjustedAmount!!
        dataBindingLoanTwo.loanTwoVm?.totalAmount?.set(Globals.getRoundOffDecimalFormat(total))
        totalAmountCallback.onTotalAmountChanged(mView)
    }

    private fun addTotalOnOtherSelection() {
        total = memberLoanDetails.outstanding!! + memberLoanDetails.interestDue!! + memberLoanDetails.penalCharges!! + memberLoanDetails.adjustedAmount!!
        dataBindingLoanTwo.loanTwoVm?.totalAmount?.set(Globals.getRoundOffDecimalFormat(total))
        totalAmountCallback.onTotalAmountChanged(mView)
    }

    private fun disableBankDetails(disable: Boolean) {
        dataBindingLoanTwo.loanTwoBankDetails = disable
        RepaymentDialog.ViewDialog.loanTwoBankDetails = disable
    }

    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        @JvmStatic
        fun newInstance(memberLoanDetails: Repayment.LoanDetail, view: View) =
                LoanTwoFragment().apply {
                    this.memberLoanDetails = memberLoanDetails
                    this.mView = view
                }
    }
}
