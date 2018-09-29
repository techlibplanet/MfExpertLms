package net.rmitsolutions.mfexpert.lms.center

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.stepstone.stepper.Step
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter
import com.stepstone.stepper.viewmodel.StepViewModel
import net.rmitsolutions.mfexpert.lms.R

class CenterStepperAdapter (fm: FragmentManager, context: Context) : AbstractFragmentStepAdapter(fm, context) {

    private var centerActivity: CenterActivity?=null;
    override fun createStep(position: Int): Step {
        centerActivity=(context as? CenterActivity)
        return when (position) {
            0 -> PrimaryInformationFragment.newInstance(R.layout.fragment_primary_information, centerActivity?.centerDetailsEntity,centerActivity?.database)
            1 -> AddressInformationFragment.newInstance(R.layout.fragment_address_information, centerActivity?.centerDetailsEntity, centerActivity?.database)
            else -> throw IllegalArgumentException("Unsupported position: $position") as Throwable
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getViewModel( position: Int): StepViewModel {
        val builder = StepViewModel.Builder(context).setTitle(R.string.app_name)
        when (position) {
            0 -> {
                centerActivity?.title = "Center Information"
                builder.setEndButtonLabel("Next")
            }
            1 ->{
                centerActivity?.title = "Center Information"
                builder
                        .setBackButtonLabel("back")
                        .setEndButtonLabel("")
            }
            else -> throw IllegalArgumentException("Unsupported position: $position")
        }
        return builder.create()
    }
}