package net.rmitsolutions.mfexpert.lms

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import net.rmitsolutions.mfexpert.lms.sample.SampleActivity
import org.jetbrains.anko.startActivity

/**
 * Created by Madhu on 20-Apr-2018.
 */
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivity<SampleActivity>()
        finish()
        overridePendingTransition(0, 0)
    }
}