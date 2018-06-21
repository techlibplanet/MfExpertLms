package net.rmitsolutions.mfexpert.lms.dashboard

import android.Manifest
import android.accounts.Account
import android.accounts.AccountManager
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.View
import android.widget.TextView
import net.rmitsolutions.libcam.LibPermissions
import net.rmitsolutions.mfexpert.lms.BaseActivity
import net.rmitsolutions.mfexpert.lms.Constants
import net.rmitsolutions.mfexpert.lms.Constants.ACCOUNT_AUTHORITY_MASTERS
import net.rmitsolutions.mfexpert.lms.MfExpertApp
import net.rmitsolutions.mfexpert.lms.R
import net.rmitsolutions.mfexpert.lms.dependency.components.DaggerInjectActivityComponent
import net.rmitsolutions.mfexpert.lms.helpers.*
import net.rmitsolutions.mfexpert.lms.network.IMasters
import javax.inject.Inject

class DashboardActivity : BaseActivity() {

    private val TAG = DashboardActivity::class.java.simpleName
    @Inject
    lateinit var mastersService: IMasters

    private lateinit var relationsList: TextView

    private lateinit var libPermissions: LibPermissions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        relationsList = findViewById(R.id.relationsList)

        val permissions = arrayOf<String>(Manifest.permission.GET_ACCOUNTS)

        libPermissions = LibPermissions(this, permissions)
        val runnable = Runnable {
            logD("Account authenticator permission enabled")
            val mAccount = createSyncAccount(this)
            ContentResolver.setSyncAutomatically(mAccount, ACCOUNT_AUTHORITY_MASTERS, true)
        }
        libPermissions.askPermissions(runnable)



        val depComponent = DaggerInjectActivityComponent.builder()
                .applicationComponent(MfExpertApp.applicationComponent)
                .build()

        depComponent.injectDashboardActivity(this)
    }

    override fun getSelfNavDrawerItem() = R.id.nav_dashboard

    fun getRelations(view: View) {
        showProgress()
        compositeDisposable.add(mastersService.getRelations(apiAccessToken)
                .processRequest(
                        { relations ->
                            hideProgress()
                            var rel = ""
                            relations.forEach { r -> rel += r.name + " == " }
                            relationsList.text = rel
                        },
                        { err ->
                            hideProgress(true, err)
                        }
                )
        )
    }


    private fun createSyncAccount(context: Context): Account? {
        logD("Inside Create account")
        // Create the account type and default account
        val newAccount = Account(Constants.ACCOUNT_NAME, Constants.ACCOUNT_TYPE)
        val accountManager = context.getSystemService(Context.ACCOUNT_SERVICE) as AccountManager

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            logE("Permission not granted. Please check !")
            return newAccount
        }
        val accounts = accountManager.getAccountsByType(Constants.ACCOUNT_TYPE)
        if (accounts.isNotEmpty()) {
            Log.d(TAG, "Account ${Constants.ACCOUNT_NAME} already exists.")
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
            Log.d(TAG, "Account ${Constants.ACCOUNT_NAME} added successfully.")
        }
        return newAccount
    }
}
