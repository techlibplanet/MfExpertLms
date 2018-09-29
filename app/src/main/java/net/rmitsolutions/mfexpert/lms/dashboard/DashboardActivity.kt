package net.rmitsolutions.mfexpert.lms.dashboard

import android.Manifest
import android.accounts.Account
import android.accounts.AccountManager
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import net.rmitsolutions.libcam.LibPermissions
import net.rmitsolutions.mfexpert.lms.BaseActivity
import net.rmitsolutions.mfexpert.lms.Constants
import net.rmitsolutions.mfexpert.lms.Constants.ACCOUNT_AUTHORITY_BANKS
import net.rmitsolutions.mfexpert.lms.Constants.ACCOUNT_AUTHORITY_CASTE
import net.rmitsolutions.mfexpert.lms.Constants.ACCOUNT_AUTHORITY_DISTRICTS
import net.rmitsolutions.mfexpert.lms.Constants.ACCOUNT_AUTHORITY_HOUSE_OWNERSHIP
import net.rmitsolutions.mfexpert.lms.Constants.ACCOUNT_AUTHORITY_INCOME_PROOF
import net.rmitsolutions.mfexpert.lms.Constants.ACCOUNT_AUTHORITY_LITERACY
import net.rmitsolutions.mfexpert.lms.Constants.ACCOUNT_AUTHORITY_LOAN_PURPOSE
import net.rmitsolutions.mfexpert.lms.Constants.ACCOUNT_AUTHORITY_OCCUPATIONS
import net.rmitsolutions.mfexpert.lms.Constants.ACCOUNT_AUTHORITY_PRIMARY_KYC
import net.rmitsolutions.mfexpert.lms.Constants.ACCOUNT_AUTHORITY_RELATIONS
import net.rmitsolutions.mfexpert.lms.Constants.ACCOUNT_AUTHORITY_RELIGION
import net.rmitsolutions.mfexpert.lms.Constants.ACCOUNT_AUTHORITY_SECONDARY_KYC
import net.rmitsolutions.mfexpert.lms.MfExpertApp
import net.rmitsolutions.mfexpert.lms.R
import net.rmitsolutions.mfexpert.lms.dependency.components.DaggerInjectActivityComponent
import net.rmitsolutions.mfexpert.lms.helpers.logD
import net.rmitsolutions.mfexpert.lms.helpers.logE

class DashboardActivity : BaseActivity() {

    private lateinit var libPermissions: LibPermissions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val permissions = arrayOf<String>(Manifest.permission.GET_ACCOUNTS)
        libPermissions = LibPermissions(this, permissions)

        // Compulsory enable master sync automatically because disable on logout time
        ContentResolver.setMasterSyncAutomatically(true)
        // To enable Master data sync
        enableMasterSync()

        val depComponent = DaggerInjectActivityComponent.builder()
                .applicationComponent(MfExpertApp.applicationComponent)
                .build()

        depComponent.injectDashboardActivity(this)
    }

    override fun getSelfNavDrawerItem() = R.id.nav_dashboard

    private fun enableMasterSync(){
        val runnable = Runnable {
            logD("Account authenticator permission enabled")
            val mAccount = createSyncAccount(this)

            ContentResolver.addPeriodicSync(mAccount, ACCOUNT_AUTHORITY_DISTRICTS, Bundle(),1200)
            ContentResolver.addPeriodicSync(mAccount, ACCOUNT_AUTHORITY_RELATIONS, Bundle(), 1500)
            ContentResolver.addPeriodicSync(mAccount, ACCOUNT_AUTHORITY_OCCUPATIONS, Bundle(),1800)
            ContentResolver.addPeriodicSync(mAccount, ACCOUNT_AUTHORITY_LITERACY, Bundle(), 1500)
            ContentResolver.addPeriodicSync(mAccount, ACCOUNT_AUTHORITY_PRIMARY_KYC, Bundle(), 1200)
            ContentResolver.addPeriodicSync(mAccount, ACCOUNT_AUTHORITY_SECONDARY_KYC, Bundle(), 1800)
            ContentResolver.addPeriodicSync(mAccount, ACCOUNT_AUTHORITY_LOAN_PURPOSE, Bundle(), 1500)
            ContentResolver.addPeriodicSync(mAccount, ACCOUNT_AUTHORITY_BANKS, Bundle(),1200)
            ContentResolver.addPeriodicSync(mAccount, ACCOUNT_AUTHORITY_RELIGION, Bundle(),1200)
            ContentResolver.addPeriodicSync(mAccount, ACCOUNT_AUTHORITY_CASTE, Bundle(),1200)
            ContentResolver.addPeriodicSync(mAccount, ACCOUNT_AUTHORITY_HOUSE_OWNERSHIP, Bundle(),1200)
            ContentResolver.addPeriodicSync(mAccount, ACCOUNT_AUTHORITY_INCOME_PROOF, Bundle(),1200)


            ContentResolver.setSyncAutomatically(mAccount, ACCOUNT_AUTHORITY_DISTRICTS, true)
            ContentResolver.setSyncAutomatically(mAccount, ACCOUNT_AUTHORITY_OCCUPATIONS, true)
            ContentResolver.setSyncAutomatically(mAccount, ACCOUNT_AUTHORITY_RELATIONS, true)
            ContentResolver.setSyncAutomatically(mAccount, ACCOUNT_AUTHORITY_LITERACY, true)
            ContentResolver.setSyncAutomatically(mAccount, ACCOUNT_AUTHORITY_PRIMARY_KYC, true)
            ContentResolver.setSyncAutomatically(mAccount, ACCOUNT_AUTHORITY_SECONDARY_KYC, true)
            ContentResolver.setSyncAutomatically(mAccount, ACCOUNT_AUTHORITY_LOAN_PURPOSE, true)
            ContentResolver.setSyncAutomatically(mAccount, ACCOUNT_AUTHORITY_BANKS, true)
            ContentResolver.setSyncAutomatically(mAccount, ACCOUNT_AUTHORITY_RELIGION, true)
            ContentResolver.setSyncAutomatically(mAccount, ACCOUNT_AUTHORITY_CASTE, true)
            ContentResolver.setSyncAutomatically(mAccount, ACCOUNT_AUTHORITY_HOUSE_OWNERSHIP, true)
            ContentResolver.setSyncAutomatically(mAccount, ACCOUNT_AUTHORITY_INCOME_PROOF, true)

        }
        libPermissions.askPermissions(runnable)
    }


    private fun createSyncAccount(context: Context): Account? {
        // Create the account type and default account
        val newAccount = Account(Constants.ACCOUNT_NAME, Constants.ACCOUNT_TYPE)
        val accountManager = context.getSystemService(Context.ACCOUNT_SERVICE) as AccountManager

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            logE("Permission not granted. Please check !")
            return newAccount
        }
        val accounts = accountManager.getAccountsByType(Constants.ACCOUNT_TYPE)
        if (accounts.isNotEmpty()) {
            logD("Account ${Constants.ACCOUNT_NAME} already exists.")
            return newAccount
        }
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call context.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
            logD("Account ${Constants.ACCOUNT_NAME} added successfully.")
        }
        return newAccount
    }
}
