package net.rmitsolutions.mfexpert.lms.cbm

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.design.widget.TextInputEditText
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*


import com.stepstone.stepper.Step
import com.stepstone.stepper.VerificationError
import net.rmitsolutions.mfexpert.lms.R
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner
import kotlinx.android.synthetic.main.family_details_fragment.*
import kotlinx.android.synthetic.main.family_details_row.*
import net.rmitsolutions.mfexpert.lms.R.id.hideLay
import net.rmitsolutions.mfexpert.lms.database.entities.CBMDataEntity
import net.rmitsolutions.mfexpert.lms.databinding.FamilyDetailsFragmentBinding
import net.rmitsolutions.mfexpert.lms.databinding.FamilyDetailsRowBinding
import net.rmitsolutions.mfexpert.lms.viewmodels.FamilyDetailsInfoModel
import java.util.*


class FamilyDetailsFragment : Fragment(), Step {
    private var relationsList = arrayOf("Mother", "Father", "sister", "brother")
    private var genderList = arrayOf("Female", "Male")
    private var occupationList = arrayOf("pvt job", "govt job", "farmer", "business")
    private var literacyList = arrayOf("india", "america", "australia")
    private lateinit var relationsListSpinner: MaterialBetterSpinner
    private lateinit var genderListSpinner: MaterialBetterSpinner
    private lateinit var occupationsListSpinner: MaterialBetterSpinner
    private lateinit var literacyListSpinner: MaterialBetterSpinner
    lateinit var dataBinding: FamilyDetailsFragmentBinding

    private lateinit var familyDetailsInfoModel: FamilyDetailsInfoModel
    private lateinit var familyDetailsInfoList: MutableList<FamilyDetailsInfoModel>
    var cbmDataEntity: CBMDataEntity? = null
    private lateinit var dobEditText: TextInputEditText

    private lateinit var ageEditText: TextInputEditText
    private lateinit var calender: Calendar
    private lateinit var showBtn: Button
    lateinit var layout2: LinearLayout
    private lateinit var hideLayout: LinearLayout
    private lateinit var containerLayout: LinearLayout
    private lateinit var scrollDown: ScrollView
    lateinit var toolbar: android.support.v7.widget.Toolbar
    var dobAndAgeMap:MutableMap<TextInputEditText,TextInputEditText>?=HashMap()

    var year: Int = 0
    var month: Int = 0
    var day: Int = 0
    var map = LinkedHashMap<Button, LinearLayout>()

    var cbmActivity: CbmActivity? = null;
    lateinit var dialog: DatePickerDialog

