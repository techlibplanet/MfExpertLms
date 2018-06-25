package net.rmitsolutions.mfexpert.lms.sync.relations

import net.rmitsolutions.libcam.Constants
import net.rmitsolutions.mfexpert.lms.database.MfExpertLmsDatabase

class SyncRelations {

    private val TAG = SyncRelations::class.java.simpleName

    internal fun syncRelations(database: MfExpertLmsDatabase): String {
        val message = ""
        Constants.logD(TAG, "Start syncing relations !")
        return message
    }
}