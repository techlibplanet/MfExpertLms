package net.rmitsolutions.mfexpert.lms.loanUtilizationCheck


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import net.rmitsolutions.mfexpert.lms.databinding.LoanUtilizationCheckBinding
import android.view.View
import android.widget.*
import androidx.databinding.DataBindingUtil
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner
import kotlinx.android.synthetic.main.activity_loan_utilization.*
import net.rmitsolutions.mfexpert.lms.BaseActivity
import net.rmitsolutions.mfexpert.lms.Constants
import net.rmitsolutions.mfexpert.lms.MfExpertApp
import net.rmitsolutions.mfexpert.lms.R
import net.rmitsolutions.mfexpert.lms.R.id.lblAverageMonthlyIncome
import net.rmitsolutions.mfexpert.lms.R.id.lblMemberCode
import net.rmitsolutions.mfexpert.lms.database.MfExpertLmsDatabase
import net.rmitsolutions.mfexpert.lms.dependency.components.DaggerInjectActivityComponent

import net.rmitsolutions.mfexpert.lms.helpers.*
import net.rmitsolutions.mfexpert.lms.network.ILoanUtilization
import net.rmitsolutions.mfexpert.lms.viewmodels.LoanUtilizationCheckModel
import net.rmitsolutions.mfexpert.lms.viewmodels.LoanUtilizationSaveModel
import net.rmitsolutions.mfexpert.lms.viewmodels.Loans
import org.jetbrains.anko.alert

import org.jetbrains.anko.toast

import javax.inject.Inject


class LoanUtilizationActivity : BaseActivity() {

    var chooseLoanDetailsSpinner: MaterialBetterSpinner? = null
    lateinit var loanIdValue: String
    private lateinit var loanUtilizationCheckModel: LoanUtilizationCheckModel
    lateinit var dataBinding: LoanUtilizationCheckBinding
    @Inject
    lateinit var loanUtilizationService: ILoanUtilization

    private lateinit var loanList: ArrayList<String>
    lateinit var lastUcDateText: TextView
    lateinit var inputValue: String

    lateinit var utilizationStatus: CheckBox
    lateinit var loanUtilizedFortheSamePurpose: CheckBox
    var loanDetails: MutableList<Loans.LoanDetails>? = null


