package net.rmitsolutions.mfexpert.lms.settings.adapter.adaptersyncsettings

import android.accounts.Account
import android.accounts.AccountManager
import android.content.ContentResolver
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Switch
import android.widget.TextView
import net.rmitsolutions.libcam.Constants.logD
import net.rmitsolutions.mfexpert.lms.Constants
import net.rmitsolutions.mfexpert.lms.R
import net.rmitsolutions.mfexpert.lms.helpers.SharedPrefKeys
import net.rmitsolutions.mfexpert.lms.helpers.getPref
import net.rmitsolutions.mfexpert.lms.viewmodels.SyncViewModels

class SyncSettingsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var context : Context? = null
    private val TAG = SyncSettingsViewHolder::class.java.simpleName
    private lateinit var textViewSyncDescription : TextView

    fun bindView(context: Context,settingViewModel: SyncViewModels, position: Int) {
        this.context = context
        val textViewSyncHeader = itemView.findViewById<TextView>(R.id.sync_header_name)
        textViewSyncDescription = itemView.findViewById<TextView>(R.id.sync_details)
        val syncSwitch = itemView.findViewById<Switch>(R.id.sync_switch)
        textViewSyncHeader.text = settingViewModel.syncHeader
        textViewSyncDescription.text = "Last synced ${settingViewModel.syncDescription}"
        val account = getSettingsAccount()
        syncSwitch.setOnClickListener {
            if (syncSwitch.isChecked) {
                when(position){
                    0 ->{
                        try {
                            ContentResolver.requestSync(account,Constants.ACCOUNT_AUTHORITY_DISTRICTS, Bundle.EMPTY)
                        }finally {
                            val lastSync = context.getPref(SharedPrefKeys.SP_DISTRICT_SYNC_TIME, "")
                            logD(TAG, "last synced $lastSync")
                            setDescriptionTextView(lastSync)
                        }
                    }
                    1 -> {
                        ContentResolver.requestSync(account,Constants.ACCOUNT_AUTHORITY_RELATIONS, Bundle.EMPTY)
                        val lastSync = context.getPref(SharedPrefKeys.SP_RELATION_SYNC_TIME, "")
                        setDescriptionTextView(lastSync)
                    }
                    2 ->{
                        ContentResolver.requestSync(account,Constants.ACCOUNT_AUTHORITY_OCCUPATIONS, Bundle.EMPTY)
                        val lastSync = context.getPref(SharedPrefKeys.SP_OCCUPATION_SYNC_TIME, "")
                        setDescriptionTextView(lastSync)
                    }
                    3 ->{
                        ContentResolver.requestSync(account,Constants.ACCOUNT_AUTHORITY_LITERACY, Bundle.EMPTY)
                        val lastSync = context.getPref(SharedPrefKeys.SP_LITERACY_SYNC_TIME, "")
                        setDescriptionTextView(lastSync)
                    }
                    4 ->{
                        ContentResolver.requestSync(account,Constants.ACCOUNT_AUTHORITY_PRIMARY_KYC, Bundle.EMPTY)
                        val lastSync = context.getPref(SharedPrefKeys.SP_PRIMARY_KYC_SYNC_TIME, "")
                        setDescriptionTextView(lastSync)
                    }
                    5 ->{
                        ContentResolver.requestSync(account,Constants.ACCOUNT_AUTHORITY_SECONDARY_KYC, Bundle.EMPTY)
                        val lastSync = context.getPref(SharedPrefKeys.SP_SECONDARY_KYC_SYNC_TIME, "")
                        setDescriptionTextView(lastSync)
                    }
                    6 ->{
                        ContentResolver.requestSync(account,Constants.ACCOUNT_AUTHORITY_LOAN_PURPOSE, Bundle.EMPTY)
                        val lastSync = context.getPref(SharedPrefKeys.SP_LOAN_PURPOSE_SYNC_TIME, "")
                        setDescriptionTextView(lastSync)
                    }
                }
            }

        }

    }

    private fun setDescriptionTextView(text : String){
        if (text != ""){
            textViewSyncDescription.text = "Last synced $text"
            return
        }
        textViewSyncDescription.text = "Not synced yet"
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


}