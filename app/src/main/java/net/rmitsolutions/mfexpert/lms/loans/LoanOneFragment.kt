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

import net.rmitsolutions.mfexpert.lms.R
import net.rmitsolutions.mfexpert.lms.databinding.LoanOneLayoutBinding
import net.rmitsolutions.mfexpert.lms.helpers.toast
import net.rmitsolutions.mfexpert.lms.models.Globals
import net.rmitsolutions.mfexpert.lms.repayment.callback.TotalAmountCallback
import net.rmitsolutions.mfexpert.lms.repayment.RepaymentDialog
import net.rmitsolutions.mfexpert.lms.viewmodels.Repayment
import org.jetbrains.anko.find


class LoanOneFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var inputBankAccNumberLoanOne : EditText
    private lateinit var inputBankNameLoanOne : EditText
    private lateinit var inputBankIfscLoanOne : EditText

    private lateinit var repaymentTypeSpinner : MaterialBetterSpinner
    private lateinit var memberLoanDetails : Repayment.LoanDetails
    private lateinit var repaymentDialogTabs: RepaymentDialog
    private lateinit var totalAmountCallback: TotalAmountCallback

    lateinit var dataBindingLoanOne: LoanOneLayoutBinding
    private var total = 0.0
    private var mView : View? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        repaymentDialogTabs = RepaymentDialog()
        totalAmountCallback = TotalAmountCallback()
        totalAmountCallback.setListener(repaymentDialogTabs)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        dataBindingLoanOne = DataBindingUtil.inflate(inflater, R.layout.loan_one_layout, container, false)
        val view = dataBindingLoanOne.root

        dataBindingLoanOne.loanOneVm = memberLoanDetails

        // Assigning view model for validation
        RepaymentDialog.ViewDialog.loanOneVm = dataBindingLoanOne.loanOneVm

        total = memberLoanDetails.interestDue + memberLoanDetails.adjustedAmount + memberLoanDetails.penalCharges
        dataBindingLoanOne.loanOneVm!!.totalAmount = total

        Repayment.RepaymentData.loanDetails.add(dataBindingLoanOne.loanOneVm!!)

        inputBankAccNumberLoanOne = view.find(R.id.editTextBankAccNumberLoanOne)
        inputBankNameLoanOne = view.find(R.id.editTextBankNameLoanOne)
        inputBankIfscLoanOne = view.find(R.id.editTextIfscCodeLoanOne)

        val repaymentTypeAdapter = ArrayAdapter<String>(activity!!, android.R.layout.simple_spinner_dropdown_item, getPreCloseTypeList())
        repaymentTypeSpinner = view.find(R.id.choosePrepaymentTypeLoanOne)
        repaymentTypeSpinner.setAdapter<ArrayAdapter<String>>(repaymentTypeAdapter)

        repaymentTypeSpinner.setOnItemClickListener { adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->
            val list = memberLoanDetails.preCloseTypes
            dataBindingLoanOne.loanOneVm!!.preCloseTypeId = 0
            dataBindingLoanOne.loanOneVm!!.preCloseTypeId = list[i].id
        }

        total = memberLoanDetails.principleDue + memberLoanDetails.interestDue + memberLoanDetails.penalCharges + memberLoanDetails.adjustedAmount
        return view
    }

    fun onPreCloseTypesChanged(position: Int) {
        dataBindingLoanOne.loanOneVm?.preCloseTypeId= position + 1
    }


    fun getPreCloseTypeList(): ArrayList<String> {
        var nameList = ArrayList<String>()
        for (name in memberLoanDetails.preCloseTypes){
            nameList.add(name.name)
        }
        return nameList
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        disableBankDetails(true)
        repaymentTypeSpinner.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                when(text.toString()){
                    "Select" ->{
                        disableBankDetails(true)
                        addTotalOnSelect()
                    }
                    "NPP" ->{
                        disableBankDetails(true)
                        addTotalOnOtherSelection()
                    }
                    "MDT" ->{
                        disableBankDetails(false)
                        addTotalOnOtherSelection()
                    }
                    "HDT" ->{
                        disableBankDetails(false)
                        addTotalOnOtherSelection()
                    }
                    else ->{
                        toast("Invalid Selection")
                    }
                }
            }
        })
    }

    private fun addTotalOnSelect(){
        total = memberLoanDetails.principleDue + memberLoanDetails.interestDue + memberLoanDetails.penalCharges + memberLoanDetails.adjustedAmount
        totalLoanOne.text = Globals.getRoundOffDecimalFormat(total).toString()
        dataBindingLoanOne.loanOneVm?.totalAmount = total     // Not working with data binding
        totalAmountCallback.onTotalAmountChanged(mView!!)
    }

    private fun addTotalOnOtherSelection(){
        total = memberLoanDetails.outstanding + memberLoanDetails.interestDue + memberLoanDetails.penalCharges + memberLoanDetails.adjustedAmount
        totalLoanOne.text = Globals.getRoundOffDecimalFormat(total).toString()
        dataBindingLoanOne.loanOneVm?.totalAmount = total     // Not working with data binding
        totalAmountCallback.onTotalAmountChanged(mView!!)
    }

    private fun disableBankDetails(disable: Boolean) {
        if (disable){
            inputBankAccNumberLoanOne.visibility = View.GONE
            inputBankNameLoanOne.visibility = View.GONE
            inputBankIfscLoanOne.visibility = View.GONE
            view?.find<TextView>(R.id.labelBankAccNumberLoanOne)?.visibility = View.GONE
            view?.find<TextView>(R.id.labelBankNameLoanOne)?.visibility = View.GONE
            view?.find<TextView>(R.id.labelIfscCodeLoanOne)?.visibility = View.GONE
            return
        }
        inputBankAccNumberLoanOne.visibility = View.VISIBLE
        inputBankNameLoanOne.visibility = View.VISIBLE
        inputBankIfscLoanOne.visibility = View.VISIBLE
        view?.find<TextView>(R.id.labelBankAccNumberLoanOne)?.visibility = View.VISIBLE
        view?.find<TextView>(R.id.labelBankNameLoanOne)?.visibility = View.VISIBLE
        view?.find<TextView>(R.id.labelIfscCodeLoanOne)?.visibility = View.VISIBLE
        return

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
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        @JvmStatic
        fun newInstance(memberLoanDetails: Repayment.LoanDetails, view : View) =
                LoanOneFragment().apply {
                    this.memberLoanDetails = memberLoanDetails
                    this.mView = view
                }
    }
}
