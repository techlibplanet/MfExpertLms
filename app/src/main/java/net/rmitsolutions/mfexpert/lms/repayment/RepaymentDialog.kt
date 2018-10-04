package net.rmitsolutions.mfexpert.lms.repayment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import io.reactivex.disposables.CompositeDisposable
import net.rmitsolutions.mfexpert.lms.Constants
import net.rmitsolutions.mfexpert.lms.MfExpertApp
import net.rmitsolutions.mfexpert.lms.R
import net.rmitsolutions.mfexpert.lms.dependency.components.DaggerInjectFragmentComponent
import net.rmitsolutions.mfexpert.lms.helpers.logD
import net.rmitsolutions.mfexpert.lms.helpers.logE
import net.rmitsolutions.mfexpert.lms.helpers.toast
import net.rmitsolutions.mfexpert.lms.loans.LoanFourFragment
import net.rmitsolutions.mfexpert.lms.loans.LoanOneFragment
import net.rmitsolutions.mfexpert.lms.loans.LoanThreeFragment
import net.rmitsolutions.mfexpert.lms.loans.LoanTwoFragment
import net.rmitsolutions.mfexpert.lms.repayment.adapter.ClientViewHolder
import net.rmitsolutions.mfexpert.lms.repayment.adapter.RepaymentPagerAdapter
import net.rmitsolutions.mfexpert.lms.repayment.callback.RepaymentDialogListener
import net.rmitsolutions.mfexpert.lms.repayment.callback.TotalAmountListener
import net.rmitsolutions.mfexpert.lms.viewmodels.Repayment
import org.jetbrains.anko.find

class RepaymentDialog() : DialogFragment(), TotalAmountListener {

    object ViewDialog {
        var memberLoanDetails: Repayment.MemberLoanDetails? = null
        var totalAmount: Double = 0.0
        var preTotalAmount: Double = 0.0
        var loanOneVm: Repayment.LoanDetails? = null
        var loanTwoVm: Repayment.LoanDetails? = null
        var loanThreeVm: Repayment.LoanDetails? = null
        var loanFourVm: Repayment.LoanDetails? = null
        var repaymentActivityVm: Repayment.RepaymentModel? = null
    }

    private lateinit var alertDialog: AlertDialog
    private lateinit var repaymentPagerAdapter: RepaymentPagerAdapter
    private lateinit var viewHolderContext: ClientViewHolder

    // Initialize listener
    private var mListener: RepaymentDialogListener? = null

    private lateinit var inputBankAccNumberLoanOne: EditText
    private lateinit var inputBankAccNumberLoanTwo: EditText
    private lateinit var inputBankAccNumberLoanThree: EditText
    private lateinit var inputBankAccNumberLoanFour: EditText

    private lateinit var compositeDisposable: CompositeDisposable
    private lateinit var totalLoanAmounts: TextView

    private var clientId: Long? = null


    @SuppressLint("ValidFragment")
    constructor(viewHolderContext: ClientViewHolder) : this() {
        this.viewHolderContext = viewHolderContext
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repaymentPagerAdapter = RepaymentPagerAdapter(childFragmentManager)
        compositeDisposable = CompositeDisposable()

        val depComponent = DaggerInjectFragmentComponent.builder()
                .applicationComponent(MfExpertApp.applicationComponent)
                .build()
        depComponent.injectRepaymentDialog(this)

        // Getting Loan Details and Client ID
        clientId = arguments?.getLong("ClientId")
        ViewDialog.memberLoanDetails = arguments?.getSerializable("MemberLoanDetails") as Repayment.MemberLoanDetails?
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        mListener = viewHolderContext as RepaymentDialogListener
        alertDialog = AlertDialog.Builder(activity!!).setTitle("${ViewDialog.memberLoanDetails?.memberName}")
                .setPositiveButton("Ok", null).setNegativeButton("Cancel", null).create()
        alertDialog.setOnShowListener { dialogInterface ->
            val okButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            val cancelButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            okButton.setOnClickListener {
                if (view != null) {
                    if (validate(view!!)) {
                        mListener?.onGettingTotalAmount(ViewDialog.totalAmount)
                        mListener?.onComplete(Constants.PREPAYMENT_CHECKED, clientId!!)
                        dialogInterface.dismiss()
                    }
                }
            }

            cancelButton.setOnClickListener {
                mListener?.onGettingTotalAmount(ViewDialog.preTotalAmount)
                mListener?.onComplete(Constants.PREPAYMENT_UNCHECKED, clientId!!)
                dialogInterface.dismiss()
            }
        }
        return alertDialog
    }


