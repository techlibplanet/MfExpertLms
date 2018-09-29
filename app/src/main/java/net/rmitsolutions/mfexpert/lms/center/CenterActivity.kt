package net.rmitsolutions.mfexpert.lms.center

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.stepstone.stepper.StepperLayout
import net.rmitsolutions.mfexpert.lms.BaseActivity
import net.rmitsolutions.mfexpert.lms.MfExpertApp
import net.rmitsolutions.mfexpert.lms.R
import net.rmitsolutions.mfexpert.lms.database.MfExpertLmsDatabase
import net.rmitsolutions.mfexpert.lms.database.entities.CenterDetailsEntity
import net.rmitsolutions.mfexpert.lms.dependency.components.DaggerInjectActivityComponent
import javax.inject.Inject

class CenterActivity : BaseActivity() {
    private lateinit var centerStepperLayout: StepperLayout
    @Inject
    lateinit var database:MfExpertLmsDatabase
    var centerDetailsEntity:CenterDetailsEntity?=null;
    private val CURRENT_STEP_POSITION_KEY = "position"
    lateinit var toolbar: androidx.appcompat.widget.Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_center)
        centerStepperLayout = findViewById(R.id.centerStepperLayout)
        toolbar=findViewById(R.id.toolbar_actionbar)
        centerDetailsEntity=CenterDetailsEntity()

        val depComponent = DaggerInjectActivityComponent.builder()
                .applicationComponent(MfExpertApp.applicationComponent)
                .build()
        depComponent.injectCenterActivity(this)
        var adapter= CenterStepperAdapter(supportFragmentManager, this)

        val startingStepPosition = savedInstanceState?.getInt(CURRENT_STEP_POSITION_KEY) ?: 0
        centerStepperLayout.setAdapter(adapter, startingStepPosition)
        //  adapter.database=database
        //centerStepperLayout?.setAdapter(adapter)
    }

    override fun getSelfNavDrawerItem() = R.id.nav_center

}