    companion object {

        private const val LAYOUT_RESOURCE_ID_ARG_KEY = "messageResourceId"
        fun newInstance(@LayoutRes layoutResId: Int, cbmDataEntity: CBMDataEntity?): FamilyDetailsFragment {
            val args = Bundle()
            args.putInt(LAYOUT_RESOURCE_ID_ARG_KEY, layoutResId)
            val fragment = FamilyDetailsFragment()
            fragment.cbmDataEntity = cbmDataEntity;
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        //initialize your UI
        cbmActivity = (context as? CbmActivity)
        dataBinding = DataBindingUtil.inflate(
                inflater, R.layout.family_details_fragment, container, false)


        val v = dataBinding.getRoot()
        dobEditText = v.findViewById(R.id.txtDateOfBirth)
        ageEditText = v.findViewById(R.id.txtRelationAge)
        dobAndAgeMap?.put(dobEditText,ageEditText)
        containerLayout = v.findViewById(R.id.container)
        hideLayout = v.findViewById(R.id.hideLay)
        showBtn = v.findViewById(R.id.showButton)

        familyDetailsInfoModel = FamilyDetailsInfoModel("", "", "", "", "", "", "")
        familyDetailsInfoList = cbmDataEntity!!.familyDetailsInfoList!!


        if (cbmDataEntity?.familyDetailsInfo != null) {
            familyDetailsInfoModel = cbmDataEntity?.familyDetailsInfo!!
        } else {
            cbmDataEntity?.familyDetailsInfo = familyDetailsInfoModel
        }
        dataBinding.familyDetailsInfoVm = familyDetailsInfoModel

        Log.d("check", "" + familyDetailsInfoModel)
        hideLayout.visibility = View.VISIBLE
        showBtn.visibility = View.VISIBLE

        showBtn.setOnClickListener(View.OnClickListener {
            if (hideLay.visibility == View.VISIBLE) {
                hideLay.visibility = View.GONE
            } else {
                if (hideLay.visibility == View.GONE) {
                    hideLay.visibility = View.VISIBLE
                }
            }
        })


        familyDetailsInfoList.forEach { familyDetailsModel ->
            addFamilyDetailsSection(familyDetailsModel)
        }
        cbmActivity?.addIconImage!!.setOnClickListener() {
            val familyDetailsInfoModel = FamilyDetailsInfoModel("", "", "", "", "", "", "")
            addFamilyDetailsSection(familyDetailsInfoModel)
            familyDetailsInfoList.add(familyDetailsInfoModel);
            hideLayout.visibility = View.GONE

        }

        dobEditText.setOnClickListener() {
            calender = Calendar.getInstance()
            year = calender.get(Calendar.YEAR)
            month = calender.get(Calendar.MONTH)
            day = calender.get(Calendar.DAY_OF_MONTH)
            dialog = DatePickerDialog(activity, mDateSetListener, year, month, day)
            dialog.show()
        }

        val relationsListAdapter = ArrayAdapter<String>(activity, android.R.layout.simple_dropdown_item_1line, relationsList)
        relationsListSpinner = v.findViewById(R.id.chooseFamilyRelation)
        relationsListSpinner.setAdapter<ArrayAdapter<String>>(relationsListAdapter)

        val genderListAdapter = ArrayAdapter<String>(activity, android.R.layout.simple_dropdown_item_1line, genderList)
        genderListSpinner = v.findViewById(R.id.chooseGender)
        genderListSpinner.setAdapter<ArrayAdapter<String>>(genderListAdapter)

        val occupationListAdapter = ArrayAdapter<String>(activity, android.R.layout.simple_dropdown_item_1line, occupationList)
        occupationsListSpinner = v.findViewById(R.id.chooseRelationOccupation)
        occupationsListSpinner.setAdapter<ArrayAdapter<String>>(occupationListAdapter)


        val literacyListAdapter = ArrayAdapter<String>(activity, android.R.layout.simple_dropdown_item_1line, literacyList)
        literacyListSpinner = v.findViewById(R.id.chooseRelationLiteracy)
        literacyListSpinner.setAdapter<ArrayAdapter<String>>(literacyListAdapter)

        return v
    }

    override fun verifyStep(): VerificationError? {
        if (!validate()) {
           return VerificationError("")
       }
        return null
    }

    private fun validate(): Boolean {
        if (familyDetailsInfoModel.name.isBlank()) {
            lblPrimaryKycName.error = "Name is required."
            return false
        } else {
            lblPrimaryKycName.isErrorEnabled = false
        }

        if (familyDetailsInfoModel.relation.isBlank()) {
            chooseFamilyRelation.error = "Relation is required"
            return false
        } else {
            chooseFamilyRelation.setError(null)
        }

        if (familyDetailsInfoModel.dob.isBlank()) {
            lblDob.error = "Date of Birth is required"
            return false
        } else {
            lblDob.setError(null)
        }

        if (familyDetailsInfoModel.age.isBlank()) {
            lblRelationAge.error = "Age is required"
            return false
        } else {
            lblRelationAge.error = null
        }

        if (familyDetailsInfoModel.gender.isBlank()) {
            chooseGender.error = "Gender is required"
            return false
        } else {
            chooseGender.setError(null)
        }
        if (familyDetailsInfoModel.occupation.isBlank()) {
            chooseRelationOccupation.error = "Occupation is required"
            return false
        } else {
            chooseRelationOccupation.error = null
        }
        if (familyDetailsInfoModel.literacy.isBlank()) {
            chooseRelationLiteracy.error = "Literacy is required"
            return false
        } else {
            chooseRelationLiteracy.error = null
        }
        return true
    }

    private fun getAge(year: Int, month: Int, day: Int): String {
        val dob = Calendar.getInstance()
        val today = Calendar.getInstance()
        dob.set(year, month, day)
        val age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR)
        val ageInt = age
        ageEditText.setText(ageInt.toString())
        return ageInt.toString()
    }

