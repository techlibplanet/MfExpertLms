package net.rmitsolutions.mfexpert.lms.loans

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner
import kotlinx.android.synthetic.main.loan_four_layout.*
import kotlinx.android.synthetic.main.loan_one_layout.*
import kotlinx.android.synthetic.main.loan_two_layout.*

import net.rmitsolutions.mfexpert.lms.R
import net.rmitsolutions.mfexpert.lms.databinding.LoanFourLayoutBinding
import net.rmitsolutions.mfexpert.lms.helpers.logD
import net.rmitsolutions.mfexpert.lms.helpers.toast
import net.rmitsolutions.mfexpert.lms.models.Globals
import net.rmitsolutions.mfexpert.lms.repayment.RepaymentDialogTabs
import net.rmitsolutions.mfexpert.lms.repayment.callback.TotalAmountCallback
import net.rmitsolutions.mfexpert.lms.viewmodels.Repayment
import org.jetbrains.anko.find

class LoanFourFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var memberLoanDetails: Repayment.LoanDetails

    private lateinit var inputBankAccNumberLoanFour: EditText
    private lateinit var inputBankNameLoanFour: EditText
    private lateinit var inputBankIfscLoanFour: EditText

    private lateinit var repaymentTypeSpinnerLoanFour: MaterialBetterSpinner
    lateinit var dataBindingLoanFour: LoanFourLayoutBinding
    private var total = 0.0
    private lateinit var repaymentDialogTabs: RepaymentDialogTabs
    private lateinit var totalAmountCallback: TotalAmountCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repaymentDialogTabs = RepaymentDialogTabs()
        totalAmountCallback = TotalAmountCallback()
        totalAmountCallback.setListener(repaymentDialogTabs)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        dataBindingLoanFour = DataBindingUtil.inflate(inflater, R.layout.loan_four_layout, container, false)
        val view = dataBindingLoanFour.root

        dataBindingLoanFour.loanFourVm = memberLoanDetails

        // Assigning view model for validation
        RepaymentDialogTabs.ViewDialog.loanFourVm = dataBindingLoanFour.loanFourVm

        total = memberLoanDetails.principleDue + memberLoanDetails.interestDue +
                memberLoanDetails.penalCharges + memberLoanDetails.adjustedAmount
        dataBindingLoanFour.loanFourVm!!.totalAmount = total

        Repayment.RepaymentData.loanDetails.add(dataBindingLoanFour.loanFourVm!!)

        inputBankAccNumberLoanFour = view.find(R.id.editTextBankAccNumberLoanFour)
        inputBankNameLoanFour = view.find(R.id.editTextBankNameLoanFour)
        inputBankIfscLoanFour = view.find(R.id.editTextIfscCodeLoanFour)

        val repaymentTypeAdapter = ArrayAdapter<String>(activity, android.R.layout.simple_spinner_dropdown_item, getPreCloseTypeList())
        repaymentTypeSpinnerLoanFour = view.find(R.id.choosePrepaymentTypeLoanFour)
//        repaymentTypeSpinnerLoanFour.adapter = repaymentTypeAdapter
        repaymentTypeSpinnerLoanFour.setAdapter<ArrayAdapter<String>>(repaymentTypeAdapter)

        repaymentTypeSpinnerLoanFour.setOnItemClickListener { adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->
            val list = memberLoanDetails.preCloseTypes
            dataBindingLoanFour.loanFourVm!!.preCloseTypeId = 0
            dataBindingLoanFour.loanFourVm!!.preCloseTypeId = list[i].id
        }
        return view
    }

    private fun getPreCloseTypeList(): ArrayList<String> {
        var nameList = ArrayList<String>()
        for (name in memberLoanDetails.preCloseTypes) {
            nameList.add(name.name)
        }
        return nameList
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        disableBankDetails(true)
        repaymentTypeSpinnerLoanFour.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                val totalAmountCallback = TotalAmountCallback()
                totalAmountCallback.setListener(repaymentDialogTabs)
                when (text.toString()) {
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
    }

    private fun addTotalOnSelect() {
        total = memberLoanDetails.principleDue + memberLoanDetails.interestDue + memberLoanDetails.penalCharges + memberLoanDetails.adjustedAmount
        totalLoanFour.text = Globals.getRoundOffDecimalFormat(total).toString()
        dataBindingLoanFour.loanFourVm?.totalAmount = total     // Not working with data binding
//        totalAmountCallback.onTotalAmountChanged()
    }

    private fun addTotalOnOtherSelection() {
        total = memberLoanDetails.outstanding + memberLoanDetails.interestDue + memberLoanDetails.penalCharges + memberLoanDetails.adjustedAmount
        totalLoanFour.text = Globals.getRoundOffDecimalFormat(total).toString()
        dataBindingLoanFour.loanFourVm?.totalAmount = total     // Not working with data binding
//        totalAmountCallback.onTotalAmountChanged()
    }

    private fun disableBankDetails(disable: Boolean) {
        if (disable) {
            inputBankAccNumberLoanFour.visibility = View.GONE
            inputBankNameLoanFour.visibility = View.GONE
            inputBankIfscLoanFour.visibility = View.GONE
            view?.find<TextView>(R.id.labelBankAccNumberLoanFour)?.visibility = View.GONE
            view?.find<TextView>(R.id.labelBankNameLoanFour)?.visibility = View.GONE
            view?.find<TextView>(R.id.labelIfscCodeLoanFour)?.visibility = View.GONE
            return
        }
        inputBankAccNumberLoanFour.visibility = View.VISIBLE
        inputBankNameLoanFour.visibility = View.VISIBLE
        inputBankIfscLoanFour.visibility = View.VISIBLE
        view?.find<TextView>(R.id.labelBankAccNumberLoanFour)?.visibility = View.VISIBLE
        view?.find<TextView>(R.id.labelBankNameLoanFour)?.visibility = View.VISIBLE
        view?.find<TextView>(R.id.labelIfscCodeLoanFour)?.visibility = View.VISIBLE

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
        fun newInstance(memberLoanDetails: Repayment.LoanDetails) =
                LoanFourFragment().apply {
                    this.memberLoanDetails = memberLoanDetails
                }
    }
}
