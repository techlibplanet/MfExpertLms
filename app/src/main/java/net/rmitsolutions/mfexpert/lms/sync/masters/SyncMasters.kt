package net.rmitsolutions.mfexpert.lms.sync.masters

import net.rmitsolutions.libcam.Constants.logD
import net.rmitsolutions.mfexpert.lms.database.MfExpertLmsDatabase

class SyncMasters {

    private val TAG = SyncMasters::class.java.simpleName

    internal fun syncDistricts(database: MfExpertLmsDatabase): String {
        val message = ""
        logD(TAG,"Start syncing district !")
        return message
    }

    internal fun syncRelations(database: MfExpertLmsDatabase): String {
        val message = ""
        logD(TAG,"Start syncing relations !")
        return message
    }

    internal fun syncOccupation(database: MfExpertLmsDatabase): String {
        val message = ""
        logD(TAG,"Start syncing occupations !")
        return message
    }

    internal fun syncLiteracy(database: MfExpertLmsDatabase): String {
        val message = ""
        logD(TAG,"Start syncing literacy !")
        return message
    }

    internal fun syncPrimaryKyc(database: MfExpertLmsDatabase): String {
        val message = ""
        logD(TAG,"Start syncing primary kyc !")
        return message
    }

    internal fun syncSecondaryKyc(database: MfExpertLmsDatabase): String {
        val message = ""
        logD(TAG,"Start syncing secondary kyc !")
        return message
    }

    internal fun syncLoanPurpose(database: MfExpertLmsDatabase): String {
        val message = ""
        logD(TAG,"Start syncing loan purpose !")
        return message
    }
}