    private fun validate(view: View): Boolean {
        for (i in 0 until Repayment.RepaymentData.loanDetails.size) {
            when (i) {
                0 -> return validateLoanOne(view)

                1 -> {
                    if (!validateLoanTwo(view)) return false
                }
                2 -> {
                    if (!validateLoanThree(view)) return false
                }
                3 -> {
                    if (!validateLoanFour(view)) return false
                }
            }
        }
        return true
    }

    private fun validateLoanOne(view: View): Boolean {
        inputBankAccNumberLoanOne = view.find(R.id.editTextBankAccNumberLoanOne)

        if (inputBankAccNumberLoanOne.visibility == View.VISIBLE) {
            if (ViewDialog.loanOneVm?.bankAccNo.isNullOrBlank()) {
                toast("Enter Loan 1 Bank Acc Number")
                return false
            }

            if (ViewDialog.loanOneVm?.bankName.isNullOrBlank()) {
                toast("Enter Loan 1 Bank Name")
                return false
            }

            if (ViewDialog.loanOneVm?.bankIfsc.isNullOrBlank()) {
                toast("Enter Loan 1 Bank IFSC")
                return false
            }
        }
        return true
    }

    private fun validateLoanTwo(view: View): Boolean {
        if (!validateLoanOne(view)) {
            return false
        }
        inputBankAccNumberLoanTwo = view.find(R.id.editTextBankAccNumberLoanTwo)
        if (inputBankAccNumberLoanTwo.visibility == View.VISIBLE) {
            if (ViewDialog.loanTwoVm?.bankAccNo.isNullOrBlank()) {
                toast("Enter Loan 2 Bank Acc Number")
                return false
            }

            if (ViewDialog.loanTwoVm?.bankName.isNullOrBlank()) {
                toast("Enter Loan 2 Bank Name")
                return false
            }

            if (ViewDialog.loanTwoVm?.bankIfsc.isNullOrBlank()) {
                toast("Enter Loan 2 Bank IFSC")
                return false
            }
        }
        return true
    }

    private fun validateLoanThree(view: View): Boolean {
        if (!validateLoanTwo(view)) {
            return false
        }
        inputBankAccNumberLoanThree = view.find(R.id.editTextBankAccNumberLoanThree)
        if (inputBankAccNumberLoanThree.visibility == View.VISIBLE) {
            if (ViewDialog.loanThreeVm?.bankAccNo.isNullOrBlank()) {
                toast("Enter Loan 3 Bank Acc Number")
                return false
            }

            if (ViewDialog.loanThreeVm?.bankName.isNullOrBlank()) {
                toast("Enter Loan 3 Bank Name")
                return false
            }

            if (ViewDialog.loanThreeVm?.bankIfsc.isNullOrBlank()) {
                toast("Enter Loan 3 Bank IFSC")
                return false
            }
        }
        return true

    }

