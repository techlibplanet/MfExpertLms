package net.rmitsolutions.mfexpert.lms.dependency.components

import dagger.Component
import net.rmitsolutions.mfexpert.lms.cbm.IdentificationInfoFragment
import net.rmitsolutions.mfexpert.lms.cbm.PresentAddressFragment
import net.rmitsolutions.mfexpert.lms.dependency.components.ApplicationComponent
import net.rmitsolutions.mfexpert.lms.dependency.scopes.ActivityScope
import net.rmitsolutions.mfexpert.lms.repayment.RepaymentDialogTabs

/**
 * Created by Madhu on 24-Apr-2018.
 */
@ActivityScope
@Component(dependencies = arrayOf(ApplicationComponent::class))
interface InjectFragmentComponent {
    fun injectPresentAddressFragment(presentAddressFragment: PresentAddressFragment)
    fun injectIdentificationInfoFragment(identificationInfoFragment: IdentificationInfoFragment)
    fun injectRepaymentDialogTabsFragment(repaymentDialogTabs: RepaymentDialogTabs)
}