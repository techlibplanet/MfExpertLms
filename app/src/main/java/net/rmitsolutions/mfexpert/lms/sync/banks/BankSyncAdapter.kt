package net.rmitsolutions.mfexpert.lms.sync.banks

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

class BankSyncAdapter(context: Context, autoInitialize: Boolean, allowParallelSyncs: Boolean, private val database: MfExpertLmsDatabase, private val masterService: IMasters) : AbstractThreadedSyncAdapter(context, autoInitialize, allowParallelSyncs) {

    private val TAG = BankSyncAdapter::class.java.simpleName
    private val accountManager : AccountManager = AccountManager.get(context)


    override fun onPerformSync(account: Account?, bundle: Bundle?, p2: String?, contentProvider: ContentProviderClient?, syncResult: SyncResult?) {
        try {
            Constants.logD(TAG, "Banks onPerformSync for account[" + account?.name + "]")
            Constants.logD(TAG, "Banks sync started and running...")

            // Get all district from api service and store in local DB
            val syncMasters = SyncMasters()
            var messages = ArrayList<String>()

            // Sync District
            var message = syncMasters.syncBanksDetails(context.apiAccessToken, database, masterService)
            Constants.logD("Banks Sync", "Message $message")
            if (message == net.rmitsolutions.mfexpert.lms.Constants.UNAUTHORIZED){
                Globals.refreshToken(context)
                return
            }

            if (!Globals.isEmptyString(message)) {
                messages.add("Banks : $message")
            }

            if (messages.size == 0){
                Constants.logD(TAG, "Banks sync success !")
                context.putPref(SharedPrefKeys.SP_BANKS_SYNC_TIME, net.rmitsolutions.mfexpert.lms.Constants.getFormatDate())
                context.sendBroadcast(Intent(SyncSettingsViewHolder.ACTION_FINISHED_SYNC).putExtra("position", 9));
            }else{
//                NotificationHelper.notifyGroupedError(context, "Banks Sync failed", messages.size.toString() + " Modules failed to sync", messages)
            }
        }finally {
            Constants.logD(TAG, "Banks sync finally called...!")
        }
    }
}