    private fun validateLoanFour(view: View): Boolean {
        if (!validateLoanThree(view)) {
            return false
        }
        inputBankAccNumberLoanFour = view.find(R.id.editTextBankAccNumberLoanFour)
        if (inputBankAccNumberLoanFour.visibility == View.VISIBLE) {
            if (ViewDialog.loanFourVm?.bankAccNo.isNullOrBlank()) {
                toast("Enter Loan 4 Bank Acc Number")
                return false
            }

            if (ViewDialog.loanFourVm?.bankName.isNullOrBlank()) {
                toast("Enter Loan 4 Bank Name")
                return false
            }

            if (ViewDialog.loanFourVm?.bankIfsc.isNullOrBlank()) {
                toast("Enter Loan 4 Bank IFSC")
                return false
            }
        }
        return true
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            // Registering listener
            this.mListener = viewHolderContext as RepaymentDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException("${ClientViewHolder::class.java.simpleName} must implement OnCompleteListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_repayment, container, false)
        val tabLayout = view.findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = view.findViewById<ViewPager>(R.id.masterViewPager)
        totalLoanAmounts = view.find(R.id.loanTotalAmount)
        // Get the total amount of all loans from all fragments
        for (i in 0 until ViewDialog.memberLoanDetails?.loanDetails?.size!!) {
            ViewDialog.totalAmount = ViewDialog.memberLoanDetails!!.loanDetails[i].principleDue +
                    ViewDialog.memberLoanDetails!!.loanDetails[i].interestDue +
                    ViewDialog.memberLoanDetails!!.loanDetails[i].penalCharges

        }
        totalLoanAmounts.text = ViewDialog.totalAmount.toString()


        // Adding fragment dynamically, passing loan details in new instance
        for (i in 0 until ViewDialog.memberLoanDetails?.loanDetails?.size!!) {
            when (i) {
                0 -> repaymentPagerAdapter.addFragment("Loan 1", LoanOneFragment.newInstance(ViewDialog.memberLoanDetails!!.loanDetails[i], view))
                1 -> repaymentPagerAdapter.addFragment("Loan 2", LoanTwoFragment.newInstance(ViewDialog.memberLoanDetails!!.loanDetails[i], view))
                2 -> repaymentPagerAdapter.addFragment("Loan 3", LoanThreeFragment.newInstance(ViewDialog.memberLoanDetails!!.loanDetails[i], view))
                3 -> repaymentPagerAdapter.addFragment("Loan 4", LoanFourFragment.newInstance(ViewDialog.memberLoanDetails!!.loanDetails[i], view))
            }
        }
        viewPager.adapter = repaymentPagerAdapter;
        viewPager.offscreenPageLimit = 4
        tabLayout.setupWithViewPager(viewPager);
        alertDialog.setView(view)
        return view;
    }

    // Method to set the total amount of dialog fragment layout
    override fun onTotalAmountChanged(view: View?) {
        if (view != null) {
            totalLoanAmounts = view.find(R.id.loanTotalAmount)
            for (i in 0 until ViewDialog.memberLoanDetails?.loanDetails?.size!!) {
                when (i) {
                    0 -> {
                        ViewDialog.totalAmount = ViewDialog.loanOneVm?.totalAmount!!
                        totalLoanAmounts.text = "${ViewDialog.totalAmount}"
                    }
                    1 -> {
                        ViewDialog.totalAmount = ViewDialog.loanOneVm?.totalAmount!! +
                                ViewDialog.loanTwoVm?.totalAmount!!
                        totalLoanAmounts.text = "${ViewDialog.totalAmount}"
                    }
                    2 -> {
                        ViewDialog.totalAmount = ViewDialog.loanOneVm?.totalAmount!! +
                                ViewDialog.loanTwoVm?.totalAmount!! + ViewDialog.loanThreeVm?.totalAmount!!
                        totalLoanAmounts.text = "${ViewDialog.totalAmount}"
                    }
                    3 -> {
                        ViewDialog.totalAmount = ViewDialog.loanOneVm?.totalAmount!! +
                                ViewDialog.loanTwoVm?.totalAmount!! + ViewDialog.loanThreeVm?.totalAmount!! +
                                ViewDialog.loanFourVm?.totalAmount!!
                        totalLoanAmounts.text = "${ViewDialog.totalAmount}"
                    }
                }
            }
        } else {
            logE("Error - View is null")
        }
    }
}