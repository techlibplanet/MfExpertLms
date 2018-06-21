package net.rmitsolutions.mfexpert.lms.dependency.components

import dagger.Component
import net.rmitsolutions.mfexpert.lms.BaseActivity
import net.rmitsolutions.mfexpert.lms.group.GroupActivity
import net.rmitsolutions.mfexpert.lms.village.VillageActivity
import net.rmitsolutions.mfexpert.lms.cbm.CbmActivity
import net.rmitsolutions.mfexpert.lms.center.CenterActivity
import net.rmitsolutions.mfexpert.lms.dashboard.DashboardActivity
import net.rmitsolutions.mfexpert.lms.dependency.scopes.ActivityScope
import net.rmitsolutions.mfexpert.lms.sample.SampleActivity
import net.rmitsolutions.mfexpert.lms.sync.masters.MastersSyncService

/**
 * Created by Madhu on 24-Apr-2018.
 */
@ActivityScope
@Component(dependencies = arrayOf(ApplicationComponent::class))
interface InjectActivityComponent {
    fun injectBaseActivity(baseActivity: BaseActivity)
    fun injectDashboardActivity(activity: DashboardActivity)
    fun injectCbmActivity(activity: CbmActivity)
    fun injectVillageActivity(activity: VillageActivity)
    fun injectGroupActivity(activity: GroupActivity)
    fun injectCenterActivity(activity: CenterActivity)
    fun injectSampleActivity(activity: SampleActivity)

    // Inject Master Sync service
    fun injectMasterSyncService(mastersSyncService: MastersSyncService)

}