package net.rmitsolutions.mfexpert.lms.sync.memberejection

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import net.rmitsolutions.mfexpert.lms.MfExpertApp
import net.rmitsolutions.mfexpert.lms.database.MfExpertLmsDatabase
import net.rmitsolutions.mfexpert.lms.dependency.components.DaggerInjectServiceComponent
import net.rmitsolutions.mfexpert.lms.network.IMasters
import net.rmitsolutions.mfexpert.lms.sync.banks.BankSyncAdapter
import javax.inject.Inject

class MemberRejectionSyncService : Service() {

    @Inject
    lateinit var database: MfExpertLmsDatabase

    @Inject
    lateinit var mastersService: IMasters

    override fun onCreate() {
        super.onCreate()

        val depComponent = DaggerInjectServiceComponent.builder()
                .applicationComponent(MfExpertApp.applicationComponent)
                .build()
        depComponent.injectMemberRejectionSyncService(this)

        synchronized(syncAdapterLocker) {
            if (syncAdapter == null) {
//                syncAdapter = MastersSyncAdapter(applicationContext, true, false, roomDatabase, masterService)
                syncAdapter = MemberRejectionReasonSyncAdapter(applicationContext, true, false, database, mastersService)
                Log.d("Sync", "Member Rejection Reason sync adapter created.")
            }
        }
    }


    override fun onBind(intent: Intent): IBinder? {
        return syncAdapter?.syncAdapterBinder
    }

    companion object {
        private val syncAdapterLocker = Any()
        private var syncAdapter: MemberRejectionReasonSyncAdapter? = null
    }
}