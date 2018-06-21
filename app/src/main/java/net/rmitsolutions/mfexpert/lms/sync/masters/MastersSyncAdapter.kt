package net.rmitsolutions.mfexpert.lms.sync.masters

import android.accounts.Account
import android.accounts.AccountManager
import android.content.AbstractThreadedSyncAdapter
import android.content.ContentProviderClient
import android.content.Context
import android.content.SyncResult
import android.os.Bundle
import net.rmitsolutions.libcam.Constants.logD
import net.rmitsolutions.mfexpert.lms.database.MfExpertLmsDatabase
import net.rmitsolutions.mfexpert.lms.helpers.NotificationHelper
import net.rmitsolutions.mfexpert.lms.models.Globals
import java.util.ArrayList

class MastersSyncAdapter(context: Context, autoInitialize: Boolean, allowParallelSyncs: Boolean, database: MfExpertLmsDatabase) : AbstractThreadedSyncAdapter(context, autoInitialize, allowParallelSyncs) {

    private val accountManager: AccountManager
    private val database = database
    private val TAG = "MasterSyncAdapter"

    init {
        accountManager = AccountManager.get(context)
    }

    override fun onPerformSync(account: Account?, bundle: Bundle?, p2: String?, contentProvider: ContentProviderClient?, syncResult: SyncResult?) {

        try {
            logD(TAG, "Masters onPerformSync for account[" + account?.name + "]")
            logD(TAG, "Master sync started and running...")


            // Get all masters from api service and store in local DB
            val syncMasters = SyncMasters()
            var messages = ArrayList<String>()

            // Sync District
            var message = syncMasters.syncDistricts(database)
            if (!Globals.isEmptyString(message)) {
                messages.add("District : $message")
            }

            // Sync Relations
            message = syncMasters.syncRelations(database)
            if (!Globals.isEmptyString(message)) {
                messages.add("Relation : $message")
            }

            // Sync Occupation
            message = syncMasters.syncOccupation(database)
            if (!Globals.isEmptyString(message)) {
                messages.add("Occupation : $message")
            }

            // Sync Literacy
            message = syncMasters.syncLiteracy(database)
            if (!Globals.isEmptyString(message)) {
                messages.add("Literacy : $message")
            }

            // Sync PrimaryKYC
            message = syncMasters.syncPrimaryKyc(database)
            if (!Globals.isEmptyString(message)) {
                messages.add("PrimaryKYC : $message")
            }

            // Sync SecondaryKYC
            message = syncMasters.syncSecondaryKyc(database)
            if (!Globals.isEmptyString(message)) {
                messages.add("SecondaryKYC : $message")
            }

            // Sync Loan Purpose
            message = syncMasters.syncLoanPurpose(database)
            if (!Globals.isEmptyString(message)) {
                messages.add("LoanPurpose : $message")
            }

            if (messages.size == 0){
                logD(TAG, "Master sync success !")
            }else{
                NotificationHelper.notifyGroupedError(context, "Masters Sync failed", messages.size.toString() + " Modules failed to sync", messages)
            }
        } finally {
            logD(TAG, "Finally called")
        }


    }
}