    override fun onSelected() {
        //update UI when selected
    }

    override fun onError(error: VerificationError) {
        //handle error inside of the fragment, e.g. show error on EditText
    }

    private val mDateSetListener = DatePickerDialog.OnDateSetListener { view, y, m, d ->
        year = y
        month = m + 1
        day = d
        val date = StringBuilder().append(day).append("-").append(month).append("-").append(year).append("").toString()
        dobEditText.setText(date)
        getAge(year, month, day)
    }

    private fun addFamilyDetailsSection(familyDetailsInfoModel: FamilyDetailsInfoModel) {

        val layoutInflater = activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val addView = layoutInflater.inflate(R.layout.family_details_row, null)

        val dataBindingRow: FamilyDetailsRowBinding

        dataBindingRow = DataBindingUtil.bind(addView)!!
        dataBindingRow.familyDetailsInfoVm = familyDetailsInfoModel

        dobEditText = addView.findViewById(R.id.txtDob)
        ageEditText = addView.findViewById(R.id.txtAge)
        dobAndAgeMap?.put(dobEditText,ageEditText)
        val v = dataBinding.getRoot()
        scrollDown = v.findViewById(R.id.scrolldown)
        scrollDown.post(Runnable { scrollDown.fullScroll(View.FOCUS_DOWN) })

        val relationsListAdapter = ArrayAdapter<String>(activity,
                android.R.layout.simple_dropdown_item_1line, relationsList)
        relationsListSpinner = addView.findViewById(R.id.chooseRelation)
        relationsListSpinner.setAdapter<ArrayAdapter<String>>(relationsListAdapter)

        val genderListAdapter = ArrayAdapter<String>(activity,
                android.R.layout.simple_dropdown_item_1line, genderList)
        genderListSpinner = addView.findViewById(R.id.selectGender)
        genderListSpinner.setAdapter<ArrayAdapter<String>>(genderListAdapter)

        val occupationListAdapter = ArrayAdapter<String>(activity,
                android.R.layout.simple_dropdown_item_1line, occupationList)
        occupationsListSpinner = addView.findViewById(R.id.chooseOccupation)
        occupationsListSpinner.setAdapter<ArrayAdapter<String>>(occupationListAdapter)


        val literacyListAdapter = ArrayAdapter<String>(activity,
                android.R.layout.simple_dropdown_item_1line, literacyList)
        literacyListSpinner = addView.findViewById(R.id.chooseLiteracy)
        literacyListSpinner.setAdapter<ArrayAdapter<String>>(literacyListAdapter)

        dobEditText.setOnClickListener() { view ->
            dobEditText = (view as TextInputEditText);
            calender = Calendar.getInstance()
            year = calender.get(Calendar.YEAR)
            month = calender.get(Calendar.MONTH)
            day = calender.get(Calendar.DAY_OF_MONTH)
            ageEditText= dobAndAgeMap?.get(dobEditText)!!
            dialog = DatePickerDialog(activity, mDateSetListener, year, month, day)
            dialog.show()
        }

        val hideLayRow1: LinearLayout = addView.findViewById(R.id.hideLayoutRow)

        val hideBtn1: Button = addView.findViewById(R.id.hideButton)
        layout2 = addView.findViewById(R.id.layout2)
        containerLayout.addView(addView)

        hideBtn1.setOnClickListener(View.OnClickListener {

            if (hideLayRow1.visibility == View.VISIBLE) {
                hideLayRow1.visibility = View.GONE
            } else {
                if (hideLayRow1.visibility == View.GONE) {
                    hideLayRow1.visibility = View.VISIBLE
                }
            }

        })

        map.put(hideBtn1, hideLayRow1)
        map.forEach { entry ->
            if (entry.key != hideBtn1) {
                entry.value.visibility = View.GONE
            } else {
                entry.value.visibility = View.VISIBLE
            }
        }

    }
}
