package net.rmitsolutions.mfexpert.lms.cbm

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.gms.common.util.ArrayUtils.indexOf
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


import com.stepstone.stepper.Step
import com.stepstone.stepper.VerificationError
import net.rmitsolutions.mfexpert.lms.R
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner
import kotlinx.android.synthetic.main.family_details_fragment.*
import kotlinx.android.synthetic.main.family_details_row.*
import kotlinx.android.synthetic.main.family_details_row.view.*
import net.rmitsolutions.mfexpert.lms.Constants
import net.rmitsolutions.mfexpert.lms.R.id.*
import net.rmitsolutions.mfexpert.lms.database.MfExpertLmsDatabase
import net.rmitsolutions.mfexpert.lms.database.entities.CBMDataEntity
import net.rmitsolutions.mfexpert.lms.database.entities.Literacy
import net.rmitsolutions.mfexpert.lms.database.entities.Occupation
import net.rmitsolutions.mfexpert.lms.database.entities.Relation
import net.rmitsolutions.mfexpert.lms.databinding.FamilyDetailsFragmentBinding
import net.rmitsolutions.mfexpert.lms.databinding.FamilyDetailsRowBinding
import net.rmitsolutions.mfexpert.lms.helpers.logD
import net.rmitsolutions.mfexpert.lms.helpers.toast
import net.rmitsolutions.mfexpert.lms.viewmodels.FamilyDetailModels
import net.rmitsolutions.mfexpert.lms.viewmodels.FamilyDetailsInfoModel
import org.jetbrains.anko.find
import java.util.*
import kotlin.math.log


class FamilyDetailsFragment : Fragment(), Step {

    private var genderList = arrayOf("Female", "Male")
    private lateinit var relationsListSpinner: MaterialBetterSpinner
    private lateinit var genderListSpinner: MaterialBetterSpinner
    private lateinit var occupationsListSpinner: MaterialBetterSpinner
    private lateinit var literacyListSpinner: MaterialBetterSpinner
    lateinit var dataBinding: FamilyDetailsFragmentBinding
    var clicked: Boolean = false
    lateinit var addView: View

    private lateinit var familyDetailsInfoModel: FamilyDetailModels
    private lateinit var familyDetailsInfoList: MutableList<FamilyDetailModels>
    var cbmDataEntity: CBMDataEntity? = null
    private lateinit var dobEditText: TextInputEditText
    private lateinit var ageEditText: TextInputEditText
    private lateinit var dobEditTextParent : TextInputEditText
    private lateinit var ageEditTextParent : TextInputEditText
    private lateinit var calender: Calendar
    private lateinit var showBtn: Button
    private lateinit var deleteBtn: Button
    private lateinit var hideLayout: LinearLayout
    private lateinit var containerLayout: LinearLayout
    private lateinit var scrollDown: ScrollView
    lateinit var toolbar: Toolbar
    var dobAndAgeMap: MutableMap<TextInputEditText, TextInputEditText>? = HashMap()
    var addViewList: MutableList<View>? = ArrayList();
    var addViewAndFamilyDetailsMap: MutableMap<View, FamilyDetailModels>? = HashMap()
    var deleteButtonAndAddViewMap: MutableMap<Button, View>? = HashMap()
    var year: Int = 0
    var month: Int = 0
    var day: Int = 0
    var map = LinkedHashMap<Button, LinearLayout>()

    var cbmActivity: CbmActivity? = null;
    lateinit var dialog: DatePickerDialog

    var database: MfExpertLmsDatabase? = null


