package net.rmitsolutions.mfexpert.lms.sync.district

import net.rmitsolutions.libcam.Constants
import net.rmitsolutions.mfexpert.lms.database.MfExpertLmsDatabase

class SyncDistrict {

    private val TAG = SyncDistrict::class.java.simpleName

    internal fun syncDistricts(database: MfExpertLmsDatabase): String {
        val message = ""
        Constants.logD(TAG, "Start syncing district !")
        return message
    }
}