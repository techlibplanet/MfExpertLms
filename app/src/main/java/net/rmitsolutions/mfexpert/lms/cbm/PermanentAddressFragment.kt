package net.rmitsolutions.mfexpert.lms.cbm

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import com.stepstone.stepper.Step
import com.stepstone.stepper.VerificationError
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner
import kotlinx.android.synthetic.main.permanent_address_fragment.*
import kotlinx.android.synthetic.main.present_address_fragment.*
import net.rmitsolutions.mfexpert.lms.R
import net.rmitsolutions.mfexpert.lms.R.id.*
import net.rmitsolutions.mfexpert.lms.database.MfExpertLmsDatabase
import net.rmitsolutions.mfexpert.lms.database.entities.CBMDataEntity
import net.rmitsolutions.mfexpert.lms.databinding.PermanentAddressFragmentBinding
import net.rmitsolutions.mfexpert.lms.viewmodels.AddressModel

class PermanentAddressFragment : Fragment(), Step {

    lateinit var dataBinding: PermanentAddressFragmentBinding
    private lateinit var permanentAddressModel: AddressModel
    var cbmDataEntity: CBMDataEntity? = null
    private lateinit var checkPermanentAddress: CheckBox
    var database : MfExpertLmsDatabase? = null
    private lateinit var districtListSpinner: MaterialBetterSpinner

    companion object {

        private const val LAYOUT_RESOURCE_ID_ARG_KEY = "messageResourceId"

        fun newInstance(@LayoutRes layoutResId: Int, cbmDataEntity: CBMDataEntity?, database: MfExpertLmsDatabase?): PermanentAddressFragment {
            val args = Bundle()
            args.putInt(LAYOUT_RESOURCE_ID_ARG_KEY, layoutResId)
            val fragment = PermanentAddressFragment()
            fragment.arguments = args
            fragment.cbmDataEntity = cbmDataEntity;
            fragment.database = database
            return fragment
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        dataBinding = DataBindingUtil.inflate(inflater, R.layout.permanent_address_fragment, container, false)
        val v = dataBinding.root

        districtListSpinner = v.findViewById(R.id.chooseDistrictPermanent)
        val villageInfoAdapter = ArrayAdapter<String>(activity, android.R.layout.simple_dropdown_item_1line,getDistricts())
        districtListSpinner.setAdapter<ArrayAdapter<String>>(villageInfoAdapter)

        if (cbmDataEntity?.permanentAddresInfo == null) {
            cbmDataEntity?.permanentAddresInfo = AddressModel("", "", "", "", "", "", "")

            cbmDataEntity?.permanentAddresInfo?.addressType = AddressModel.PERMANENT_ADDRESS
            permanentAddressModel = cbmDataEntity?.permanentAddresInfo!!
        }
        dataBinding.permanentAddressInfoVm = cbmDataEntity?.permanentAddresInfo

        checkPermanentAddress = v.findViewById(R.id.checkPresentAddress)

        checkPermanentAddress.setOnCheckedChangeListener { buttonView, isChecked ->
            val presentAddr = cbmDataEntity?.presentAddresInfo
            val permanentAddr = cbmDataEntity?.permanentAddresInfo
            if (isChecked) {
                permanentAddr?.copyData(presentAddr!!)
                permanentAddr?.permanentAddressSameAsPresent = true;
                dataBinding.permanentAddressInfoVm = permanentAddr
            } else {
                permanentAddr?.permanentAddressSameAsPresent = false;
                dataBinding.permanentAddressInfoVm = permanentAddr
            }
        }
        return v
    }

    private fun getDistricts(): List<String> {
        return database?.districtDao()?.getDistrictNames()!!
    }

    fun handlePresentAndPermanentEvalution() {
        val permanentAddr = cbmDataEntity?.permanentAddresInfo
        val presentAddr = cbmDataEntity?.presentAddresInfo
        if (permanentAddr?.permanentAddressSameAsPresent == true) {
            permanentAddr.copyData(presentAddr!!)
            dataBinding.permanentAddressInfoVm = permanentAddr
        }
    }

    override fun verifyStep(): VerificationError? {
         if (!validate()) {
             return VerificationError("")
         }
        return null
    }

    override fun onSelected() {
        //update UI when selected
    }

    override fun onError(error: VerificationError) {
        //handle error inside of the fragment, e.g. show error on EditText
    }

    private fun validate(): Boolean {
        if (permanentAddressModel.houseNo.isBlank()) {
            lblHouseNoPermanent.error = "House No is required."
            return false
        } else {
            lblHouseNoPermanent.isErrorEnabled = false
        }

        if (permanentAddressModel.street.isBlank()) {
            lblStreetPermanent.error = "Street name is required."
            return false
        } else {
            lblStreetPermanent.isErrorEnabled = false
        }

        if (permanentAddressModel.mandal.isBlank()) {
            lblMandalPermanent.error = "Mandal name is required."
            return false
        } else {
            lblMandalPermanent.isErrorEnabled = false
        }

        if (permanentAddressModel.city.isBlank()) {
            lblCityPermanent.error = "City name is required."
            return false
        } else {
            lblCityPermanent.isErrorEnabled = false
        }

        if (permanentAddressModel.phone.isBlank()) {
            lblPhonePermanent.error = "Phone No required."
            return false
        } else {
            lblPhonePermanent.isErrorEnabled = false
        }
//        if (permanentAddressModel.district.isBlank()) {
//            lblDistrictPermanent.error = "District is required."
//            return false
//        } else {
//            lblDistrictPermanent.isErrorEnabled = false
//        }
        if (permanentAddressModel.district.isBlank()) {
            chooseDistrictPermanent.error = "District is required."
            return false
        } else {
            chooseDistrictPermanent.error = null
        }

        if (permanentAddressModel.pincode.isBlank()) {
            lblPincodePermanent.error = "Pin code as on is required."
            return false
        } else {
            lblPincodePermanent.isErrorEnabled = false
        }
        return true
    }
}
