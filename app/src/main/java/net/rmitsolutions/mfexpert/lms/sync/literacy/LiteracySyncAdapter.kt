package net.rmitsolutions.mfexpert.lms.sync.literacy

import android.accounts.Account
import android.accounts.AccountManager
import android.content.*
import android.os.Bundle
import net.rmitsolutions.libcam.Constants
import net.rmitsolutions.mfexpert.lms.Constants.getFormatDate
import net.rmitsolutions.mfexpert.lms.database.MfExpertLmsDatabase
import net.rmitsolutions.mfexpert.lms.helpers.NotificationHelper
import net.rmitsolutions.mfexpert.lms.helpers.SharedPrefKeys
import net.rmitsolutions.mfexpert.lms.helpers.putPref
import net.rmitsolutions.mfexpert.lms.models.Globals
import net.rmitsolutions.mfexpert.lms.settings.adapter.adaptersyncsettings.SyncSettingsViewHolder
import net.rmitsolutions.mfexpert.lms.sync.district.SyncDistrict
import java.util.ArrayList

class LiteracySyncAdapter(context: Context, autoInitialize: Boolean, allowParallelSyncs: Boolean, database : MfExpertLmsDatabase) : AbstractThreadedSyncAdapter(context, autoInitialize, allowParallelSyncs) {


    private val TAG = LiteracySyncAdapter::class.java.simpleName
    private val accountManager : AccountManager
    private val database = database

    init {
        accountManager = AccountManager.get(context)
    }

    override fun onPerformSync(account: Account?, bundle: Bundle?, p2: String?, contentProvider: ContentProviderClient?, syncResult: SyncResult?) {
        try {
            Constants.logD(TAG, "Literacy onPerformSync for account[" + account?.name + "]")
            Constants.logD(TAG, "Literacy sync started and running...")

            // Get all literacy from api service and store in local DB
            val syncLiteracy = SyncLiteracy()
            var messages = ArrayList<String>()

            // Sync Literacy
            var message = syncLiteracy.syncLiteracy(database)
            if (!Globals.isEmptyString(message)) {
                messages.add("Literacy : $message")
            }

            if (messages.size == 0){
                Constants.logD(TAG, "Literacy sync success !")
                context.putPref(SharedPrefKeys.SP_LITERACY_SYNC_TIME, getFormatDate())
                context.sendBroadcast(Intent(SyncSettingsViewHolder.ACTION_FINISHED_SYNC).putExtra("position", 3));
            }else{
                NotificationHelper.notifyGroupedError(context, "Literacy Sync failed", messages.size.toString() + " Modules failed to sync", messages)
            }
        }finally {
            Constants.logD(TAG, "Literacy sync finally called...!")
        }
    }
}