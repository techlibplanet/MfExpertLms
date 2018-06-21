package net.rmitsolutions.mfexpert.lms.sync.masters

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import net.rmitsolutions.mfexpert.lms.MfExpertApp
import net.rmitsolutions.mfexpert.lms.database.MfExpertLmsDatabase
import net.rmitsolutions.mfexpert.lms.dependency.components.DaggerApplicationComponent
import net.rmitsolutions.mfexpert.lms.dependency.components.DaggerInjectActivityComponent
import javax.inject.Inject

class MastersSyncService : Service() {

    @Inject
    lateinit var database: MfExpertLmsDatabase
//    @Inject
//    lateinit var masterService: IMasters

    override fun onCreate() {
        super.onCreate()

        val depComponent = DaggerInjectActivityComponent.builder()
                .applicationComponent(MfExpertApp.applicationComponent)
                .build()
        depComponent.injectMasterSyncService(this)

        synchronized(syncAdapterLocker) {
            if (syncAdapter == null) {
//                syncAdapter = MastersSyncAdapter(applicationContext, true, false, roomDatabase, masterService)
                syncAdapter = MastersSyncAdapter(applicationContext, true, false, database)
                Log.d("sync", "Master sync adapter created.")
            }
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return syncAdapter?.syncAdapterBinder
    }

    companion object {
        private val syncAdapterLocker = Any()
        private var syncAdapter: MastersSyncAdapter? = null
    }
}