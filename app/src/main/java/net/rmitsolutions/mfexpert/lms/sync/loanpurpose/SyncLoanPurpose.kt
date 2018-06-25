package net.rmitsolutions.mfexpert.lms.sync.loanpurpose

import net.rmitsolutions.libcam.Constants
import net.rmitsolutions.mfexpert.lms.database.MfExpertLmsDatabase

class SyncLoanPurpose {

    private val TAG = SyncLoanPurpose::class.java.simpleName

    internal fun syncLoanPurpose(database: MfExpertLmsDatabase): String {
        val message = ""
        Constants.logD(TAG, "Start syncing loan purpose !")
        return message
    }
}