    companion object {

        private const val LAYOUT_RESOURCE_ID_ARG_KEY = "messageResourceId"
        fun newInstance(@LayoutRes layoutResId: Int, cbmDataEntity: CBMDataEntity?, database: MfExpertLmsDatabase?): FamilyDetailsFragment {
            val args = Bundle()
            args.putInt(LAYOUT_RESOURCE_ID_ARG_KEY, layoutResId)
            val fragment = FamilyDetailsFragment()
            fragment.cbmDataEntity = cbmDataEntity;
            fragment.arguments = args
            fragment.database = database
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        //initialize your UI
        cbmActivity = (context as? CbmActivity)
        dataBinding = DataBindingUtil.inflate(
                inflater, R.layout.family_details_fragment, container, false)

        val v = dataBinding.root
        dobEditTextParent = v.findViewById(R.id.txtDateOfBirth)
        ageEditTextParent = v.findViewById(R.id.txtRelationAge)
        dobAndAgeMap?.put(dobEditTextParent, ageEditTextParent)
        containerLayout = v.findViewById(R.id.container)
        hideLayout = v.findViewById(R.id.hideLay)
        showBtn = v.findViewById(R.id.showButton)

        familyDetailsInfoModel = FamilyDetailModels("", "", "", "", "", "", "",0,0,0)
        if(cbmDataEntity?.familyDetailsInfoList!!.isEmpty()){
            cbmDataEntity?.familyDetailsInfoList!!.add(familyDetailsInfoModel) ;
        }else{
            familyDetailsInfoModel= cbmDataEntity?.familyDetailsInfoList!!.get(0)
        }
        familyDetailsInfoList = cbmDataEntity?.familyDetailsInfoList!!


        if (cbmDataEntity?.familyDetailsInfo != null) {
            familyDetailsInfoModel = cbmDataEntity?.familyDetailsInfo!!
        } else {
            cbmDataEntity?.familyDetailsInfo = familyDetailsInfoModel
        }

        dataBinding.familyDetailsInfoVm = familyDetailsInfoModel

        Log.d("check", "" + familyDetailsInfoModel)
        // hideLayout.visibility = View.VISIBLE
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

        ageEditTextParent.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(text: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                val age = text.toString()
                val date = 1
                if (age!=""){
                    val calendar = Calendar.getInstance()
                    calendar.add(Calendar.YEAR, -age.toInt())
                    calendar.set(Calendar.DATE,date)
                    calendar.set(Calendar.MONTH, Calendar.JULY)
                    val date = Constants.getDate(calendar.time)
                    logD("Year - $date")
                    dobEditTextParent.setText(date)
                }
            }
        })

//        familyDetailsInfoList.forEach {
//            logD("Inside for each")
//            addFamilyDetailsSection(familyDetailsInfoModel)
//
//        }

        logD("List Size - ${familyDetailsInfoList.size}")
        var count=0;
        familyDetailsInfoList.forEach { familyDetailsModel ->
            if(count>0) {
                addFamilyDetailsSection(familyDetailsModel)
            }
            count++;
        }

        // Check this line

//        if (familyDetailsInfoList.size == 0){
//            logD("Inside list size is zero")
//            familyDetailsInfoList.add(familyDetailsInfoModel)
//
//        }

//        familyDetailsInfoList.add(familyDetailsInfoModel)


        cbmActivity?.addIconImage!!.setOnClickListener() {
            clicked = true
            val familyDetailsInfoModel = FamilyDetailModels("", "", "", "", "", "", "",Constants.familyDetailsRelationId,Constants.familyDetailsOccupationId,Constants.familyDetailsLiteracyId)
            addFamilyDetailsSection(familyDetailsInfoModel)
            familyDetailsInfoList.add(familyDetailsInfoModel);

            hideLayout.visibility = View.GONE
        }

        dobEditTextParent.setOnClickListener() {
            calender = Calendar.getInstance()
            year = calender.get(Calendar.YEAR)
            month = calender.get(Calendar.MONTH)
            day = calender.get(Calendar.DAY_OF_MONTH)
            dialog = DatePickerDialog(activity, mDateSetListenerParent, year, month, day)
            dialog.show()
        }

        val relationsListAdapter = ArrayAdapter<String>(activity, android.R.layout.simple_dropdown_item_1line, getAllRelationNames())
        relationsListSpinner = v.findViewById(R.id.chooseFamilyRelation)
        relationsListSpinner.setAdapter<ArrayAdapter<String>>(relationsListAdapter)

        relationsListSpinner.setOnItemClickListener { adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->
            logD("Position CLicked - $i")
            val list = getAllRelations()
            Constants.familyDetailsRelationId = list!![i].id.toInt()
            familyDetailsInfoModel.relationId = list[i].id.toInt()
            logD("Id - ${list[i].id} District Name - ${list[i].name}")
        }


        val genderListAdapter = ArrayAdapter<String>(activity, android.R.layout.simple_dropdown_item_1line, genderList)
        genderListSpinner = v.findViewById(R.id.chooseGender)
        genderListSpinner.setAdapter<ArrayAdapter<String>>(genderListAdapter)

