package net.rmitsolutions.mfexpert.lms.sample

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import android.view.View
import io.reactivex.disposables.CompositeDisposable
import net.rmitsolutions.libcam.LibPermissions
import net.rmitsolutions.mfexpert.lms.Constants
import net.rmitsolutions.mfexpert.lms.MfExpertApp
import net.rmitsolutions.mfexpert.lms.R
import net.rmitsolutions.mfexpert.lms.database.MfExpertLmsDatabase
import net.rmitsolutions.mfexpert.lms.dependency.components.DaggerInjectActivityComponent
import net.rmitsolutions.mfexpert.lms.helpers.*
import net.rmitsolutions.mfexpert.lms.location.LocationHelper
import net.rmitsolutions.mfexpert.lms.location.LocationListener
import net.rmitsolutions.mfexpert.lms.network.IRepayment
import net.rmitsolutions.mfexpert.lms.viewmodels.Repayment
import org.jetbrains.anko.toast
import javax.inject.Inject

class SampleActivity : AppCompatActivity() {

    private val TAG = SampleActivity::class.java
    internal lateinit var compositeDisposable: CompositeDisposable
    @Inject
    lateinit var repayService : IRepayment

    private val permissions = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)



//        val toolbar = findViewById<Toolbar>(R.id.toolbar_actionbar)
//        setSupportActionBar(toolbar)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        toolbar.setNavigationOnClickListener{
//            finishAfterTransition()
//        }


        compositeDisposable = CompositeDisposable()

        val depComponent = DaggerInjectActivityComponent.builder()
                .applicationComponent(MfExpertApp.applicationComponent)
                .build()
        depComponent.injectSampleActivity(this)
    }

    fun testService(view: View){
//        val clientId = 21213.toLong()
//        getMemberLoanDetails(clientId)

//        postRepaymentDataTrue().
        postRepaymentDataFalse()
    }


    private fun getMemberLoanDetails(clientId : Long){
//        val memberId = "M0091805950"
//        compositeDisposable.add(repayService.getMemberLoanDetails(apiAccessToken,clientId).processRequest(this,{memberLoanDetails ->
//            if (memberLoanDetails!=null){
//                logD("Member Code - ${memberLoanDetails.memberCode}, Member Name - ${memberLoanDetails.memberName}")
//                val loans = memberLoanDetails.loanDetails
//
//                for (loan in loans){
//                    logD("Product Id - ${loan.productId}\n" +
//                            "Product Name - ${loan.productName}")
//
//                    val preClose = loan.preCloseTypes
//                    for (pre in preClose){
//                        logD("${pre.name}")
//                    }
//                }
//            }else{
//                logD("Member loan details is null")
//            }
//
//
//        },{err->
//            logD("Error - $err")
//
//        }))
    }

    /*
    * [
  {
    "memberId": 0,
    "recoveryDate": "2018-09-24T19:55:40.847Z",
    "repaymentType": 0,
    "paidAmount": 0,
    "isPreClosure": true,
    "loanRepaymentDetails": [
      {
        "loanId": 0,
        "preClosureType": 0,
        "paidAmount": 0,
        "adjustetAmount": 0,
        "bankIFSC": "string",
        "bankName": "string",
        "bankAccountNo": "string"
      }
    ],
    "arrearReasonId": 0
  }
]
*/

    // Working Post Data with isPreClosure = true
    private fun postRepaymentDataTrue(){

        val repaymentDetail = Repayment.RepaymentDetail()

        repaymentDetail.memberId = 0
        repaymentDetail.repaymentType = 0
        repaymentDetail.paidAmount = 0.toDouble()
        repaymentDetail.isPreClosure = true




        val loans = Repayment.LoanRepaymentDetail()

        loans.loanId = 0
        loans.preClosureType = 0
        loans.adjustedAmount = 0.toDouble()
        loans.paidAmount = 0.toDouble()
        loans.bankIFSC = "SBIN0007726"
        loans.bankAccountNo = "32895984735"
        loans.bankName = "SBI"

        val loanList = ArrayList<Repayment.LoanRepaymentDetail>()

        loanList.add(loans)

        repaymentDetail.loanRepaymentDetails = loanList


        val repaymentDetailsList = ArrayList<Repayment.RepaymentDetail>()

        repaymentDetailsList.add(repaymentDetail)

        for (data in repaymentDetailsList){
            for (d in data.loanRepaymentDetails!!){
                logD("Bank Acc No - ${d.bankAccountNo} \nBank IFSC - ${d.bankIFSC}\nBank Name - ${d.bankName}")
            }
        }



//        repaymentDetail.memberId = 918000000005030
//        repaymentDetail.repaymentType = 0
//        repaymentDetail.paidAmount = 0.0
//        repaymentDetail.isPreClosure = false





//        loans.loanId = 918000000017719
//        loans.preClosureType = 0
//        loans.adjustedAmount = 0.0
//        loans.paidAmount = 0.0
//        loans.bankIFSC = "SBIN0007726"
//        loans.bankAccountNo = "32895984735"
//        loans.bankName = "SBI"




        compositeDisposable.add(repayService.postRepaymentDetails(apiAccessToken, repaymentDetailsList).processRequest(this, { response ->
            if (response.isSuccess) {
                toast("Loan Repayment Details Post Successfully")
            } else {
                toast("${response.message}")
            }
        }, { err ->
            toast("Error - $err")
        }))
    }


    // Post data with isPreClosure = false
    private fun postRepaymentDataFalse(){
        val repaymentDetail = Repayment.RepaymentDetail()

        repaymentDetail.memberId = 0
        repaymentDetail.repaymentType = 0
        repaymentDetail.paidAmount = 0.toDouble()
        repaymentDetail.isPreClosure = false




        val loans = Repayment.LoanRepaymentDetail()

        loans.loanId = 0
        loans.preClosureType = 0
        loans.adjustedAmount = 0.toDouble()
        loans.paidAmount = 0.toDouble()
        loans.bankIFSC = "SBIN0007726"
        loans.bankAccountNo = "32895984735"
        loans.bankName = "SBI"

        val loanList = ArrayList<Repayment.LoanRepaymentDetail>()

        loanList.add(loans)

        repaymentDetail.loanRepaymentDetails = null


        val repaymentDetailsList = ArrayList<Repayment.RepaymentDetail>()

        repaymentDetailsList.add(repaymentDetail)

//        for (data in repaymentDetailsList){
//            for (d in data.loanRepaymentDetails!!){
//                logD("Bank Acc No - ${d.bankAccountNo} \nBank IFSC - ${d.bankIFSC}\nBank Name - ${d.bankName}")
//            }
//        }



//        repaymentDetail.memberId = 918000000005030
//        repaymentDetail.repaymentType = 0
//        repaymentDetail.paidAmount = 0.0
//        repaymentDetail.isPreClosure = false





//        loans.loanId = 918000000017719
//        loans.preClosureType = 0
//        loans.adjustedAmount = 0.0
//        loans.paidAmount = 0.0
//        loans.bankIFSC = "SBIN0007726"
//        loans.bankAccountNo = "32895984735"
//        loans.bankName = "SBI"




        compositeDisposable.add(repayService.postRepaymentDetails(apiAccessToken, repaymentDetailsList).processRequest(this, { response ->
            if (response.isSuccess) {
                toast("Loan Repayment Details Post Successfully")
                logD("Response is successful")
            } else {
                toast("${response.message}")
                logD("Message - ${response.message}")
            }
        }, { err ->
            toast("Error - $err")
        }))
    }


}
