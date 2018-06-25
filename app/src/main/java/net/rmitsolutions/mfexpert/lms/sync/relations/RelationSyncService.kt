package net.rmitsolutions.mfexpert.lms.sync.relations

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import net.rmitsolutions.mfexpert.lms.MfExpertApp
import net.rmitsolutions.mfexpert.lms.database.MfExpertLmsDatabase
import net.rmitsolutions.mfexpert.lms.dependency.components.DaggerInjectServiceComponent
import net.rmitsolutions.mfexpert.lms.sync.district.DistrictSyncAdapter
import net.rmitsolutions.mfexpert.lms.sync.district.DistrictSyncService
import javax.inject.Inject

class RelationSyncService : Service() {

    @Inject
    lateinit var database: MfExpertLmsDatabase

    override fun onCreate() {
        super.onCreate()

        val depComponent = DaggerInjectServiceComponent.builder()
                .applicationComponent(MfExpertApp.applicationComponent)
                .build()
        depComponent.injectRelationSyncService(this)

        synchronized(syncAdapterLocker) {
            if (syncAdapter == null) {
//                syncAdapter = MastersSyncAdapter(applicationContext, true, false, roomDatabase, masterService)
                syncAdapter = DistrictSyncAdapter(applicationContext, true, false, database)
                Log.d("Sync", "Relation sync adapter created.")
            }
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return syncAdapter?.syncAdapterBinder
    }

    companion object {
        private val syncAdapterLocker = Any()
        private var syncAdapter: DistrictSyncAdapter? = null
    }
}