package net.rmitsolutions.mfexpert.lms.settings

import android.accounts.Account
import android.accounts.AccountManager
import android.content.ContentResolver
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import net.rmitsolutions.mfexpert.lms.BaseActivity
import net.rmitsolutions.mfexpert.lms.Constants
import net.rmitsolutions.mfexpert.lms.R
import net.rmitsolutions.mfexpert.lms.settings.adapter.SettingsAdapter
import net.rmitsolutions.mfexpert.lms.viewmodels.SettingViewModel
import org.jetbrains.anko.toast
import android.support.v7.widget.DividerItemDecoration
import android.view.MotionEvent


class Settings : BaseActivity() {

    private val TAG = Settings::class.java.simpleName
    private lateinit var settingRecyclerView : RecyclerView
    val adapter: SettingsAdapter by lazy { SettingsAdapter() }
    lateinit var modelList: MutableList<SettingViewModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        settingRecyclerView = findViewById(R.id.setting_recycler_view)
        settingRecyclerView.layoutManager = LinearLayoutManager(this)
        settingRecyclerView.setHasFixedSize(true)
        settingRecyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        settingRecyclerView.adapter = adapter
        modelList = mutableListOf<SettingViewModel>()


        setSettingsItem()

    }

    private fun setSettingsItem() {
        modelList.clear()
        val settingViewModel : SettingViewModel
        modelList.add(SettingViewModel("Profile settings", R.mipmap.ic_profile))
        modelList.add(SettingViewModel("Data & sync", R.mipmap.ic_sync))
        setRecyclerViewAdapter(modelList)

    }

    private fun setRecyclerViewAdapter(list: List<SettingViewModel>) {
        adapter.items = list
        adapter.notifyDataSetChanged()
    }

    override fun getSelfNavDrawerItem() = R.id.nav_dashboard


//    fun dataAndSync(view : View){
//        val mAccount = getSettingsAccount()
//        ContentResolver.requestSync(mAccount, Constants.ACCOUNT_AUTHORITY_DISTRICTS, Bundle.EMPTY)
//        ContentResolver.requestSync(mAccount, Constants.ACCOUNT_AUTHORITY_RELATIONS, Bundle.EMPTY)
//        ContentResolver.requestSync(mAccount, Constants.ACCOUNT_AUTHORITY_OCCUPATIONS, Bundle.EMPTY)
//        ContentResolver.requestSync(mAccount, Constants.ACCOUNT_AUTHORITY_LITERACY, Bundle.EMPTY)
//        ContentResolver.requestSync(mAccount, Constants.ACCOUNT_AUTHORITY_PRIMARY_KYC, Bundle.EMPTY)
//        ContentResolver.requestSync(mAccount, Constants.ACCOUNT_AUTHORITY_SECONDARY_KYC, Bundle.EMPTY)
//        ContentResolver.requestSync(mAccount, Constants.ACCOUNT_AUTHORITY_LOAN_PURPOSE, Bundle.EMPTY)
//        toast("Data sync successfully !")
//
//    }

    private fun getSettingsAccount(): Account? {
        val newAccount = Account(Constants.ACCOUNT_NAME, Constants.ACCOUNT_TYPE)
        val accountManager = this.getSystemService(Context.ACCOUNT_SERVICE) as AccountManager

        val accounts = accountManager.getAccountsByType(Constants.ACCOUNT_TYPE)
        if (accounts.isNotEmpty()) {
            Log.d(TAG, "Account ${Constants.ACCOUNT_NAME} already exists.")
            return newAccount
        }
        return null
    }
}
