package net.rmitsolutions.mfexpert.lms.cbm


import android.os.Bundle
import androidx.annotation.LayoutRes
import com.google.android.material.textfield.TextInputEditText
import androidx.fragment.app.Fragment
import android.widget.*
import com.stepstone.stepper.Step
import com.stepstone.stepper.VerificationError
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner
import net.rmitsolutions.mfexpert.lms.R
import net.rmitsolutions.mfexpert.lms.database.entities.CBMDataEntity
import net.rmitsolutions.mfexpert.lms.databinding.PersonalInfoFragmentBinding

import net.rmitsolutions.mfexpert.lms.viewmodels.PersonalInfoModel
import java.util.*
import android.app.DatePickerDialog
import androidx.databinding.DataBindingUtil
import androidx.annotation.RequiresApi
import android.text.Editable
import android.text.TextWatcher

import kotlinx.android.synthetic.main.personal_info_fragment.*
import java.util.regex.Pattern
import android.view.*
import net.rmitsolutions.mfexpert.lms.Constants
import net.rmitsolutions.mfexpert.lms.helpers.logD
import net.rmitsolutions.mfexpert.lms.models.Globals


class PersonalInfoFragment : Fragment(), Step {

    private lateinit var genderListSpinner: MaterialBetterSpinner
    private lateinit var maritalStatusSpinner: MaterialBetterSpinner
    private lateinit var personalInfoModel: PersonalInfoModel
    lateinit var dataBinding: PersonalInfoFragmentBinding
    var cbmDataEntity: CBMDataEntity? = null
    private lateinit var dobEditText: TextInputEditText
    private lateinit var ageAsOnEditText: TextInputEditText
    private lateinit var ageEditText: TextInputEditText
    private lateinit var mobileEditText: TextInputEditText
    private lateinit var calender: Calendar

    lateinit var dialog: DatePickerDialog
    private var year: Int = 0
    private var month: Int = 0
    private var day: Int = 0
    lateinit var title: String
    private var dobAge: Boolean = false


    private var genderList = arrayOf("Female", "Male")
    private var marriageStatusList = arrayOf("Married", "UnMarried")

    companion object {
        private const val LAYOUT_RESOURCE_ID_ARG_KEY = "messageResourceId"

        fun newInstance(@LayoutRes layoutResId: Int, cbmDataEntity: CBMDataEntity?): PersonalInfoFragment {
            val args = Bundle()
            args.putInt(LAYOUT_RESOURCE_ID_ARG_KEY, layoutResId)
            val fragment = PersonalInfoFragment()
            fragment.arguments = args
            fragment.cbmDataEntity = cbmDataEntity
            return fragment
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.personal_info_fragment, container, false)
        val v = dataBinding.root
        dobEditText = v.findViewById(R.id.txtDob)
        ageEditText = v.findViewById(R.id.txtAge)
        ageAsOnEditText = v.findViewById(R.id.txtAgeAsOn)
        mobileEditText = v.findViewById(R.id.txtMobileNumber)

        dobEditText.setOnClickListener {
            calender = Calendar.getInstance()
            year = calender.get(Calendar.YEAR)
            month = calender.get(Calendar.MONTH)
            day = calender.get(Calendar.DAY_OF_MONTH)
            dialog = DatePickerDialog(activity, mDateSetListener, year, month, day)
            dialog.show()
        }

        ageAsOnEditText.setOnClickListener {
            calender = Calendar.getInstance()
            year = calender.get(Calendar.YEAR)
            month = calender.get(Calendar.MONTH)
            day = calender.get(Calendar.DAY_OF_MONTH)
            dialog = DatePickerDialog(activity, mDateSetListenerAge, year, month, day)
            dialog.show()

        }

        personalInfoModel = PersonalInfoModel("", "", "", "",
                "", "", "", "", "", "")
        if (cbmDataEntity?.personalInfoModel != null) {
            personalInfoModel = cbmDataEntity?.personalInfoModel!!
        } else {
            cbmDataEntity?.personalInfoModel = personalInfoModel
        }
        dataBinding.personalInfoVm = personalInfoModel

        val genderListAdapter = ArrayAdapter<String>(activity,
                android.R.layout.simple_dropdown_item_1line, genderList)
        genderListSpinner = v.findViewById(R.id.chooseGender)
        genderListSpinner.setAdapter<ArrayAdapter<String>>(genderListAdapter)


        ageEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                val age = text.toString()
                val date = 1
                if (age != "") {
                    val calendar = Calendar.getInstance()
                    calendar.add(Calendar.YEAR, -age.toInt())
                    calendar.set(Calendar.DATE, date)
                    calendar.set(Calendar.MONTH, Calendar.JULY)
                    val date = Constants.getDate(calendar.time)
                    logD("Year - $date")
                    dobEditText.setText(date)
                }
            }

        })

