package net.rmitsolutions.mfexpert.lms.repayment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import io.reactivex.disposables.CompositeDisposable
import net.rmitsolutions.mfexpert.lms.Constants
import net.rmitsolutions.mfexpert.lms.MfExpertApp
import net.rmitsolutions.mfexpert.lms.R
import net.rmitsolutions.mfexpert.lms.databinding.DialogRepaymentBinding
import net.rmitsolutions.mfexpert.lms.dependency.components.DaggerInjectFragmentComponent
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

class RepaymentDialog() : DialogFragment(), TotalAmountListener {

    object ViewDialog {
        var memberLoanDetails: Repayment.MemberLoanDetails? = null
        var totalAmount: Double = 0.0
        var preTotalAmount: Double = 0.0
        var loanOneVm: Repayment.LoanDetail? = null
        var loanTwoVm: Repayment.LoanDetail? = null
        var loanThreeVm: Repayment.LoanDetail? = null
        var loanFourVm: Repayment.LoanDetail? = null
        var repaymentActivityVm: Repayment.RepaymentModel? = null
        var loanOneBankDetails: Boolean = false
        var loanTwoBankDetails: Boolean = false
        var loanThreeBankDetails: Boolean = false
        var loanFourBankDetails: Boolean = false
    }

    private lateinit var alertDialog: AlertDialog
    private lateinit var repaymentPagerAdapter: RepaymentPagerAdapter
    private lateinit var viewHolderContext: ClientViewHolder

    // Initialize listener
    private var mListener: RepaymentDialogListener? = null
    private lateinit var compositeDisposable: CompositeDisposable
    private var clientId: Long? = null
    private lateinit var dialogRepaymentBinding: DialogRepaymentBinding


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
        mListener = viewHolderContext
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
        if (!ViewDialog.loanOneBankDetails) {
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
        if (!ViewDialog.loanTwoBankDetails) {
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
        if (!ViewDialog.loanThreeBankDetails) {
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
        if (!ViewDialog.loanFourBankDetails) {
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
            this.mListener = viewHolderContext
        } catch (e: ClassCastException) {
            throw ClassCastException("${ClientViewHolder::class.java.simpleName} must implement OnCompleteListener")
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialogRepaymentBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_repayment, container, false)
        var view = dialogRepaymentBinding.root
        // Get the total amount of all loans from all fragments
        for (i in 0 until ViewDialog.memberLoanDetails?.loanDetails?.size!!) {
            ViewDialog.totalAmount = ViewDialog.memberLoanDetails!!.loanDetails[i].principleDue!! +
                    ViewDialog.memberLoanDetails!!.loanDetails[i].interestDue!! +
                    ViewDialog.memberLoanDetails!!.loanDetails[i].penalCharges!!

        }
        dialogRepaymentBinding.repaymentDialogVm?.totalLoanAmounts?.set(ViewDialog.totalAmount)

        // Adding fragment dynamically, passing loan details in new instance
        for (i in 0 until ViewDialog.memberLoanDetails?.loanDetails?.size!!) {
            when (i) {
                0 -> repaymentPagerAdapter.addFragment("Loan 1", LoanOneFragment.newInstance(ViewDialog.memberLoanDetails?.loanDetails!![i], dialogRepaymentBinding))
                1 -> repaymentPagerAdapter.addFragment("Loan 2", LoanTwoFragment.newInstance(ViewDialog.memberLoanDetails?.loanDetails!![i], dialogRepaymentBinding))
                2 -> repaymentPagerAdapter.addFragment("Loan 3", LoanThreeFragment.newInstance(ViewDialog.memberLoanDetails?.loanDetails!![i], dialogRepaymentBinding))
                3 -> repaymentPagerAdapter.addFragment("Loan 4", LoanFourFragment.newInstance(ViewDialog.memberLoanDetails?.loanDetails!![i], dialogRepaymentBinding))
            }
        }
        dialogRepaymentBinding.masterViewPager.adapter = repaymentPagerAdapter;
        dialogRepaymentBinding.masterViewPager.offscreenPageLimit = 4
        dialogRepaymentBinding.tabLayout.setupWithViewPager(dialogRepaymentBinding.masterViewPager);
        alertDialog.setView(view)
        return view;
    }

    // Method to set the total amount of dialog fragment layout
    override fun onTotalAmountChanged(binding: DialogRepaymentBinding?) {
        if (binding != null) {
            for (i in 0 until ViewDialog.memberLoanDetails?.loanDetails?.size!!) {
                when (i) {
                    0 -> {
                        ViewDialog.totalAmount = ViewDialog.loanOneVm?.totalAmount?.get()!!
                        binding.loanTotalAmount.text = ViewDialog.totalAmount.toString()
                    }
                    1 -> {
                        ViewDialog.totalAmount = ViewDialog.loanOneVm?.totalAmount?.get()!! +
                                ViewDialog.loanTwoVm?.totalAmount?.get()!!
                        binding.loanTotalAmount.text = "${ViewDialog.totalAmount}"
                    }
                    2 -> {
                        ViewDialog.totalAmount = ViewDialog.loanOneVm?.totalAmount?.get()!! +
                                ViewDialog.loanTwoVm?.totalAmount?.get()!! + ViewDialog.loanThreeVm?.totalAmount?.get()!!
                        binding.loanTotalAmount.text = "${ViewDialog.totalAmount}"
                    }
                    3 -> {
                        ViewDialog.totalAmount = ViewDialog.loanOneVm?.totalAmount?.get()!! +
                                ViewDialog.loanTwoVm?.totalAmount?.get()!! + ViewDialog.loanThreeVm?.totalAmount?.get()!! +
                                ViewDialog.loanFourVm?.totalAmount?.get()!!
                        binding.loanTotalAmount.text = "${ViewDialog.totalAmount}"
                    }
                }
            }
        } else {
            logE("Error - RepaymentDialogBinding is null")
        }
    }

}