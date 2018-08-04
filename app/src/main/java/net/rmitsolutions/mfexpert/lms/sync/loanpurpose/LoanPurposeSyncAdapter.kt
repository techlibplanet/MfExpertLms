package net.rmitsolutions.mfexpert.lms.sync.loanpurpose

import android.accounts.Account
import android.accounts.AccountManager
import android.content.*
import android.os.Bundle
import net.rmitsolutions.libcam.Constants
import net.rmitsolutions.libcam.Constants.logD
import net.rmitsolutions.mfexpert.lms.Constants.getFormatDate
import net.rmitsolutions.mfexpert.lms.database.MfExpertLmsDatabase
import net.rmitsolutions.mfexpert.lms.helpers.NotificationHelper
import net.rmitsolutions.mfexpert.lms.helpers.SharedPrefKeys
import net.rmitsolutions.mfexpert.lms.helpers.apiAccessToken
import net.rmitsolutions.mfexpert.lms.helpers.putPref
import net.rmitsolutions.mfexpert.lms.models.Globals
import net.rmitsolutions.mfexpert.lms.network.IMasters
import net.rmitsolutions.mfexpert.lms.settings.adapter.adaptersyncsettings.SyncSettingsViewHolder
import net.rmitsolutions.mfexpert.lms.sync.SyncMasters
import java.util.ArrayList

class LoanPurposeSyncAdapter(context: Context, autoInitialize: Boolean, allowParallelSyncs: Boolean, database : MfExpertLmsDatabase, mastersService : IMasters) : AbstractThreadedSyncAdapter(context, autoInitialize, allowParallelSyncs) {


    private val TAG = LoanPurposeSyncAdapter::class.java.simpleName
    private val accountManager : AccountManager
    private val database = database
    private val masterService = mastersService

    init {
        accountManager = AccountManager.get(context)
    }

    override fun onPerformSync(account: Account?, bundle: Bundle?, p2: String?, contentProvider: ContentProviderClient?, syncResult: SyncResult?) {
        try {
            Constants.logD(TAG, "Loan purpose onPerformSync for account[" + account?.name + "]")
            Constants.logD(TAG, "Loan purpose sync started and running...")

            // Get all loan purpose from api service and store in local DB
            val syncMasters = SyncMasters()
            var messages = ArrayList<String>()

            // Sync Loan purpose
            var message = syncMasters.syncLoanPurpose(context.apiAccessToken, database, masterService)
            logD(TAG, "Message - $message")
            if (message == "Unauthorized"){
                Globals.refreshToken(context)
                return
            }
            if (!Globals.isEmptyString(message)) {
                messages.add("Loan purpose : $message")
            }

            if (messages.size == 0){
                Constants.logD(TAG, "Loan purpose sync success !")
                context.putPref(SharedPrefKeys.SP_LOAN_PURPOSE_SYNC_TIME, getFormatDate())
                context.sendBroadcast(Intent(SyncSettingsViewHolder.ACTION_FINISHED_SYNC).putExtra("position", 6));
            }else{
//                NotificationHelper.notifyGroupedError(context, "Loan purpose Sync failed", messages.size.toString() + " Modules failed to sync", messages)
            }
        }finally {
            Constants.logD(TAG, "Loan purpose sync finally called...!")
        }
    }
}