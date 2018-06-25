package net.rmitsolutions.mfexpert.lms.sync.loanpurpose

import android.accounts.Account
import android.accounts.AccountManager
import android.content.AbstractThreadedSyncAdapter
import android.content.ContentProviderClient
import android.content.Context
import android.content.SyncResult
import android.os.Bundle
import net.rmitsolutions.libcam.Constants
import net.rmitsolutions.mfexpert.lms.database.MfExpertLmsDatabase
import net.rmitsolutions.mfexpert.lms.helpers.NotificationHelper
import net.rmitsolutions.mfexpert.lms.models.Globals
import net.rmitsolutions.mfexpert.lms.sync.district.SyncDistrict
import java.util.ArrayList

class LoanPurposeSyncAdapter(context: Context, autoInitialize: Boolean, allowParallelSyncs: Boolean, database : MfExpertLmsDatabase) : AbstractThreadedSyncAdapter(context, autoInitialize, allowParallelSyncs) {


    private val TAG = LoanPurposeSyncAdapter::class.java.simpleName
    private val accountManager : AccountManager
    private val database = database

    init {
        accountManager = AccountManager.get(context)
    }

    override fun onPerformSync(account: Account?, bundle: Bundle?, p2: String?, contentProvider: ContentProviderClient?, syncResult: SyncResult?) {
        try {
            Constants.logD(TAG, "Loan purpose onPerformSync for account[" + account?.name + "]")
            Constants.logD(TAG, "Loan purpose sync started and running...")

            // Get all loan purpose from api service and store in local DB
            val syncLoanPurpose = SyncLoanPurpose()
            var messages = ArrayList<String>()

            // Sync Loan purpose
            var message = syncLoanPurpose.syncLoanPurpose(database)
            if (!Globals.isEmptyString(message)) {
                messages.add("Loan purpose : $message")
            }

            if (messages.size == 0){
                Constants.logD(TAG, "Loan purpose sync success !")
            }else{
                NotificationHelper.notifyGroupedError(context, "Loan purpose Sync failed", messages.size.toString() + " Modules failed to sync", messages)
            }
        }finally {
            Constants.logD(TAG, "Loan purpose sync finally called...!")
        }
    }
}