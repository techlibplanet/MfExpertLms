package net.rmitsolutions.mfexpert.lms.settings

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import net.rmitsolutions.mfexpert.lms.BaseActivity
import net.rmitsolutions.mfexpert.lms.R
import net.rmitsolutions.mfexpert.lms.settings.adapter.adaptersettings.SettingsMenuFragment
import net.rmitsolutions.mfexpert.lms.settings.adapter.adaptersyncsettings.SyncSettingsFragment


class SettingsActivity : BaseActivity(), SettingsMenuFragment.OnFragmentInteractionListener, SyncSettingsFragment.OnFragmentInteractionListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val settingMenuFragment = SettingsMenuFragment()
        switchToFragment(settingMenuFragment)

    }

    override fun getSelfNavDrawerItem() = R.id.nav_dashboard


    // Switch UI to the given fragment
    private fun switchToFragment(newFrag: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, newFrag)
                .commit()
    }

    override fun onFragmentInteraction(uri: Uri) {

    }



}
