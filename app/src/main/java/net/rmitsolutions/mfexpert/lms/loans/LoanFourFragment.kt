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
import net.rmitsolutions.mfexpert.lms.databinding.DialogRepaymentBinding
import net.rmitsolutions.mfexpert.lms.databinding.LoanFourLayoutBinding
import net.rmitsolutions.mfexpert.lms.helpers.toast
import net.rmitsolutions.mfexpert.lms.models.Globals
import net.rmitsolutions.mfexpert.lms.repayment.callback.TotalAmountCallback
import net.rmitsolutions.mfexpert.lms.repayment.RepaymentDialog
import net.rmitsolutions.mfexpert.lms.viewmodels.Repayment

class LoanFourFragment : Fragment() {

    private var listener: OnFragmentInteractionListener? = null
    private lateinit var memberLoanDetails: Repayment.LoanDetail
    lateinit var dataBindingLoanFour: LoanFourLayoutBinding
    private var total = 0.0
    private lateinit var repaymentDialog: RepaymentDialog
    private lateinit var totalAmountCallback: TotalAmountCallback
    private var binding: DialogRepaymentBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repaymentDialog = RepaymentDialog()
        totalAmountCallback = TotalAmountCallback()
        totalAmountCallback.setListener(repaymentDialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        dataBindingLoanFour = DataBindingUtil.inflate(inflater, R.layout.loan_four_layout, container, false)
        val view = dataBindingLoanFour.root

        dataBindingLoanFour.loanFourVm = memberLoanDetails

        // Assigning view model for validation
        RepaymentDialog.ViewDialog.loanFourVm = dataBindingLoanFour.loanFourVm

        total = memberLoanDetails.principleDue!! + memberLoanDetails.interestDue!! +
                memberLoanDetails.penalCharges!! + memberLoanDetails.adjustedAmount!!
        dataBindingLoanFour.loanFourVm!!.totalAmount.set(total)

        Repayment.RepaymentData.loanDetails.add(dataBindingLoanFour.loanFourVm!!)

        disableBankDetails(true)
        setPreCloseTypes()
        dataBindingLoanFour.eventHandler = (object : Repayment.RepaymentEventHandler {
            override fun onPrepaymentTypeChanged(pos: Int) {
                when {
                    pos < 0 -> {
                        dataBindingLoanFour.loanFourVm?.preCloseTypeId = 0
                        return
                    }
                    else -> {
                        dataBindingLoanFour.loanFourVm?.preCloseTypeId = memberLoanDetails.preCloseTypes[pos].id
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
                }
            }
        })
        return view
    }


    private fun setPreCloseTypes() {
        for (data in memberLoanDetails.preCloseTypes) dataBindingLoanFour.loanFourVm?.preCloseTypeList?.add(data.name)
    }

    private fun addTotalOnSelect() {
        total = memberLoanDetails.principleDue!! + memberLoanDetails.interestDue!! + memberLoanDetails.penalCharges!! + memberLoanDetails.adjustedAmount!!
        dataBindingLoanFour.loanFourVm?.totalAmount?.set(Globals.getRoundOffDecimalFormat(total))
        totalAmountCallback.onTotalAmountChanged(binding)
    }

    private fun addTotalOnOtherSelection() {
        total = memberLoanDetails.outstanding!! + memberLoanDetails.interestDue!! + memberLoanDetails.penalCharges!! + memberLoanDetails.adjustedAmount!!
        dataBindingLoanFour.loanFourVm?.totalAmount?.set(Globals.getRoundOffDecimalFormat(total))
        totalAmountCallback.onTotalAmountChanged(binding)
    }

    private fun disableBankDetails(disable: Boolean) {
        dataBindingLoanFour.loanFourBankDetails = disable
        RepaymentDialog.ViewDialog.loanFourBankDetails = disable
    }

    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        when (context) {
            is OnFragmentInteractionListener -> listener = context
            else -> throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
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
        fun newInstance(memberLoanDetails: Repayment.LoanDetail, binding: DialogRepaymentBinding) =
                LoanFourFragment().apply {
                    this.memberLoanDetails = memberLoanDetails
                    this.binding = binding
                }
    }
}
