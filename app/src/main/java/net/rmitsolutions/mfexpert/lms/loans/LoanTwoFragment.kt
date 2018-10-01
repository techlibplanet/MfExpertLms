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
import kotlinx.android.synthetic.main.loan_two_layout.*

import net.rmitsolutions.mfexpert.lms.R
import net.rmitsolutions.mfexpert.lms.databinding.LoanTwoLayoutBinding
import net.rmitsolutions.mfexpert.lms.helpers.logD
import net.rmitsolutions.mfexpert.lms.helpers.toast
import net.rmitsolutions.mfexpert.lms.models.Globals
import net.rmitsolutions.mfexpert.lms.repayment.RepaymentDialogTabs
import net.rmitsolutions.mfexpert.lms.repayment.callback.TotalAmountCallback
import net.rmitsolutions.mfexpert.lms.viewmodels.Repayment
import org.jetbrains.anko.find


class LoanTwoFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null

    private lateinit var inputBankAccNumberLoanTwo: EditText
    private lateinit var inputBankNameLoanTwo: EditText
    private lateinit var inputBankIfscLoanTwo: EditText

    private lateinit var repaymentTypeSpinnerLoanTwo: MaterialBetterSpinner

    private lateinit var memberLoanDetails: Repayment.LoanDetails

    private lateinit var repaymentDialogTabs: RepaymentDialogTabs
    private lateinit var totalAmountCallback: TotalAmountCallback

    lateinit var dataBindingLoanTwo: LoanTwoLayoutBinding
    private var total = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repaymentDialogTabs = RepaymentDialogTabs()
        totalAmountCallback = TotalAmountCallback()
        totalAmountCallback.setListener(repaymentDialogTabs)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        dataBindingLoanTwo = DataBindingUtil.inflate(inflater, R.layout.loan_two_layout, container, false)
        val view = dataBindingLoanTwo.root

        dataBindingLoanTwo.loanTwoVm = memberLoanDetails

        // Assigning view model for validation
        RepaymentDialogTabs.ViewDialog.loanTwoVm = dataBindingLoanTwo.loanTwoVm

        total = memberLoanDetails.principleDue + memberLoanDetails.interestDue +
                memberLoanDetails.penalCharges + memberLoanDetails.adjustedAmount
        dataBindingLoanTwo.loanTwoVm!!.totalAmount = total

        Repayment.RepaymentData.loanDetails.add(dataBindingLoanTwo.loanTwoVm!!)

        inputBankAccNumberLoanTwo = view.find(R.id.editTextBankAccNumberLoanTwo)
        inputBankNameLoanTwo = view.find(R.id.editTextBankNameLoanTwo)
        inputBankIfscLoanTwo = view.find(R.id.editTextIfscCodeLoanTwo)

        val repaymentTypeAdapter = ArrayAdapter<String>(activity, android.R.layout.simple_spinner_dropdown_item, getPreCloseTypeList())
        repaymentTypeSpinnerLoanTwo = view.find(R.id.choosePrepaymentTypeLoanTwo)
//        repaymentTypeSpinnerLoanTwo.adapter = repaymentTypeAdapter
        repaymentTypeSpinnerLoanTwo.setAdapter<ArrayAdapter<String>>(repaymentTypeAdapter)

        repaymentTypeSpinnerLoanTwo.setOnItemClickListener { adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->
            val list = memberLoanDetails.preCloseTypes
            dataBindingLoanTwo.loanTwoVm!!.preCloseTypeId = 0
            dataBindingLoanTwo.loanTwoVm!!.preCloseTypeId = list[i].id
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
        repaymentTypeSpinnerLoanTwo.addTextChangedListener(object : TextWatcher {
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
        totalLoanTwo.text = Globals.getRoundOffDecimalFormat(total).toString()
        dataBindingLoanTwo.loanTwoVm?.totalAmount = total     // Not working with data binding
//        totalAmountCallback.onTotalAmountChanged()
    }

    private fun addTotalOnOtherSelection() {
        total = memberLoanDetails.outstanding + memberLoanDetails.interestDue + memberLoanDetails.penalCharges + memberLoanDetails.adjustedAmount
        totalLoanTwo.text = Globals.getRoundOffDecimalFormat(total).toString()
        dataBindingLoanTwo.loanTwoVm?.totalAmount = total     // Not working with data binding
//        totalAmountCallback.onTotalAmountChanged()
    }

    private fun disableBankDetails(disable: Boolean) {
        if (disable) {
            inputBankAccNumberLoanTwo.visibility = View.GONE
            inputBankNameLoanTwo.visibility = View.GONE
            inputBankIfscLoanTwo.visibility = View.GONE
            view?.find<TextView>(R.id.labelBankAccNumberLoanTwo)?.visibility = View.GONE
            view?.find<TextView>(R.id.labelBankNameLoanTwo)?.visibility = View.GONE
            view?.find<TextView>(R.id.labelIfscCodeLoanTwo)?.visibility = View.GONE
            return
        }
        inputBankAccNumberLoanTwo.visibility = View.VISIBLE
        inputBankNameLoanTwo.visibility = View.VISIBLE
        inputBankIfscLoanTwo.visibility = View.VISIBLE
        view?.find<TextView>(R.id.labelBankAccNumberLoanTwo)?.visibility = View.VISIBLE
        view?.find<TextView>(R.id.labelBankNameLoanTwo)?.visibility = View.VISIBLE
        view?.find<TextView>(R.id.labelIfscCodeLoanTwo)?.visibility = View.VISIBLE

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
                LoanTwoFragment().apply {
                    this.memberLoanDetails = memberLoanDetails
                }
    }
}
