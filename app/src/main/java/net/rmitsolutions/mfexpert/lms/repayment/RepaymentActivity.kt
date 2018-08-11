package net.rmitsolutions.mfexpert.lms.repayment

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import net.rmitsolutions.mfexpert.lms.BaseActivity
import net.rmitsolutions.mfexpert.lms.R
import net.rmitsolutions.mfexpert.lms.repayment.adapter.ClientDetailAdapter
import net.rmitsolutions.mfexpert.lms.settings.adapter.adaptersettings.SettingsAdapter
import net.rmitsolutions.mfexpert.lms.viewmodels.ClientViewModels
import net.rmitsolutions.mfexpert.lms.viewmodels.SettingViewModel
import org.jetbrains.anko.find

class RepaymentActivity : BaseActivity() {

    private lateinit var clientRecyclerView : RecyclerView
    val adapter: ClientDetailAdapter by lazy { ClientDetailAdapter() }
    lateinit var modelList: MutableList<ClientViewModels>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repayment)

        clientRecyclerView = find(R.id.client_recycler_view)
        clientRecyclerView.layoutManager = LinearLayoutManager(this)
        clientRecyclerView.setHasFixedSize(true)
        clientRecyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        clientRecyclerView.adapter = adapter
        modelList = mutableListOf<ClientViewModels>()
        setSettingsItem()
    }

    private fun setSettingsItem() {
        modelList.clear()
        modelList.add(ClientViewModels("client1000001", "Spider Man"))
        modelList.add(ClientViewModels("client1000002", "Iron  Man"))
        modelList.add(ClientViewModels("client1000003", "Hulk Khan"))
        setRecyclerViewAdapter(modelList)
    }

    private fun setRecyclerViewAdapter(list: MutableList<ClientViewModels>) {
        adapter.items = list
        adapter.notifyDataSetChanged()
    }

    override fun getSelfNavDrawerItem() = R.id.nav_dashboard
}
