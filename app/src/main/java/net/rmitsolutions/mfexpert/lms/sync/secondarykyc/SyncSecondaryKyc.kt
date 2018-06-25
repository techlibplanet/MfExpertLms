package net.rmitsolutions.mfexpert.lms.sync.secondarykyc

import net.rmitsolutions.libcam.Constants
import net.rmitsolutions.mfexpert.lms.database.MfExpertLmsDatabase

class SyncSecondaryKyc {

    private val TAG = SyncSecondaryKyc::class.java.simpleName

    internal fun syncSecondaryKyc(database: MfExpertLmsDatabase): String {
        val message = ""
        Constants.logD(TAG, "Start syncing secondary kyc !")
        return message
    }
}