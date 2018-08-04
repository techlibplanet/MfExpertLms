package net.rmitsolutions.mfexpert.lms.sync.loanrejection

import android.accounts.Account
import android.accounts.AccountManager
import android.content.*
import android.os.Bundle
import net.rmitsolutions.libcam.Constants
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

class LoanRejectionReasonSyncAdapter(context: Context, autoInitialize: Boolean, allowParallelSyncs: Boolean, database : MfExpertLmsDatabase, masterService: IMasters) : AbstractThreadedSyncAdapter(context, autoInitialize, allowParallelSyncs) {

    private val TAG = LoanRejectionReasonSyncAdapter::class.java.simpleName
    private val accountManager : AccountManager
    private val database = database
    private val masterService = masterService

    init {
        accountManager = AccountManager.get(context)
    }


    override fun onPerformSync(account: Account?, bundle: Bundle?, p2: String?, contentProvider: ContentProviderClient?, syncResult: SyncResult?) {
        try {
            Constants.logD(TAG, "Loan Rejection Reason onPerformSync for account[" + account?.name + "]")
            Constants.logD(TAG, "Loan Rejection reason sync started and running...")

            // Get all district from api service and store in local DB
            val syncMasters = SyncMasters()
            var messages = ArrayList<String>()

            // Sync District
            var message = syncMasters.syncLoanRejectionReasons(context.apiAccessToken, database, masterService)
            Constants.logD("Loan rejection reason Sync", "Message $message")

            if (message == "Unauthorized"){
                Globals.refreshToken(context)
                return
            }
            
            if (!Globals.isEmptyString(message)) {
                messages.add("Loan Rejection Reason: $message")
            }

            if (messages.size == 0){
                Constants.logD(TAG, "Loan rejection reason sync success !")
                context.putPref(SharedPrefKeys.SP_LOAN_REJECTION_REASON_SYNC_TIME, net.rmitsolutions.mfexpert.lms.Constants.getFormatDate())
                context.sendBroadcast(Intent(SyncSettingsViewHolder.ACTION_FINISHED_SYNC).putExtra("position", 14));
            }else{
//                NotificationHelper.notifyGroupedError(context, "Loan Rejection Reason Sync failed", messages.size.toString() + " Modules failed to sync", messages)
            }
        }finally {
            Constants.logD(TAG, "Loan Rejection Reason sync finally called...!")
        }
    }
}