        relationsListSpinner.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                showBtn.text = text
                when (text.toString()) {
                    "Spouse" -> genderListSpinner.setText("")
                    "Guardian" -> genderListSpinner.setText("")
                    "Father" -> genderListSpinner.setText("Male")
                    "Brother" -> genderListSpinner.setText("Male")
                    "Sister" -> genderListSpinner.setText("Female")
                    "Cousin" -> genderListSpinner.setText("")
                    "Friend" -> genderListSpinner.setText("")
                    "Brother-in-law" -> genderListSpinner.setText("")
                    "Mother" -> genderListSpinner.setText("Female")
                    "Son" -> genderListSpinner.setText("Male")
                    "Daughter" -> genderListSpinner.setText("Female")
                    "Other" -> genderListSpinner.setText("")
                }
            }
        })

        val occupationListAdapter = ArrayAdapter<String>(activity, android.R.layout.simple_dropdown_item_1line, getAllOccupationNames())
        occupationsListSpinner = v.findViewById(R.id.chooseRelationOccupation)
        occupationsListSpinner.setAdapter<ArrayAdapter<String>>(occupationListAdapter)

        occupationsListSpinner.setOnItemClickListener { adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->
            logD("Position CLicked - $i")
            val list = getAllOccupations()
            Constants.familyDetailsOccupationId = list!![i].id.toInt()
            familyDetailsInfoModel.occupationId = list[i].id.toInt()
            logD("Id - ${list[i].id} Occupation Name - ${list[i].name}")
        }



        val literacyListAdapter = ArrayAdapter<String>(activity, android.R.layout.simple_dropdown_item_1line, getAllLiteracyNames())
        literacyListSpinner = v.findViewById(R.id.chooseRelationLiteracy)
        literacyListSpinner.setAdapter<ArrayAdapter<String>>(literacyListAdapter)

        literacyListSpinner.setOnItemClickListener { adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->
            logD("Position CLicked - $i")
            val list = getAllLiteracy()
            Constants.familyDetailsLiteracyId= list!![i].id.toInt()
            familyDetailsInfoModel.literacyId = list[i].id.toInt()
            logD("Id - ${list[i].id} District Name - ${list[i].name}")
        }

        return v
    }

    private fun getAllRelationNames(): List<String> {
        return database?.relationDao()?.getAllRelationsNames()!!
    }

    private fun getAllRelations(): List<Relation>? {
        return database?.relationDao()?.getAllRelation()
    }

    private fun getAllOccupationNames(): List<String> {
        return database?.occupationDao()?.getAllOccupationNames()!!
    }

    private fun getAllLiteracyNames(): List<String> {
        return database?.literacyDao()?.getAllLiteracyNames()!!
    }

    private fun getAllLiteracy(): List<Literacy>? {
        return database?.literacyDao()?.getAllLiteracy()
    }

    private fun getAllOccupations(): List<Occupation>? {
        return database?.occupationDao()?.getAllOccupation()
    }

    override fun verifyStep(): VerificationError? {
        if (!validate()) {
            return VerificationError("")
        }
        logD("size--${familyDetailsInfoList.size}")
        if(familyDetailsInfoList.size>1){
            if (clicked == true) {

                if (!validateFamilySection()) {
                    return VerificationError("")
                }
            }
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
            lblDob.error = null
        }

        if (familyDetailsInfoModel.age.isBlank()) {
            lblRelationAge.error = "Age is required"
            return false
        } else {
            lblRelationAge.error = null
        }

        if (familyDetailsInfoModel.age == "0") {
            lblRelationAge.error = "Enter valid age"
            return false
        } else {
            lblRelationAge.error = null
        }

        if (familyDetailsInfoModel.gender.isBlank()) {
            chooseGender.error = "Gender is required"
            return false
        } else {
            chooseGender.error = null
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

    private fun validateFamilySection(): Boolean {

        addViewAndFamilyDetailsMap?.forEach { entry ->
            if (entry.value.name.isBlank()) {
                val lblName: TextInputLayout = addView.findViewById(R.id.lblName)
                lblName.error = "Name is required"
                return false
            }else{
                lblName.error = null
            }
            val relation: MaterialBetterSpinner = addView.findViewById(R.id.chooseRelation)
            if (entry.value.relation.isBlank()) {
                relation.error = "Relation is required"
                return false
            }else{
                relation.error = null
            }

            val lblDob: TextInputLayout = addView.findViewById(R.id.lblDateOfBirth)
            if (entry.value.dob.isBlank()) {
                lblDob.error = "Dob is required"
                return false
            }else{
                lblDob.error = null
            }

            val lblAge: TextInputLayout = addView.findViewById(R.id.lbAge)
            if (entry.value.age.isBlank()) {
                lblAge.error = "Age is required"
                return false
            }else{
                lblAge.error = null
            }
//
            val gender: MaterialBetterSpinner = addView.findViewById(R.id.selectGender)
            if (entry.value.gender.isBlank()) {
                gender.error = "Gender is required"
                return false
            }else {
                gender.error = null
            }

            val occupation: MaterialBetterSpinner = addView.findViewById(R.id.chooseOccupation)
            if (entry.value.occupation.isBlank()) {
                occupation.error = "Occupation is required"
                return false
            }else{
                occupation.error = null
            }

            val literacy: MaterialBetterSpinner = addView.findViewById(R.id.chooseLiteracy)
            if (entry.value.literacy.isBlank()) {
                literacy.error = "Literacy is required"
                return false
            }else {
                literacy.error = null
            }

        }

        //addViewAndFamilyDetailsMap.remove(addView)
        return true
    }

    private fun getAgeParent(year: Int, month: Int, day: Int): String {
        val dob = Calendar.getInstance()
        val today = Calendar.getInstance()
        dob.set(year, month, day)
        val age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR)
        val ageInt = age
        ageEditTextParent.setText(ageInt.toString())
        return ageInt.toString()
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

    private val mDateSetListenerParent = DatePickerDialog.OnDateSetListener { view, y, m, d ->
        year = y
        month = m + 1
        day = d
        val date = StringBuilder().append(year).append("-").append(month).append("-").append(day).append("").toString()
        dobEditTextParent.setText(date)
        getAgeParent(year, month, day)
    }

    private val mDateSetListener = DatePickerDialog.OnDateSetListener { view, y, m, d ->
        year = y
        month = m + 1
        day = d
        val date = StringBuilder().append(year).append("-").append(month).append("-").append(day).append("").toString()
        dobEditText.setText(date)
        getAge(year, month, day)
    }

    private fun addFamilyDetailsSection(familyDetailsInfoModel: FamilyDetailModels) {

        val layoutInflater = activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        addView = layoutInflater.inflate(R.layout.family_details_row, null)

        val dataBindingRow: FamilyDetailsRowBinding

        dataBindingRow = DataBindingUtil.bind(addView)!!
        dataBindingRow.familyDetailsInfoVm = familyDetailsInfoModel
        /*    val lblName: TextInputLayout = addView.findViewById(R.id.lblName)
             lblName.error="reriured"*/
        addViewAndFamilyDetailsMap?.put(addView, familyDetailsInfoModel)


        dobEditText = addView.findViewById(R.id.txtDob)
        ageEditText = addView.findViewById(R.id.txtAge)
        deleteBtn = addView.findViewById(R.id.deleteButton)

        deleteButtonAndAddViewMap?.put(deleteBtn, addView)
        dobAndAgeMap?.put(dobEditText, ageEditText)
        val v = dataBinding.root
        scrollDown = v.findViewById(R.id.scrolldown)
        scrollDown.post(Runnable { scrollDown.fullScroll(View.FOCUS_DOWN) })

        val relationsListAdapter = ArrayAdapter<String>(activity,
                android.R.layout.simple_dropdown_item_1line, getAllRelationNames())
        relationsListSpinner = addView.findViewById(R.id.chooseRelation)
        relationsListSpinner.setAdapter<ArrayAdapter<String>>(relationsListAdapter)

        relationsListSpinner.setOnItemClickListener { adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->
            logD("Position CLicked - $i")
            val list = getAllRelations()
            Constants.familyDetailsRelationId = list!![i].id.toInt()
            familyDetailsInfoModel.relationId = list[i].id.toInt()
            logD("Id - ${list[i].id} Relation Name - ${list[i].name}")
        }

        ageEditText.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                val age = text.toString()
                val date = 1
                if (age!=""){
                    val calendar = Calendar.getInstance()
                    calendar.add(Calendar.YEAR, -age.toInt())
                    calendar.set(Calendar.DATE,date)
                    calendar.set(Calendar.MONTH, Calendar.JULY)
                    val date = Constants.getDate(calendar.time)
                    logD("Year - $date")
                    dobEditText.setText(date)
                }
            }

        })



        val genderListAdapter = ArrayAdapter<String>(activity,
                android.R.layout.simple_dropdown_item_1line, genderList)
        genderListSpinner = addView.findViewById(R.id.selectGender)
        genderListSpinner.setAdapter<ArrayAdapter<String>>(genderListAdapter)

        val occupationListAdapter = ArrayAdapter<String>(activity,
                android.R.layout.simple_dropdown_item_1line, getAllOccupationNames())
        occupationsListSpinner = addView.findViewById(R.id.chooseOccupation)
        occupationsListSpinner.setAdapter<ArrayAdapter<String>>(occupationListAdapter)

        occupationsListSpinner.setOnItemClickListener { adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->
            logD("Position CLicked - $i")
            val list = getAllOccupations()
            Constants.familyDetailsRelationId = list!![i].id.toInt()
            familyDetailsInfoModel.occupationId = list[i].id.toInt()
            logD("Id - ${list[i].id} Occupation Name - ${list[i].name}")
        }


        val literacyListAdapter = ArrayAdapter<String>(activity,
                android.R.layout.simple_dropdown_item_1line, getAllLiteracyNames())
        literacyListSpinner = addView.findViewById(R.id.chooseLiteracy)
        literacyListSpinner.setAdapter<ArrayAdapter<String>>(literacyListAdapter)

        literacyListSpinner.setOnItemClickListener { adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->
            logD("Position CLicked - $i")
            val list = getAllLiteracy()
            Constants.familyDetailsRelationId = list!![i].id.toInt()
            familyDetailsInfoModel.literacyId = list[i].id.toInt()
            logD("Id - ${list[i].id} Literacy Name - ${list[i].name}")
        }

        dobEditText.setOnClickListener() { view ->
            dobEditText = (view as TextInputEditText);
            calender = Calendar.getInstance()
            year = calender.get(Calendar.YEAR)
            month = calender.get(Calendar.MONTH)
            day = calender.get(Calendar.DAY_OF_MONTH)
            ageEditText = dobAndAgeMap?.get(dobEditText)!!
            dialog = DatePickerDialog(activity, mDateSetListener, year, month, day)
            dialog.show()
        }

        val hideLayRow1: LinearLayout = addView.findViewById(R.id.hideLayoutRow)

        val hideBtn1: Button = addView.findViewById(R.id.hideButton)
        // layout2 = addView.findViewById(R.id.layout2)
        containerLayout.addView(addView)
        addViewList!!.add(addView);
        hideBtn1.setOnClickListener(View.OnClickListener {

            if (hideLayRow1.visibility == View.VISIBLE) {
                hideLayRow1.visibility = View.GONE
            } else {
                if (hideLayRow1.visibility == View.GONE) {

                    hideLayRow1.visibility = View.VISIBLE
                }
            }
        })

        deleteBtn.setOnClickListener(View.OnClickListener { button ->
            val addView1: View = deleteButtonAndAddViewMap!![button]!!
            containerLayout.removeView(addView1)
            addViewAndFamilyDetailsMap?.remove(addView1)
            familyDetailsInfoList.removeAt(familyDetailsInfoList.indexOf(familyDetailsInfoModel))
        })

        relationsListSpinner.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                hideBtn1.text = text
                when (text.toString()) {
                    "Spouse" -> genderListSpinner.setText("")
                    "Guardian" -> genderListSpinner.setText("")
                    "Father" -> genderListSpinner.setText("Male")
                    "Brother" -> genderListSpinner.setText("Male")
                    "Sister" -> genderListSpinner.setText("Female")
                    "Cousin" -> genderListSpinner.setText("")
                    "Friend" -> genderListSpinner.setText("")
                    "Brother-in-law" -> genderListSpinner.setText("Male")
                    "Mother" -> genderListSpinner.setText("Female")
                    "Son" -> genderListSpinner.setText("Male")
                    "Daughter" -> genderListSpinner.setText("Female")
                    "Other" -> genderListSpinner.setText("")
                }
            }
        })

        map[hideBtn1] = hideLayRow1
        map.forEach { entry ->
            if (entry.key != hideBtn1) {
                entry.value.visibility = View.GONE
            } else {
                entry.value.visibility = View.VISIBLE
            }
        }
    }


}
