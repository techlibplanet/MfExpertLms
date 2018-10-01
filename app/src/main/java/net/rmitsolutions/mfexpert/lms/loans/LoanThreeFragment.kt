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
import kotlinx.android.synthetic.main.loan_one_layout.*
import kotlinx.android.synthetic.main.loan_three_layout.*
import kotlinx.android.synthetic.main.loan_two_layout.*

import net.rmitsolutions.mfexpert.lms.R
import net.rmitsolutions.mfexpert.lms.databinding.LoanThreeLayoutBinding
import net.rmitsolutions.mfexpert.lms.helpers.logD
import net.rmitsolutions.mfexpert.lms.helpers.toast
import net.rmitsolutions.mfexpert.lms.models.Globals
import net.rmitsolutions.mfexpert.lms.repayment.RepaymentDialogTabs
import net.rmitsolutions.mfexpert.lms.repayment.callback.TotalAmountCallback
import net.rmitsolutions.mfexpert.lms.viewmodels.Repayment
import org.jetbrains.anko.find
import java.math.RoundingMode
import java.text.DecimalFormat

class LoanThreeFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var memberLoanDetails : Repayment.LoanDetails

    private lateinit var inputBankAccNumberLoanThree : EditText
    private lateinit var inputBankNameLoanThree : EditText
    private lateinit var inputBankIfscLoanThree : EditText

    private lateinit var repaymentTypeSpinnerLoanThree : MaterialBetterSpinner
    private var total = 0.0
    private lateinit var repaymentDialogTabs: RepaymentDialogTabs
    private lateinit var totalAmountCallback: TotalAmountCallback

    lateinit var dataBindingLoanThree: LoanThreeLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repaymentDialogTabs = RepaymentDialogTabs()
        totalAmountCallback = TotalAmountCallback()
        totalAmountCallback.setListener(repaymentDialogTabs)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        dataBindingLoanThree = DataBindingUtil.inflate(inflater, R.layout.loan_three_layout, container, false)
        val view = dataBindingLoanThree.root
        dataBindingLoanThree.loanThreeVm = memberLoanDetails

        // Assigning view model for validation
        RepaymentDialogTabs.ViewDialog.loanThreeVm= dataBindingLoanThree.loanThreeVm

        total = memberLoanDetails.principleDue + memberLoanDetails.interestDue +
                memberLoanDetails.penalCharges + memberLoanDetails.adjustedAmount
        dataBindingLoanThree.loanThreeVm!!.totalAmount = total

        Repayment.RepaymentData.loanDetails.add(dataBindingLoanThree.loanThreeVm!!)

        inputBankAccNumberLoanThree = view.find(R.id.editTextBankAccNumberLoanThree)
        inputBankNameLoanThree = view.find(R.id.editTextBankNameLoanThree)
        inputBankIfscLoanThree = view.find(R.id.editTextIfscCodeLoanThree)


        val repaymentTypeAdapter = ArrayAdapter<String>(activity, android.R.layout.simple_spinner_dropdown_item, getPreCloseTypeList())
        repaymentTypeSpinnerLoanThree = view.find(R.id.choosePrepaymentTypeLoanThree)
//        repaymentTypeSpinnerLoanThree.adapter = repaymentTypeAdapter
        repaymentTypeSpinnerLoanThree.setAdapter<ArrayAdapter<String>>(repaymentTypeAdapter)

        repaymentTypeSpinnerLoanThree.setOnItemClickListener { adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->
            val list = memberLoanDetails.preCloseTypes
            dataBindingLoanThree.loanThreeVm!!.preCloseTypeId = 0
            dataBindingLoanThree.loanThreeVm!!.preCloseTypeId = list[i].id
        }
        return view
    }

    private fun getPreCloseTypeList(): ArrayList<String> {
        var nameList = ArrayList<String>()
        for (name in memberLoanDetails.preCloseTypes){
            nameList.add(name.name)
        }
        return nameList
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        disableBankDetails(true)
        repaymentTypeSpinnerLoanThree.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                val totalAmountCallback = TotalAmountCallback()
                totalAmountCallback.setListener(repaymentDialogTabs)
                when(text.toString()){
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
        totalLoanThree.text = Globals.getRoundOffDecimalFormat(total).toString()
        dataBindingLoanThree.loanThreeVm?.totalAmount = total     // Not working with data binding
//        totalAmountCallback.onTotalAmountChanged()
    }

    private fun addTotalOnOtherSelection() {
        total = memberLoanDetails.outstanding + memberLoanDetails.interestDue + memberLoanDetails.penalCharges + memberLoanDetails.adjustedAmount
        totalLoanThree.text = Globals.getRoundOffDecimalFormat(total).toString()
        dataBindingLoanThree.loanThreeVm?.totalAmount = total     // Not working with data binding
//        totalAmountCallback.onTotalAmountChanged()
    }


    private fun disableBankDetails(disable: Boolean) {
        if (disable){
            inputBankAccNumberLoanThree.visibility = View.GONE
            inputBankNameLoanThree.visibility = View.GONE
            inputBankIfscLoanThree.visibility = View.GONE
            view?.find<TextView>(R.id.labelBankAccNumberLoanThree)?.visibility = View.GONE
            view?.find<TextView>(R.id.labelBankNameLoanThree)?.visibility = View.GONE
            view?.find<TextView>(R.id.labelIfscCodeLoanThree)?.visibility = View.GONE
            return
        }
        inputBankAccNumberLoanThree.visibility = View.VISIBLE
        inputBankNameLoanThree.visibility = View.VISIBLE
        inputBankIfscLoanThree.visibility = View.VISIBLE
        view?.find<TextView>(R.id.labelBankAccNumberLoanThree)?.visibility = View.VISIBLE
        view?.find<TextView>(R.id.labelBankNameLoanThree)?.visibility = View.VISIBLE
        view?.find<TextView>(R.id.labelIfscCodeLoanThree)?.visibility = View.VISIBLE

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
                LoanThreeFragment().apply {
                    this.memberLoanDetails = memberLoanDetails

                }
    }
}
