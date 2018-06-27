package net.rmitsolutions.mfexpert.lms.settings

import android.accounts.Account
import android.accounts.AccountManager
import android.content.Context
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.util.Log
import net.rmitsolutions.mfexpert.lms.BaseActivity
import net.rmitsolutions.mfexpert.lms.Constants
import net.rmitsolutions.mfexpert.lms.R
import net.rmitsolutions.mfexpert.lms.viewmodels.SettingViewModel
import net.rmitsolutions.mfexpert.lms.settings.adapter.adaptersettings.SettingsAdapter
import net.rmitsolutions.mfexpert.lms.settings.adapter.adaptersettings.SettingsMenuFragment
import net.rmitsolutions.mfexpert.lms.settings.adapter.adaptersyncsettings.SyncSettingsFragment


class SettingsActivity : BaseActivity(), SettingsMenuFragment.OnFragmentInteractionListener, SyncSettingsFragment.OnFragmentInteractionListener {


    private val TAG = SettingsAdapter::class.java.simpleName
    private lateinit var settingRecyclerView : RecyclerView
    val adapter: SettingsAdapter by lazy { SettingsAdapter() }
    private lateinit var modelList: MutableList<SettingViewModel>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

//        settingRecyclerView = findViewById(R.id.setting_recycler_view)
//        settingRecyclerView.layoutManager = LinearLayoutManager(this)
//        settingRecyclerView.setHasFixedSize(true)
//        settingRecyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
//
//        settingRecyclerView.adapter = adapter
//        modelList = mutableListOf<SettingViewModel>()



//        setSettingsItem()

        val settingMenuFragment = SettingsMenuFragment()
        switchToFragment(settingMenuFragment)


    }


    private fun setSettingsItem() {
        modelList.clear()
        modelList.add(SettingViewModel("Profile",R.mipmap.ic_profile))
        modelList.add(SettingViewModel("Data & Sync",R.mipmap.ic_sync))
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

    // Switch UI to the given fragment
    private fun switchToFragment(newFrag: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, newFrag)
                .commit()
    }

    override fun onFragmentInteraction(uri: Uri) {

    }
}
