package net.rmitsolutions.mfexpert.lms.sample

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.rmitsolutions.libcam.LibPermissions
import net.rmitsolutions.mfexpert.lms.BaseActivity
import net.rmitsolutions.mfexpert.lms.MfExpertApp
import net.rmitsolutions.mfexpert.lms.R
import net.rmitsolutions.mfexpert.lms.dependency.components.DaggerInjectActivityComponent
import net.rmitsolutions.mfexpert.lms.helpers.*
import net.rmitsolutions.mfexpert.lms.loans.LoanFourFragment
import net.rmitsolutions.mfexpert.lms.loans.LoanOneFragment
import net.rmitsolutions.mfexpert.lms.loans.LoanThreeFragment
import net.rmitsolutions.mfexpert.lms.loans.LoanTwoFragment
import net.rmitsolutions.mfexpert.lms.network.IRepayment
import net.rmitsolutions.mfexpert.lms.repayment.adapter.ClientAdapter
import net.rmitsolutions.mfexpert.lms.viewmodels.Repayment
import org.jetbrains.anko.find
import org.jetbrains.anko.toast
import javax.inject.Inject

class SampleActivity : BaseActivity(), View.OnClickListener {

    //internal lateinit var compositeDisposable: CompositeDisposable
    @Inject
    lateinit var repayService : IRepayment

    private lateinit var buttonPostData: Button

    private val permissions = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.CAMERA)
    private lateinit var libPermissions: LibPermissions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)

        val depComponent = DaggerInjectActivityComponent.builder()
                .applicationComponent(MfExpertApp.applicationComponent)
                .build()
        depComponent.injectSampleActivity(this)

        buttonPostData = find(R.id.buttonPostData)

        buttonPostData.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.buttonPostData ->{
                libPermissions = LibPermissions(this, permissions)
                val runnable = Runnable {
                    logD("All Permission Enabled")
                }
                libPermissions.askPermissions(runnable)
            }
        }
    }


}
