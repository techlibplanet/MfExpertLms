package net.rmitsolutions.mfexpert.lms.sync.relations

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

class RelationSyncAdapter(context: Context, autoInitialize: Boolean, allowParallelSyncs: Boolean, database : MfExpertLmsDatabase) : AbstractThreadedSyncAdapter(context, autoInitialize, allowParallelSyncs) {


    private val TAG = RelationSyncAdapter::class.java.simpleName
    private val accountManager : AccountManager
    private val database = database

    init {
        accountManager = AccountManager.get(context)
    }

    override fun onPerformSync(account: Account?, bundle: Bundle?, p2: String?, contentProvider: ContentProviderClient?, syncResult: SyncResult?) {
        try {
            Constants.logD(TAG, "Relations onPerformSync for account[" + account?.name + "]")
            Constants.logD(TAG, "Relations sync started and running...")

            // Get all relations from api service and store in local DB
            val syncRelations = SyncRelations()
            var messages = ArrayList<String>()

            // Sync Relations
            var message = syncRelations.syncRelations(database)
            if (!Globals.isEmptyString(message)) {
                messages.add("Relation : $message")
            }

            if (messages.size == 0){
                Constants.logD(TAG, "Relations sync success !")
            }else{
                NotificationHelper.notifyGroupedError(context, "Relations Sync failed", messages.size.toString() + " Modules failed to sync", messages)
            }
        }finally {
            Constants.logD(TAG, "Relation sync finally called...!")
        }
    }
}