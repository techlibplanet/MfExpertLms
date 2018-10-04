package net.rmitsolutions.mfexpert.lms.dependency.components

import dagger.Component
import net.rmitsolutions.mfexpert.lms.BaseActivity
import net.rmitsolutions.mfexpert.lms.WelcomeActivity
import net.rmitsolutions.mfexpert.lms.group.GroupActivity
import net.rmitsolutions.mfexpert.lms.village.VillageActivity
import net.rmitsolutions.mfexpert.lms.cbm.CbmActivity
import net.rmitsolutions.mfexpert.lms.center.CenterActivity
import net.rmitsolutions.mfexpert.lms.dashboard.DashboardActivity
import net.rmitsolutions.mfexpert.lms.dependency.scopes.ActivityScope
import net.rmitsolutions.mfexpert.lms.loanUtilizationCheck.LoanUtilizationActivity
import net.rmitsolutions.mfexpert.lms.repayment.RepaymentActivity
import net.rmitsolutions.mfexpert.lms.repayment.adapter.ClientViewHolder

import net.rmitsolutions.mfexpert.lms.sample.SampleActivity

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
    fun injectLoanUtilizationCheckActivity(activity: LoanUtilizationActivity)
    fun injectGroupActivity(activity: GroupActivity)
    fun injectCenterActivity(activity: CenterActivity)
    fun injectSampleActivity(activity: SampleActivity)
    fun injectWelcomeActivity(activity: WelcomeActivity)
    fun injectLoanUtilizationActivity(activity: LoanUtilizationActivity)
    fun injectRepaymentActivity(activity: RepaymentActivity)
    fun injectClientHolder(holder : ClientViewHolder)
}