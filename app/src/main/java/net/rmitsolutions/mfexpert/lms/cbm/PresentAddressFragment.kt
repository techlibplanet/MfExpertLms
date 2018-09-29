package net.rmitsolutions.mfexpert.lms.cbm


import androidx.databinding.DataBindingUtil
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.stepstone.stepper.Step
import com.stepstone.stepper.VerificationError
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner
import net.rmitsolutions.mfexpert.lms.R
import kotlinx.android.synthetic.main.present_address_fragment.*
import net.rmitsolutions.mfexpert.lms.Constants
import net.rmitsolutions.mfexpert.lms.database.MfExpertLmsDatabase
import net.rmitsolutions.mfexpert.lms.database.entities.CBMDataEntity
import net.rmitsolutions.mfexpert.lms.database.entities.District
import net.rmitsolutions.mfexpert.lms.databinding.PresentAddressFragmentBinding
import net.rmitsolutions.mfexpert.lms.helpers.logD
import net.rmitsolutions.mfexpert.lms.viewmodels.AddressModel
import java.util.regex.Pattern


class PresentAddressFragment : Fragment(), Step {
    lateinit var toolbar: Toolbar
    lateinit var dataBinding: PresentAddressFragmentBinding
    private lateinit var presentAddressModel: AddressModel
    var cbmDataEntity: CBMDataEntity? = null
    var database: MfExpertLmsDatabase? = null
    private lateinit var districtListSpinner: MaterialBetterSpinner
    private var position :Int = 0

    companion object {

        private const val LAYOUT_RESOURCE_ID_ARG_KEY = "messageResourceId"

        fun newInstance(@LayoutRes layoutResId: Int, cbmDataEntity: CBMDataEntity?, database: MfExpertLmsDatabase?): PresentAddressFragment {
            val args = Bundle()
            args.putInt(LAYOUT_RESOURCE_ID_ARG_KEY, layoutResId)
            val fragment = PresentAddressFragment()
            fragment.arguments = args
            fragment.cbmDataEntity = cbmDataEntity
            fragment.database = database
            return fragment
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        dataBinding = DataBindingUtil.inflate(inflater, R.layout.present_address_fragment, container, false)
        val v = dataBinding.root
        presentAddressModel = AddressModel("", "", "", "", "", "", "",0)

        districtListSpinner = v.findViewById(R.id.chooseDistrictPresent)
        val villageInfoAdapter = ArrayAdapter<String>(activity, android.R.layout.simple_dropdown_item_1line, getDistricts())
        districtListSpinner.setAdapter<ArrayAdapter<String>>(villageInfoAdapter)

        //        genderListSpinner.setOnItemClickListener{
//            adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->
//           if (){
//               dataBinding.personalInfoVm?.gender = "0"
//           }else if(i == 1){
//               dataBinding.personalInfoVm?.gender = "1"
//           }
//
//        }

        districtListSpinner.setOnItemClickListener { adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->
            logD("Position CLicked - $i")
            position = i
            val list = getDistrictIds()
            cbmDataEntity?.presentAddresInfo?.districtId = list[i].id.toInt()
            logD("Id - ${list[i].id} District Name - ${list[i].name}")

        }

        if (cbmDataEntity?.presentAddresInfo != null) {
            presentAddressModel = cbmDataEntity?.presentAddresInfo!!
        } else {
            cbmDataEntity?.presentAddresInfo = presentAddressModel
        }
        dataBinding.presentAddressInfoVm = presentAddressModel
        return v
    }

    private fun getDistricts(): List<String> {
        return database?.districtDao()?.getDistrictNames()!!
    }

    private fun getDistrictIds(): List<District> {
        return database?.districtDao()?.getAllDistrict()!!
    }

    override fun onSelected() {
    }

    override fun verifyStep(): VerificationError? {
      /*  if (!validate()) {
            return VerificationError("")
        }*/
        return null
    }

    override fun onError(error: VerificationError) {

    }

    private fun validate(): Boolean {
        if (presentAddressModel.houseNo.isBlank()) {
            lblHouseNo.error = "House No is required."
            return false
        } else {
            lblHouseNo.isErrorEnabled = false
        }

        if (presentAddressModel.street.isBlank()) {
            lblStreet.error = "Street name is required."
            return false
        } else {
            lblStreet.isErrorEnabled = false
        }

        if (presentAddressModel.mandal.isBlank()) {
            lblMandal.error = "Mandal name is required."
            return false
        } else {
            lblMandal.isErrorEnabled = false
        }

        if (presentAddressModel.city.isBlank()) {
            lblCity.error = "City name is required."
            return false
        } else {
            lblCity.isErrorEnabled = false
        }

        if (presentAddressModel.phone.isBlank()) {
            lblPhone.error = "Phone No required."
            return false
        } else {
            lblPhone.isErrorEnabled = false
        }

        if (isValidPhone(presentAddressModel.phone)) {
            lblPhone.error = "Enter valid phone number"
            return false
        } else {
            lblPhone.isErrorEnabled = false
        }


//        if (presentAddressModel.district.isBlank()) {
//            lblDistrict.error = "District is required."
//            return false
//        } else {
//            lblDistrict.isErrorEnabled = false
//        }

        if (presentAddressModel.district.isBlank()) {
            chooseDistrictPresent.error = "District is required."
            return false
        } else {
            chooseDistrictPresent.error = null
        }



        if (presentAddressModel.pincode.isBlank()) {
            lblPincode.error = "Pin code as on is required."
            return false
        } else {
            lblPincode.isErrorEnabled = false
        }

        if (!Constants.zipCodeValidate(presentAddressModel.pincode)){
            lblPincode.error = "Enter valid Pin Code."
            return false
        } else {
            lblPincode.isErrorEnabled = false
        }

        if (position == 0){
            chooseDistrictPresent.error = "District is required."
            return false
        } else {
            chooseDistrictPresent.error = null
        }
        return true
    }

    private fun isValidPhone(phone: String): Boolean {
        var check = false
        check = if (!Pattern.matches("[a-zA-Z]+", phone)) {
            !(phone.length == 10)
        } else {
            false
        }
        return check
    }

}
