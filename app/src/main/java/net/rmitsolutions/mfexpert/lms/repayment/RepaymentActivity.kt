package net.rmitsolutions.mfexpert.lms.repayment

import android.net.Uri
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.SearchView
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
import net.rmitsolutions.mfexpert.lms.repayment.adapter.ClientDetailAdapter
import net.rmitsolutions.mfexpert.lms.viewmodels.Repayment
import org.jetbrains.anko.find
import org.jetbrains.anko.toast
import javax.inject.Inject


class RepaymentActivity : BaseActivity(),
        LoanOneFragment.OnFragmentInteractionListener,
        LoanTwoFragment.OnFragmentInteractionListener,
        LoanThreeFragment.OnFragmentInteractionListener,
        LoanFourFragment.OnFragmentInteractionListener {

    private lateinit var clientRecyclerView: RecyclerView
    val adapter: ClientDetailAdapter by lazy { ClientDetailAdapter() }
    lateinit var modelList: MutableList<RepaymentModel>
    @Inject
    lateinit var repayService: IRepayment
    private lateinit var buttonPostData: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repayment)

        buttonPostData = find(R.id.buttonPostData)

        buttonPostData.visibility = View.GONE

        clientRecyclerView = find(R.id.client_recycler_view)
        clientRecyclerView.layoutManager = LinearLayoutManager(this)
        clientRecyclerView.setHasFixedSize(true)
        clientRecyclerView.adapter = adapter
        modelList = mutableListOf<RepaymentModel>()

        val depComponent = DaggerInjectActivityComponent.builder()
                .applicationComponent(MfExpertApp.applicationComponent)
                .build()
        depComponent.injectRepaymentActivity(this)

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
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setRecyclerViewAdapter(list: List<RepaymentModel>) {
        adapter.items = list
        adapter.notifyDataSetChanged()
    }

    override fun getSelfNavDrawerItem() = R.id.nav_dashboard

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

    override fun onFragmentInteraction(uri: Uri) {

    }

    private fun validate(): Boolean{
        if (!isNetConnected(false)) {
            snackBar(buttonPostData, getString(R.string.you_are_offline))
            return false
        }
        return true
    }

    fun onSubmitData(view: View) {
        if (!validate()) return
        showProgress()
        compositeDisposable.add(repayService.postRepaymentDetails(apiAccessToken, Repayment.RepaymentData.repaymentDataList)
                .processRequest(this, { response ->
                    hideProgress()
                    if (response.isSuccess) {
                        toast("Repayment Details Post Successfully")
                        Repayment.RepaymentData.repaymentDataList.clear()
                        Repayment.RepaymentData.loanDetails.clear()
                        buttonPostData.visibility = View.GONE
                        startActivity(intent)
                    } else {
                        toast("${response.message}")
                        logD("Message - ${response.message}")
                    }
                }, { err ->
                    hideProgress()
                    toast("Error - $err")
                    logD("Error - $err")
                }))
    }
}
