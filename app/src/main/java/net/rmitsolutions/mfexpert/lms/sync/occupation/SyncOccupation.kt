package net.rmitsolutions.mfexpert.lms.sync.occupation

import net.rmitsolutions.libcam.Constants
import net.rmitsolutions.mfexpert.lms.database.MfExpertLmsDatabase

class SyncOccupation {

    private val TAG = SyncOccupation::class.java.simpleName

    internal fun syncOccupation(database: MfExpertLmsDatabase): String {
        val message = ""
        Constants.logD(TAG, "Start syncing occupations !")
        return message
    }
}