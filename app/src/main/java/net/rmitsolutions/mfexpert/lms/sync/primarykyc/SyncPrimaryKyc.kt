package net.rmitsolutions.mfexpert.lms.sync.primarykyc

import net.rmitsolutions.libcam.Constants
import net.rmitsolutions.mfexpert.lms.database.MfExpertLmsDatabase

class SyncPrimaryKyc {

    private val TAG = SyncPrimaryKyc::class.java.simpleName

    internal fun syncPrimaryKyc(database: MfExpertLmsDatabase): String {
        val message = ""
        Constants.logD(TAG, "Start syncing primary kyc !")
        return message
    }
}