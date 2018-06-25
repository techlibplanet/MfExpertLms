package net.rmitsolutions.mfexpert.lms.sync.literacy

import net.rmitsolutions.libcam.Constants
import net.rmitsolutions.mfexpert.lms.database.MfExpertLmsDatabase

class SyncLiteracy {

    private val TAG = SyncLiteracy::class.java.simpleName

    internal fun syncLiteracy(database: MfExpertLmsDatabase): String {
        val message = ""
        Constants.logD(TAG, "Start syncing literacy !")
        return message
    }
}