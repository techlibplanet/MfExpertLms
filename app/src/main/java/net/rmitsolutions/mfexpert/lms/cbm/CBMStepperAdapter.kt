package net.rmitsolutions.mfexpert.lms.cbm

import  android.content.Context

import android.support.v4.app.FragmentManager
import android.view.View

import com.stepstone.stepper.Step
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter
import net.rmitsolutions.mfexpert.lms.R
import net.rmitsolutions.mfexpert.lms.database.MfExpertLmsDatabase
import net.rmitsolutions.mfexpert.lms.database.entities.CBMDataEntity
import com.stepstone.stepper.viewmodel.StepViewModel



class CBMStepperAdapter(fm: FragmentManager, context: Context) : AbstractFragmentStepAdapter(fm, context) {
    var cbmDataEntity:CBMDataEntity?=null
    var database: MfExpertLmsDatabase?=null
    private var cbmActivity:CbmActivity?=null;
    private var permanentAddressFragment:PermanentAddressFragment?=null

    override fun createStep(position: Int): Step {
        cbmActivity=(context as? CbmActivity)
        when (position) {
            0 -> return PersonalInfoFragment.newInstance(R.layout.personal_info_fragment,cbmDataEntity)
            1 -> return PresentAddressFragment.newInstance(R.layout.present_address_fragment,cbmDataEntity, database)
            2 ->   {
                permanentAddressFragment=PermanentAddressFragment.newInstance(R.layout.permanent_address_fragment,cbmDataEntity, database)
                return permanentAddressFragment!!
            }
            3 ->  return FamilyDetailsFragment.newInstance(R.layout.family_details_fragment,cbmDataEntity, database)
            4 ->  return IdentificationInfoFragment.newInstance(R.layout.identification_info_fragment,cbmDataEntity,database)

            else -> throw IllegalArgumentException("Unsupported position: $position") as Throwable
        }
    }

    override fun getCount(): Int {
        return 5
    }


    override fun getViewModel( position: Int): StepViewModel {
        val builder = StepViewModel.Builder(context)
                .setTitle(R.string.app_name)
        when (position) {
            0 -> {
                cbmActivity?.title = "Personal info"
                cbmActivity?.addIconImage!!.visibility = View.INVISIBLE
                builder.setEndButtonLabel("Next")
            }
            1 ->{
                cbmActivity?.title = "Present Address"
                cbmActivity?.addIconImage!!.visibility = View.INVISIBLE
                builder
                        .setEndButtonLabel("Next")
                        .setBackButtonLabel("back")
            }

            2 -> {
                cbmActivity?.title = "Permanent Address"
                cbmActivity?.addIconImage!!.visibility = View.INVISIBLE
                permanentAddressFragment?.handlePresentAndPermanentEvalution()
                builder
                        .setEndButtonLabel("Next")
                        .setBackButtonLabel("back")
            }

            3 ->{
                cbmActivity?.title = "Family Details"
                cbmActivity?.addIconImage!!.visibility = View.VISIBLE
                builder
                        .setEndButtonLabel("Next")
                        .setBackButtonLabel("back")
            }

            4 -> {
                cbmActivity?.title = "Identification Info"
                cbmActivity?.addIconImage!!.visibility = View.INVISIBLE
                builder
                        .setEndButtonLabel("")
                        .setBackButtonLabel("back")
            }
            else -> throw IllegalArgumentException("Unsupported position: $position")
        }
        return builder.create()
    }
}


