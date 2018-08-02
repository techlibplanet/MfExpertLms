package net.rmitsolutions.mfexpert.lms.settings.adapter.adaptersyncsettings

import android.accounts.Account
import android.accounts.AccountManager
import android.content.*
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import net.rmitsolutions.libcam.Constants.logD
import net.rmitsolutions.mfexpert.lms.Constants
import net.rmitsolutions.mfexpert.lms.R
import net.rmitsolutions.mfexpert.lms.helpers.SharedPrefKeys
import net.rmitsolutions.mfexpert.lms.helpers.getPref
import net.rmitsolutions.mfexpert.lms.helpers.isNetConnected
import net.rmitsolutions.mfexpert.lms.viewmodels.SyncViewModels

class SyncSettingsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val syncIntentFilter = IntentFilter(ACTION_FINISHED_SYNC)
    private var view: View? = null
    private var lastSync = ""
    private var context: Context? = null
    private lateinit var textViewSyncDescription: TextView

    fun bindView(context: Context, settingViewModel: SyncViewModels, position: Int) {
        this.context = context
        val textViewSyncHeader = itemView.findViewById<TextView>(R.id.sync_header_name)
        textViewSyncDescription = itemView.findViewById<TextView>(R.id.sync_details)
        val syncSwitch = itemView.findViewById<Switch>(R.id.sync_switch)
        textViewSyncHeader.text = settingViewModel.syncHeader
        textViewSyncDescription.text = "Last synced ${settingViewModel.syncDescription}"
        val account = getSettingsAccount()

        syncSwitch.setOnClickListener {
            view = it
            if (context.isNetConnected()){
                if (syncSwitch.isChecked) {
                    context.registerReceiver(syncBroadcastReceiver, syncIntentFilter);
                    when (position) {
                        0 -> ContentResolver.requestSync(account, Constants.ACCOUNT_AUTHORITY_DISTRICTS, Bundle.EMPTY)
                        1 -> ContentResolver.requestSync(account, Constants.ACCOUNT_AUTHORITY_RELATIONS, Bundle.EMPTY)
                        2 -> ContentResolver.requestSync(account, Constants.ACCOUNT_AUTHORITY_OCCUPATIONS, Bundle.EMPTY)
                        3 -> ContentResolver.requestSync(account, Constants.ACCOUNT_AUTHORITY_LITERACY, Bundle.EMPTY)
                        4 -> ContentResolver.requestSync(account, Constants.ACCOUNT_AUTHORITY_PRIMARY_KYC, Bundle.EMPTY)
                        5 -> ContentResolver.requestSync(account, Constants.ACCOUNT_AUTHORITY_SECONDARY_KYC, Bundle.EMPTY)
                        6 -> ContentResolver.requestSync(account, Constants.ACCOUNT_AUTHORITY_LOAN_PURPOSE, Bundle.EMPTY)
                        7 -> ContentResolver.requestSync(account, Constants.ACCOUNT_AUTHORITY_KYC_DETAILS, Bundle.EMPTY)
                        8 -> ContentResolver.requestSync(account, Constants.ACCOUNT_AUTHORITY_ASSIGN_CATEGORY, Bundle.EMPTY)
                        9 -> ContentResolver.requestSync(account, Constants.ACCOUNT_AUTHORITY_BANKS, Bundle.EMPTY)
                        10 -> ContentResolver.requestSync(account, Constants.ACCOUNT_AUTHORITY_CASTE, Bundle.EMPTY)
                        11 -> ContentResolver.requestSync(account, Constants.ACCOUNT_AUTHORITY_HOUSE_OWNERSHIP, Bundle.EMPTY)
                        12 -> ContentResolver.requestSync(account, Constants.ACCOUNT_AUTHORITY_INCOME_PROOF, Bundle.EMPTY)
                        13 -> ContentResolver.requestSync(account, Constants.ACCOUNT_AUTHORITY_LOAN_CLOSE_TYPE, Bundle.EMPTY)
                        14 -> ContentResolver.requestSync(account, Constants.ACCOUNT_AUTHORITY_LOAN_REJECTION, Bundle.EMPTY)
                        15 -> ContentResolver.requestSync(account, Constants.ACCOUNT_AUTHORITY_MEMBER_REJECTION, Bundle.EMPTY)
                        16 -> ContentResolver.requestSync(account, Constants.ACCOUNT_AUTHORITY_NATIONALITY, Bundle.EMPTY)
                        17 -> ContentResolver.requestSync(account, Constants.ACCOUNT_AUTHORITY_PRODUCTS, Bundle.EMPTY)
                        18 -> ContentResolver.requestSync(account, Constants.ACCOUNT_AUTHORITY_RELIGION, Bundle.EMPTY)
                    }
                }
            }else{
                Toast.makeText(context, "No Internet !", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val syncBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (ACTION_FINISHED_SYNC == intent.action) {
                val key = intent.getIntExtra("position", -1)
                when (key) {
                    0 -> lastSync = context.getPref(SharedPrefKeys.SP_DISTRICT_SYNC_TIME, "")
                    1 -> lastSync = context.getPref(SharedPrefKeys.SP_RELATION_SYNC_TIME, "")
                    2 -> lastSync = context.getPref(SharedPrefKeys.SP_OCCUPATION_SYNC_TIME, "")
                    3 -> lastSync = context.getPref(SharedPrefKeys.SP_LITERACY_SYNC_TIME, "")
                    4 -> lastSync = context.getPref(SharedPrefKeys.SP_PRIMARY_KYC_SYNC_TIME, "")
                    5 -> lastSync = context.getPref(SharedPrefKeys.SP_SECONDARY_KYC_SYNC_TIME, "")
                    6 -> lastSync = context.getPref(SharedPrefKeys.SP_LOAN_PURPOSE_SYNC_TIME, "")
                    7 -> lastSync = context.getPref(SharedPrefKeys.SP_KYC_DETAILS_SYNC_TIME, "")
                    8 -> lastSync = context.getPref(SharedPrefKeys.SP_ASSIGN_CATEGORY_SYNC_TIME, "")
                    9 -> lastSync = context.getPref(SharedPrefKeys.SP_BANKS_SYNC_TIME, "")
                    10 -> lastSync = context.getPref(SharedPrefKeys.SP_CASTE_SYNC_TIME, "")
                    11 -> lastSync = context.getPref(SharedPrefKeys.SP_HOUSE_OWNERSHIP_SYNC_TIME, "")
                    12 -> lastSync = context.getPref(SharedPrefKeys.SP_INCOME_PROOF_SYNC_TIME, "")
                    13 -> lastSync = context.getPref(SharedPrefKeys.SP_LOAN_CLOSE_TYPE_SYNC_TIME, "")
                    14 -> lastSync = context.getPref(SharedPrefKeys.SP_LOAN_REJECTION_REASON_SYNC_TIME, "")
                    15 -> lastSync = context.getPref(SharedPrefKeys.SP_MEMBER_REJECTION_REASON_SYNC_TIME, "")
                    16 -> lastSync = context.getPref(SharedPrefKeys.SP_NATIONALITY_SYNC_TIME, "")
                    17 -> lastSync = context.getPref(SharedPrefKeys.SP_PRODUCTS_SYNC_TIME, "")
                    18 -> lastSync = context.getPref(SharedPrefKeys.SP_RELIGION_SYNC_TIME, "")
                }
                setDescriptionTextView(view!!, lastSync)
            }
        }
    }

    private fun setDescriptionTextView(view: View, text: String) {
        if (text != "") {
            textViewSyncDescription.text = "Last synced $text"
            context?.unregisterReceiver(syncBroadcastReceiver)
            return
        }
        textViewSyncDescription.text = "Not synced yet"
        context?.unregisterReceiver(syncBroadcastReceiver)
    }


    private fun getSettingsAccount(): Account? {
        val newAccount = Account(Constants.ACCOUNT_NAME, Constants.ACCOUNT_TYPE)
        val accountManager = context?.getSystemService(Context.ACCOUNT_SERVICE) as AccountManager
        val accounts = accountManager.getAccountsByType(Constants.ACCOUNT_TYPE)
        if (accounts.isNotEmpty()) {
            Log.d("TAG", "Account ${Constants.ACCOUNT_NAME} already exists.")
            return newAccount
        }
        return null
    }

    companion object {
        const val ACTION_FINISHED_SYNC = "net.rmitsolutions.mfexpert.lms.ACTION_FINISHED_SYNC"
    }
}