    @Inject
    lateinit var database: MfExpertLmsDatabase


    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)

        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_loan_utilization)

        val depComponent = DaggerInjectActivityComponent.builder()
                .applicationComponent(MfExpertApp.applicationComponent)
                .build()
        depComponent.injectLoanUtilizationActivity(this)

        utilizationStatus = findViewById(R.id.chooseUtilizationStatus)
        loanUtilizedFortheSamePurpose = findViewById(R.id.loanUtilizedFortheSamePurpose)
        lastUcDateText = findViewById(R.id.lastUcDateText);
        chooseLoanDetailsSpinner = findViewById(R.id.chooseLoanDetails)


        chooseLoanDetailsSpinner?.isFocusable = false
        chooseLoanDetailsSpinner?.isClickable = false


        utilizationStatus.setOnCheckedChangeListener { buttonView, isChecked ->
            if (utilizationStatus.isChecked) {
                loanUtilizedFortheSamePurpose.isEnabled = true
                //loanUtilizedFortheSamePurpose.isChecked = true
                lblAverageMonthlyIncome.isFocusableInTouchMode = true
                txtAverageMonthlyIncome.isFocusableInTouchMode = true
                dataBinding.loanUtilizationCheckVm?.utilizationStatus = "true"
            }

            if (!utilizationStatus.isChecked) {
                dataBinding.loanUtilizationCheckVm?.utilizationStatus = "false"
                loanUtilizedFortheSamePurpose.isEnabled = false
                loanUtilizedFortheSamePurpose.isChecked = false
                lblAverageMonthlyIncome.isErrorEnabled = false
                lblAverageMonthlyIncome.isFocusable = false
                txtAverageMonthlyIncome.isFocusable = false
                txtAverageMonthlyIncome.setText("0")
                dataBinding.loanUtilizationCheckVm?.aveargeMonthlyIncome = "0"


            }

        }

        loanUtilizedFortheSamePurpose.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {

                dataBinding.loanUtilizationCheckVm?.loanUtilizedForSamePurpose = "true"
            } else {
                dataBinding.loanUtilizationCheckVm?.loanUtilizedForSamePurpose = "false"
            }

        }

        loanUtilizationCheckModel = LoanUtilizationCheckModel("", "", "", "", "",
                "")

        dataBinding.loanUtilizationCheckVm = loanUtilizationCheckModel


    }

    override fun getSelfNavDrawerItem() = R.id.loan_utilization_check

    fun saveButtonClick(view: View) {
        saveLoanUtilizationCheckDetails()
        // logD(""+loanUtilizationCheckModel)
    }

    fun addButtonClick(view: View) {

        if (loanUtilizationCheckModel.memberCode.isBlank()) {
            lblMemberCode.error = "Member Code is required."
        } else {

            lblMemberCode.isErrorEnabled = false
            getMemberLoanDetail()
            chooseLoanDetailsSpinner?.isFocusable = true
            chooseLoanDetailsSpinner?.isClickable = true
        }


    }

    @SuppressLint("ClickableViewAccessibility")
    private fun getMemberLoanDetail() {
        logD("Token  -" + Constants.accessToken)


        loanList = ArrayList()

        compositeDisposable.add(loanUtilizationService.getMemberLoanDetail(apiAccessToken, dataBinding.loanUtilizationCheckVm?.memberCode!!)
                .processRequest(this, { loans ->

                    if (loans.isSuccess!!) {

                        loanDetails = loans.loanDetails
                        for (loan in loans.loanDetails) {
                            logD("loneid" + loan.id)
                            logD("Loan Name -" + loan.name)
                            loanList.add(loan.name)
                            //   lastUcDateText.setText(dateString)

                        }
                    } else {
                        showDialog(this, loans.message)
                    }
                }

                ) { err ->
                    logD("Status -  $err")
                    hideProgress()
                    when (err) {
                        Constants.TOKEN_REFRESH_SUCCESS -> showDialog(this, "Access Token Expired !\nAgain click on submit button to process the transaction.")
                        Constants.TOKEN_REFRESH_FAILED -> {
                            toast("Session expired! Login again!")
                            logout()
                        }
                        else -> logD("Unknown error occurred - $err")
                    }
                }
        )

        val loanDetailsListAdapter = ArrayAdapter<String>(applicationContext, android.R.layout.simple_dropdown_item_1line, loanList)

        chooseLoanDetailsSpinner?.setAdapter<ArrayAdapter<String>>(loanDetailsListAdapter)

        chooseLoanDetailsSpinner?.setOnItemClickListener { adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->
            logD("loan details" + loanUtilizationCheckModel.loanDetails)
            var id: String = loanDetails!!.filter { loanObj -> loanObj.name.equals(loanUtilizationCheckModel.loanDetails) }.map { obj -> obj.id }.get(0)
            getLastVerificationDate(id)
            loanIdValue = id

        }
    }


    private fun getLastVerificationDate(loanId: String) {
        showProgress()
        compositeDisposable.add(loanUtilizationService.getLastVerificationDate(apiAccessToken, loanId)
                .processRequest(this, { lastDate ->
                    hideProgress()
                    logD(JsonHelper.KtToJson(lastDate))
                    //   lastUcDateText.setTex)
                    lastUcDateText.setText(lastDate.message)

                }

                ) { err ->
                    logD("Status -  $err")
                    hideProgress()
                    when (err) {
                        Constants.TOKEN_REFRESH_SUCCESS -> showDialog(this, "Access Token Expired !\nAgain click on submit button to process the transaction.")
                        Constants.TOKEN_REFRESH_FAILED -> {
                            toast("Session expired! Login again!")
                            logout()
                        }
                        else -> logD("Unknown error occurred - $err")
                    }
                }
        )
    }

    private fun saveLoanUtilizationCheckDetails() {

        if (!validate())
            return
        val loanUtilizationSaveModel = LoanUtilizationSaveModel(loanIdValue,
                loanUtilizationCheckModel.utilizationStatus.toBoolean(),
                loanUtilizationCheckModel.loanUtilizedForSamePurpose.toBoolean(),
                loanUtilizationCheckModel.aveargeMonthlyIncome,
                loanUtilizationCheckModel.remarks

        )
        compositeDisposable.add(loanUtilizationService.postLoanUtilizationData(apiAccessToken, loanUtilizationSaveModel)
                .processRequest(this, { response ->

                    logD("Status - ${response.isSuccess}")
                    logD("Message - ${response.message}")

                    toast("" + response.message)

                    startActivity(intent)

                }

                ) { err ->
                    logD("Status -  $err")
                    hideProgress()
                    when (err) {
                        Constants.TOKEN_REFRESH_SUCCESS -> showDialog(this, "Access Token Expired !\nAgain click on submit button to process the transaction.")
                        Constants.TOKEN_REFRESH_FAILED -> {
                            toast("Session expired! Login again!")
                            logout()
                        }
                        else -> logD("Unknown error occurred - $err")
                    }
                }
        )


    }


    private fun validate(): Boolean {
        if (loanUtilizationCheckModel.memberCode.isBlank()) {
            lblMemberCode.error = "Member Code is required."
            return false
        } else {
            lblMemberCode.isErrorEnabled = false
        }


        if (loanUtilizationCheckModel.loanDetails.isBlank()) {
            chooseLoanDetailsSpinner?.error = "Loan Details is required"
            return false
        } else {
            chooseLoanDetailsSpinner?.error = null
        }

        if (loanUtilizationCheckModel.aveargeMonthlyIncome.isBlank()) {

            if (utilizationStatus.isChecked) {

                lblAverageMonthlyIncome.error = "Average Monthly Income Is Required"
            } else {
                lblAverageMonthlyIncome.isErrorEnabled = false
                dataBinding.loanUtilizationCheckVm?.aveargeMonthlyIncome = "0"
            }
        }

        return true
    }

}





