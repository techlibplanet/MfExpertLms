package net.rmitsolutions.mfexpert.lms.settings

import android.accounts.Account
import android.accounts.AccountManager
import android.content.ContentResolver
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import net.rmitsolutions.mfexpert.lms.BaseActivity
import net.rmitsolutions.mfexpert.lms.Constants
import net.rmitsolutions.mfexpert.lms.R
import org.jetbrains.anko.toast

class Settings : BaseActivity() {

    private val TAG = Settings::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
    }

    override fun getSelfNavDrawerItem() = R.id.nav_dashboard


    fun dataAndSync(view : View){
        val mAccount = getSettingsAccount()
        ContentResolver.requestSync(mAccount, Constants.ACCOUNT_AUTHORITY_DISTRICTS, Bundle.EMPTY)
        ContentResolver.requestSync(mAccount, Constants.ACCOUNT_AUTHORITY_RELATIONS, Bundle.EMPTY)
        ContentResolver.requestSync(mAccount, Constants.ACCOUNT_AUTHORITY_OCCUPATIONS, Bundle.EMPTY)
        ContentResolver.requestSync(mAccount, Constants.ACCOUNT_AUTHORITY_LITERACY, Bundle.EMPTY)
        ContentResolver.requestSync(mAccount, Constants.ACCOUNT_AUTHORITY_PRIMARY_KYC, Bundle.EMPTY)
        ContentResolver.requestSync(mAccount, Constants.ACCOUNT_AUTHORITY_SECONDARY_KYC, Bundle.EMPTY)
        ContentResolver.requestSync(mAccount, Constants.ACCOUNT_AUTHORITY_LOAN_PURPOSE, Bundle.EMPTY)
        toast("Data sync successfully !")

    }

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
