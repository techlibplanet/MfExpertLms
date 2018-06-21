package net.rmitsolutions.mfexpert.lms.cbm

import android.os.Bundle
import com.stepstone.stepper.StepperLayout
import net.rmitsolutions.mfexpert.lms.BaseActivity
import net.rmitsolutions.mfexpert.lms.MfExpertApp
import net.rmitsolutions.mfexpert.lms.R
import net.rmitsolutions.mfexpert.lms.database.MfExpertLmsDatabase

import net.rmitsolutions.mfexpert.lms.database.entities.CBMDataEntity
import net.rmitsolutions.mfexpert.lms.dependency.components.DaggerInjectActivityComponent
import javax.inject.Inject
import android.databinding.adapters.TextViewBindingAdapter.setText
import android.widget.ImageView
import android.widget.TextView
import org.jetbrains.anko.find


class CbmActivity : BaseActivity() {


    private lateinit var cbmStepperLayout: StepperLayout
    @Inject
    lateinit var database: MfExpertLmsDatabase
    private var cbmDataEntity: CBMDataEntity? = null;
    var addIconImage: ImageView? = null
    private val CURRENT_STEP_POSITION_KEY = "position"
    lateinit var toolbar: android.support.v7.widget.Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cbm)

        val depComponent = DaggerInjectActivityComponent.builder()
                .applicationComponent(MfExpertApp.applicationComponent)
                .build()
        depComponent.injectCbmActivity(this)

        cbmDataEntity = CBMDataEntity()
        cbmStepperLayout = findViewById(R.id.cbmStepperLayout)
        toolbar = findViewById(R.id.toolbar_actionbar)
        addIconImage = findViewById(R.id.addMoreFamilyDetails)

        var adapter = CBMStepperAdapter(supportFragmentManager, this)
        adapter.cbmDataEntity = CBMDataEntity()
        //   adapter.personalDetailsDataEntity=PersonalDetailsDataEntity()
        val startingStepPosition = savedInstanceState?.getInt(CURRENT_STEP_POSITION_KEY) ?: 0
        cbmStepperLayout.setAdapter(CBMStepperAdapter(supportFragmentManager, this), startingStepPosition)
        adapter.database = database
        cbmStepperLayout.adapter = adapter
    }

    override fun getSelfNavDrawerItem() = R.id.nav_cbm_details

}