//        genderListSpinner.setOnItemClickListener{
//            adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->
//           if (){
//               dataBinding.personalInfoVm?.gender = "0"
//           }else if(i == 1){
//               dataBinding.personalInfoVm?.gender = "1"
//           }
//
//        }


        val maritalStatusListAdapter = ArrayAdapter<String>(activity,
                android.R.layout.simple_dropdown_item_1line, marriageStatusList)
        maritalStatusSpinner = v.findViewById(R.id.maritalStatus)
        maritalStatusSpinner.setAdapter<ArrayAdapter<String>>(maritalStatusListAdapter)

        return v
    }

    override fun onError(error: VerificationError) {

    }

    override fun verifyStep(): VerificationError? {
        /* if (!validate()) {
             return VerificationError("")
         }*/
        return null
    }

    override fun onSelected() {

    }

    private val mDateSetListener = DatePickerDialog.OnDateSetListener { view, y, m, d ->
        year = y
        month = m + 1
        day = d
        val date = StringBuilder().append(year).append("-").append(month).append("-").append(day).append("").toString()
        dobEditText.setText(date)
        logD("Date - $date")
        getAge(year, month, day)
    }

    private val mDateSetListenerAge = DatePickerDialog.OnDateSetListener { view, y, m, d ->
        year = y
        month = m + 1
        day = d
        val date = StringBuilder().append(year).append("-").append(month).append("-").append(day).append("").toString()
        ageAsOnEditText.setText(date)
    }


    private fun validate(): Boolean {
        if (personalInfoModel.firstName.isBlank()) {
            lblFirstName.error = "First name is required."
            return false
        } else {
            lblFirstName.isErrorEnabled = false
        }

        if (personalInfoModel.firstName.length < 3) {
            lblFirstName.error = "Enter valid name."
            return false
        } else {
            lblFirstName.isErrorEnabled = false
        }


        if (personalInfoModel.surName.isBlank()) {
            lblSurname.error = "Surname name is required."
            return false
        } else {
            lblSurname.isErrorEnabled = false
        }

        if (personalInfoModel.nameAsPerKYC.isBlank()) {
            lblKycName.error = "KYC name is required."
            return false
        } else {
            lblKycName.isErrorEnabled = false
        }

        if (personalInfoModel.gender.isBlank()) {
            chooseGender.error = "Gender is required"
            return false
        } else {
            chooseGender.error = null
        }
        if (personalInfoModel.maritalStatus.isBlank()) {
            maritalStatus.error = "Marital Status is required"
            return false
        } else {
            maritalStatus.error = null
        }

        if (personalInfoModel.dob.isBlank()) {
            lblDob.error = "DOB is required."
            return false
        } else {
            lblDob.isErrorEnabled = false
        }
        if (personalInfoModel.age.isBlank()) {
            lblAge.error = "Age is required."
            return false
        } else {
            lblAge.isErrorEnabled = false
        }

        if (personalInfoModel.age.toInt() < 18) {
            lblAge.error = "Age must be greater than 18."
            return false
        } else {
            lblAge.isErrorEnabled = false
        }

        if (personalInfoModel.ageAsOn.isBlank()) {
            lblAgeAsOn.error = "Age as on is required."
            return false
        } else {
            lblAgeAsOn.isErrorEnabled = false
        }
        if (personalInfoModel.mobile.isBlank()) {
            lblMobileNumber.error = "Mobile Number is required."
            return false
        } else {
            lblMobileNumber.isErrorEnabled = false
        }

        if (Globals.isValidPhone(mobileEditText.text.toString())) {
            lblMobileNumber.isErrorEnabled = false
        } else {
            lblMobileNumber.error = "Please Enter Valid Mobile Number."
            return false
        }

//        if (isValidPhone(mobileEditText.text.toString())) {
//            lblMobileNumber.isErrorEnabled = false
//        } else {
//            lblMobileNumber.error = "Please Enter Valid Mobile Number."
//            return false
//        }
        return true
    }

    private fun getAge(year: Int, month: Int, day: Int): String {
        val dob = Calendar.getInstance()
        val today = Calendar.getInstance()
        dob.set(year, month, day)
        var age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR)
        val ageInt = age
        ageEditText.setText(ageInt.toString())
        return ageInt.toString()
    }

    private fun isValidPhone(phone: String): Boolean {
        var check = false
        check = if (!Pattern.matches("[a-zA-Z]+", phone)) {
            !(phone.length <= 9 || phone.length > 10)
        } else {
            false
        }
        return check
    }

}

