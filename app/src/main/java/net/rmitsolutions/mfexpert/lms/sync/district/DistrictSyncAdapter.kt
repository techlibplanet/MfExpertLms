package net.rmitsolutions.mfexpert.lms.sync.district

import android.accounts.Account
import android.accounts.AccountManager
import android.app.Activity
import android.content.*
import android.os.Bundle
import android.widget.Toast
import net.rmitsolutions.libcam.Constants
import net.rmitsolutions.libcam.Constants.logD
import net.rmitsolutions.mfexpert.lms.Constants.getFormatDate
import net.rmitsolutions.mfexpert.lms.database.MfExpertLmsDatabase
import net.rmitsolutions.mfexpert.lms.helpers.NotificationHelper
import net.rmitsolutions.mfexpert.lms.helpers.SharedPrefKeys
import net.rmitsolutions.mfexpert.lms.helpers.putPref
import net.rmitsolutions.mfexpert.lms.models.Globals
import net.rmitsolutions.mfexpert.lms.settings.SettingsActivity
import net.rmitsolutions.mfexpert.lms.settings.adapter.adaptersyncsettings.SyncSettingsViewHolder
import java.util.*

class DistrictSyncAdapter(context: Context, autoInitialize: Boolean, allowParallelSyncs: Boolean, database : MfExpertLmsDatabase) : AbstractThreadedSyncAdapter(context, autoInitialize, allowParallelSyncs) {


    private val TAG = DistrictSyncAdapter::class.java.simpleName
    private val accountManager : AccountManager
    private val database = database

    init {
        accountManager = AccountManager.get(context)
    }


    override fun onPerformSync(account: Account?, bundle: Bundle?, p2: String?, contentProvider: ContentProviderClient?, syncResult: SyncResult?) {
        try {
            Constants.logD(TAG, "Districts onPerformSync for account[" + account?.name + "]")
            Constants.logD(TAG, "Districts sync started and running...")

            // Get all district from api service and store in local DB
            val syncDistrict = SyncDistrict()
            var messages = ArrayList<String>()

            // Sync District
            var message = syncDistrict.syncDistricts(database)
            if (!Globals.isEmptyString(message)) {
                messages.add("District : $message")
            }

            if (messages.size == 0){
                Constants.logD(TAG, "District sync success !")
                context.putPref(SharedPrefKeys.SP_DISTRICT_SYNC_TIME, getFormatDate())
                context.sendBroadcast(Intent(SyncSettingsViewHolder.ACTION_FINISHED_SYNC).putExtra("position", 0));
            }else{
                NotificationHelper.notifyGroupedError(context, "District Sync failed", messages.size.toString() + " Modules failed to sync", messages)
            }
        }finally {
            logD(TAG,"District sync finally called...!")
        }
    }
}