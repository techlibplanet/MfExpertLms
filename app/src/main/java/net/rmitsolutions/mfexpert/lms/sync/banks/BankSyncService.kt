package net.rmitsolutions.mfexpert.lms.sync.banks

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import net.rmitsolutions.mfexpert.lms.MfExpertApp
import net.rmitsolutions.mfexpert.lms.database.MfExpertLmsDatabase
import net.rmitsolutions.mfexpert.lms.dependency.components.DaggerInjectServiceComponent
import net.rmitsolutions.mfexpert.lms.network.IMasters
import net.rmitsolutions.mfexpert.lms.sync.district.DistrictSyncAdapter
import javax.inject.Inject

class BankSyncService : Service() {

    @Inject
    lateinit var database: MfExpertLmsDatabase

    @Inject
    lateinit var mastersService: IMasters

    override fun onCreate() {
        super.onCreate()

        val depComponent = DaggerInjectServiceComponent.builder()
                .applicationComponent(MfExpertApp.applicationComponent)
                .build()
        depComponent.injectBankSyncService(this)

        synchronized(syncAdapterLocker) {
            if (syncAdapter == null) {
//                syncAdapter = MastersSyncAdapter(applicationContext, true, false, roomDatabase, masterService)
                syncAdapter = BankSyncAdapter(applicationContext, true, false, database, mastersService)
                Log.d("Sync", "Banks sync adapter created.")
            }
        }
    }


    override fun onBind(intent: Intent): IBinder? {
        return syncAdapter?.syncAdapterBinder
    }

    companion object {
        private val syncAdapterLocker = Any()
        private var syncAdapter: BankSyncAdapter? = null
    }
}