package net.rmitsolutions.mfexpert.lms.sync.secondarykyc

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
import net.rmitsolutions.mfexpert.lms.sync.primarykyc.SyncPrimaryKyc
import java.util.ArrayList

class SecondaryKycSyncAdapter(context: Context, autoInitialize: Boolean, allowParallelSyncs: Boolean, database : MfExpertLmsDatabase) : AbstractThreadedSyncAdapter(context, autoInitialize, allowParallelSyncs) {


    private val TAG = SecondaryKycSyncAdapter::class.java.simpleName
    private val accountManager : AccountManager
    private val database = database

    init {
        accountManager = AccountManager.get(context)
    }

    override fun onPerformSync(account: Account?, bundle: Bundle?, p2: String?, contentProvider: ContentProviderClient?, syncResult: SyncResult?) {
        try {
            Constants.logD(TAG, "Secondary Kyc onPerformSync for account[" + account?.name + "]")
            Constants.logD(TAG, "Secondary Kyc sync started and running...")

            // Get all secondary kyc from api service and store in local DB
            val syncSecondaryKyc = SyncSecondaryKyc()
            var messages = ArrayList<String>()

            // Sync Secondary Kyc
            var message = syncSecondaryKyc.syncSecondaryKyc(database)
            if (!Globals.isEmptyString(message)) {
                messages.add("Secondary Kyc : $message")
            }

            if (messages.size == 0){
                Constants.logD(TAG, "Secondary Kyc sync success !")
            }else{
                NotificationHelper.notifyGroupedError(context, "Secondary Kyc Sync failed", messages.size.toString() + " Modules failed to sync", messages)
            }
        }finally {
            Constants.logD(TAG, "Secondary Kyc sync finally called...!")
        }
    }
}