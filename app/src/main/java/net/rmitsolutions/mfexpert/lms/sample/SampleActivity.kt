package net.rmitsolutions.mfexpert.lms.sample

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import android.view.View
import android.widget.Button
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.disposables.CompositeDisposable
import net.rmitsolutions.libcam.LibPermissions
import net.rmitsolutions.mfexpert.lms.BaseActivity
import net.rmitsolutions.mfexpert.lms.Constants
import net.rmitsolutions.mfexpert.lms.MfExpertApp
import net.rmitsolutions.mfexpert.lms.R
import net.rmitsolutions.mfexpert.lms.database.MfExpertLmsDatabase
import net.rmitsolutions.mfexpert.lms.dependency.components.DaggerInjectActivityComponent
import net.rmitsolutions.mfexpert.lms.helpers.*
import net.rmitsolutions.mfexpert.lms.location.LocationHelper
import net.rmitsolutions.mfexpert.lms.location.LocationListener
import net.rmitsolutions.mfexpert.lms.network.IRepayment
import net.rmitsolutions.mfexpert.lms.repayment.RepaymentModel
import net.rmitsolutions.mfexpert.lms.repayment.RepaymentParamsModel
import net.rmitsolutions.mfexpert.lms.repayment.adapter.ClientDetailAdapter
import net.rmitsolutions.mfexpert.lms.viewmodels.Repayment
import org.jetbrains.anko.find
import org.jetbrains.anko.toast
import javax.inject.Inject

class SampleActivity : BaseActivity() {

    //internal lateinit var compositeDisposable: CompositeDisposable
    @Inject
    lateinit var repayService : IRepayment

    private lateinit var clientRecyclerView: RecyclerView
    val adapter: ClientAdapter by lazy { ClientAdapter() }
    lateinit var modelList: MutableList<RepaymentModel>
    private lateinit var buttonPostData: Button

    private val permissions = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)

        val depComponent = DaggerInjectActivityComponent.builder()
                .applicationComponent(MfExpertApp.applicationComponent)
                .build()
        depComponent.injectSampleActivity(this)
        compositeDisposable = CompositeDisposable()

        buttonPostData = find(R.id.buttonPostData)

        buttonPostData.visibility = View.GONE

        clientRecyclerView = find(R.id.client_recycler_view)
        clientRecyclerView.layoutManager = LinearLayoutManager(this)
        clientRecyclerView.setHasFixedSize(true)
        clientRecyclerView.adapter = adapter
        modelList = mutableListOf<RepaymentModel>()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_repayment_client_search, menu)
        val searchView = menu?.findItem(R.id.action_search)?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> {
                logD("Search Clicked")
                false
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun filter(text: String) {
        if (!validate()) return
        showProgress()
        val repaymentParamsModel = RepaymentParamsModel(text, "GL")
        compositeDisposable.add(repayService.getGroupLoansList(apiAccessToken, repaymentParamsModel)
                .processRequest(this,
                        { dues ->
                            hideProgress()
                            buttonPostData.visibility = View.VISIBLE
                            // Adding Repayment Details to post
                            for (repayData in dues) {
                                val repaymentDetail = Repayment.RepaymentDetail()
                                repaymentDetail.memberId = repayData.id
                                repaymentDetail.repaymentType = 1
                                repaymentDetail.paidAmount = repayData.pastDue + repayData.currentDue + repayData.otherCharges
                                repaymentDetail.isPreClosure = false
                                Repayment.RepaymentData.repaymentDataList.add(repaymentDetail)
                            }
                            setRecyclerViewAdapter(dues)

                        }
                ) { err ->
                    hideProgress()
                    toast("Error: $err")
                }
        )
    }

    private fun validate(): Boolean{
        if (!isNetConnected(false)) {
            snackBar(buttonPostData, getString(R.string.you_are_offline))
            return false
        }
        return true
    }

    private fun setRecyclerViewAdapter(list: List<RepaymentModel>) {
        adapter.items = list
        adapter.notifyDataSetChanged()
    }


}
