package net.rmitsolutions.mfexpert.lms.about

import android.os.Bundle
import net.rmitsolutions.mfexpert.lms.BaseActivity
import net.rmitsolutions.mfexpert.lms.R

class AboutActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
    }

    override fun getSelfNavDrawerItem() = R.id.nav_about
}
