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
import net.rmitsolutions.mfexpert.lms.databinding.LoanOneLayoutBinding
import net.rmitsolutions.mfexpert.lms.helpers.toast
import net.rmitsolutions.mfexpert.lms.models.Globals
import net.rmitsolutions.mfexpert.lms.repayment.callback.TotalAmountCallback
import net.rmitsolutions.mfexpert.lms.repayment.RepaymentDialog
import net.rmitsolutions.mfexpert.lms.viewmodels.Repayment


class LoanOneFragment : Fragment() {

    private var listener: OnFragmentInteractionListener? = null
    private lateinit var memberLoanDetails: Repayment.LoanDetail
    private lateinit var repaymentDialog: RepaymentDialog
    private lateinit var totalAmountCallback: TotalAmountCallback

    lateinit var dataBindingLoanOne: LoanOneLayoutBinding
    private var total = 0.0
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
        dataBindingLoanOne = DataBindingUtil.inflate(inflater, R.layout.loan_one_layout, container, false)
        val view = dataBindingLoanOne.root

        dataBindingLoanOne.loanOneVm = memberLoanDetails

        // Assigning view model for validation
        RepaymentDialog.ViewDialog.loanOneVm = dataBindingLoanOne.loanOneVm

        total = memberLoanDetails.principleDue!! + memberLoanDetails.interestDue!! +
                memberLoanDetails.adjustedAmount!! + memberLoanDetails.penalCharges!!
        dataBindingLoanOne.loanOneVm!!.totalAmount.set(total)

        Repayment.RepaymentData.loanDetails.add(dataBindingLoanOne.loanOneVm!!)

        disableBankDetails(true)
        setPreCloseTypes()
        dataBindingLoanOne.eventHandler = (object : Repayment.RepaymentEventHandler {
            override fun onPrepaymentTypeChanged(pos: Int) {
                when {
                    pos < 0 -> {
                        dataBindingLoanOne.loanOneVm?.preCloseTypeId = 0
                        return
                    }
                    else -> {
                        dataBindingLoanOne.loanOneVm?.preCloseTypeId = memberLoanDetails.preCloseTypes[pos].id
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
        for (data in memberLoanDetails.preCloseTypes) dataBindingLoanOne.loanOneVm?.preCloseTypeList?.add(data.name)
    }


    private fun addTotalOnSelect() {
        total = memberLoanDetails.principleDue!! + memberLoanDetails.interestDue!! + memberLoanDetails.penalCharges!! + memberLoanDetails.adjustedAmount!!
        dataBindingLoanOne.loanOneVm?.totalAmount?.set(Globals.getRoundOffDecimalFormat(total))
        totalAmountCallback.onTotalAmountChanged(binding!!)
    }

    private fun addTotalOnOtherSelection() {
        total = memberLoanDetails.outstanding!! + memberLoanDetails.interestDue!! + memberLoanDetails.penalCharges!! + memberLoanDetails.adjustedAmount!!
        dataBindingLoanOne.loanOneVm?.totalAmount?.set(Globals.getRoundOffDecimalFormat(total))
        totalAmountCallback.onTotalAmountChanged(binding!!)
    }

    private fun disableBankDetails(disable: Boolean) {
        dataBindingLoanOne.loanOneBankDetails = disable
        RepaymentDialog.ViewDialog.loanOneBankDetails = disable
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
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        @JvmStatic
        fun newInstance(memberLoanDetails: Repayment.LoanDetail, binding: DialogRepaymentBinding) =
                LoanOneFragment().apply {
                    this.memberLoanDetails = memberLoanDetails
                    this.binding = binding
                }
    }
}
