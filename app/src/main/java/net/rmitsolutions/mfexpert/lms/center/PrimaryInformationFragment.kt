package net.rmitsolutions.mfexpert.lms.center

import android.app.DatePickerDialog
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import androidx.annotation.LayoutRes
import com.google.android.material.textfield.TextInputEditText
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.stepstone.stepper.Step
import com.stepstone.stepper.VerificationError
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_primary_information.*

import net.rmitsolutions.mfexpert.lms.R
import net.rmitsolutions.mfexpert.lms.database.MfExpertLmsDatabase
import net.rmitsolutions.mfexpert.lms.databinding.PrimaryInfoBinding
import net.rmitsolutions.mfexpert.lms.viewmodels.CenterPrimaryInformationModel

import java.util.*
import net.rmitsolutions.mfexpert.lms.database.entities.CenterDetailsEntity
import net.rmitsolutions.mfexpert.lms.helpers.logE
import net.rmitsolutions.mfexpert.lms.helpers.processRequest


class PrimaryInformationFragment : Fragment(), Step {

    lateinit var dataBinding: PrimaryInfoBinding
    private lateinit var openDateEditText: TextInputEditText
    private lateinit var villageListSpinner: MaterialBetterSpinner
    private lateinit var repaymentListSpinner: MaterialBetterSpinner
    private lateinit var categoryListSpinner: MaterialBetterSpinner
    var database: MfExpertLmsDatabase? = null
    var centerDetailsEntity: CenterDetailsEntity? = null
    private var repaymentList = arrayOf("Weekly", "Monthly")
    private var categoryList = arrayOf("Category1", "Category2")
    private var villageList = arrayOf("")
    var compositeDisposable: CompositeDisposable = CompositeDisposable();
    lateinit var calender: Calendar
    var year: Int = 0
    var month: Int = 0
    var day: Int = 0
    lateinit var dialog: DatePickerDialog


    companion object {
        private const val LAYOUT_RESOURCE_ID_ARG_KEY = "messageResourceId"

        fun newInstance(@LayoutRes layoutResId: Int, centerDetailsEntity: CenterDetailsEntity?, database: MfExpertLmsDatabase?): PrimaryInformationFragment {
            val args = Bundle()
            args.putInt(LAYOUT_RESOURCE_ID_ARG_KEY, layoutResId)
            val fragment = PrimaryInformationFragment()
            fragment.arguments = args
            fragment.centerDetailsEntity = centerDetailsEntity
            fragment.database = database
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        dataBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_primary_information, container, false)
        val v = dataBinding.root
        openDateEditText = v.findViewById(R.id.txtOpenDate)

        openDateEditText.setOnClickListener() {
            calender = Calendar.getInstance()
            year = calender.get(Calendar.YEAR)
            month = calender.get(Calendar.MONTH)
            day = calender.get(Calendar.DAY_OF_MONTH)
            dialog = DatePickerDialog(context!!, mDateSetListener, year, month, day)
            dialog.show()
        }
        compositeDisposable.add(Single.fromCallable {
            database?.villageDao()?.getVillagesInfo()
        }.processRequest({ villageNameList ->
            villageList = villageNameList!!.toTypedArray();
            val villageListAdapter = ArrayAdapter<String>(context!!, android.R.layout.simple_dropdown_item_1line, villageList)
            villageListSpinner = v.findViewById(R.id.chooseVillage)
            villageListSpinner.setAdapter<ArrayAdapter<String>>(villageListAdapter)
        }, { err ->
            //  hideProgress(true, err)
            logE("Error : $err")
        }
        ))

        if (centerDetailsEntity?.primaryInformationModel == null) {
            centerDetailsEntity?.primaryInformationModel = CenterPrimaryInformationModel("", "", "", "",
                    "", "", "")
        }


        dataBinding.primaryInfoVm = centerDetailsEntity?.primaryInformationModel

        /* val villageListAdapter = ArrayAdapter<String>(activity,
                 android.R.layout.simple_dropdown_item_1line, villageList)
         villageListSpinner = v.findViewById(R.id.chooseVillage)
         villageListSpinner.setAdapter<ArrayAdapter<String>>(villageListAdapter)*/

        val paymentListAdapter = ArrayAdapter<String>(context!!,
                android.R.layout.simple_dropdown_item_1line, repaymentList)
        repaymentListSpinner = v.findViewById(R.id.chooseRepaymentType)
        repaymentListSpinner.setAdapter<ArrayAdapter<String>>(paymentListAdapter)

        val categoryListAdapter = ArrayAdapter<String>(context!!, android.R.layout.simple_dropdown_item_1line, categoryList)
        categoryListSpinner = v.findViewById(R.id.chooseCategory)
        categoryListSpinner.setAdapter<ArrayAdapter<String>>(categoryListAdapter)
        return v

    }

    override fun onSelected() {

    }

    override fun verifyStep(): VerificationError? {
        if (!validate()) {
            return VerificationError("")
        }
        Log.d("", "" + centerDetailsEntity?.primaryInformationModel)
        return null
    }

    override fun onError(error: VerificationError) {

    }


    private fun validate(): Boolean {
        if (centerDetailsEntity?.primaryInformationModel?.centerName!!.isBlank()) {
            lblCenterName.error = "Center name is required."
            return false
        } else {
            lblCenterName.isErrorEnabled = false
        }

        if (centerDetailsEntity?.primaryInformationModel!!.villageName.isBlank()) {
            chooseVillage.error = "Village Name is required"
            return false
        } else {
            chooseVillage.error = null
        }

        if (centerDetailsEntity?.primaryInformationModel!!.distance.isBlank()) {
            lblDistance.error = "Distance is required."
            return false
        } else {
            lblDistance.isErrorEnabled = false
        }

        if (centerDetailsEntity?.primaryInformationModel!!.paymentType.isBlank()) {
            chooseRepaymentType.error = "Repayment Type is required"
            return false
        } else {
            chooseRepaymentType.setError(null)
        }
        if (centerDetailsEntity?.primaryInformationModel!!.category.isBlank()) {
            chooseCategory.error = "Category is required"
            return false
        } else {
            chooseCategory.error = null
        }
        return true
    }

    private val mDateSetListener = DatePickerDialog.OnDateSetListener { view, y, m, d ->
        year = y
        month = m + 1
        day = d
        val date = StringBuilder().append(day).append("-").append(month)
                .append("-").append(year).append("").toString()
        openDateEditText.setText(date)
        //getAge(year,month,day)
    }

}