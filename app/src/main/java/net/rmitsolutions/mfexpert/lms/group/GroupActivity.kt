package net.rmitsolutions.mfexpert.lms.group

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import com.google.android.material.textfield.TextInputEditText
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner
import io.reactivex.Single
import kotlinx.android.synthetic.main.activity_group.*
import net.rmitsolutions.libcam.ImageData
import net.rmitsolutions.libcam.LibCamera
import net.rmitsolutions.libcam.LibCameraActivity
import net.rmitsolutions.mfexpert.lms.BaseActivity
import net.rmitsolutions.mfexpert.lms.MfExpertApp
import net.rmitsolutions.mfexpert.lms.R
import net.rmitsolutions.mfexpert.lms.database.MfExpertLmsDatabase
import net.rmitsolutions.mfexpert.lms.databinding.GroupDataBinding
import net.rmitsolutions.mfexpert.lms.dependency.components.DaggerInjectActivityComponent
import net.rmitsolutions.mfexpert.lms.helpers.logE
import net.rmitsolutions.mfexpert.lms.helpers.processRequest
import net.rmitsolutions.mfexpert.lms.viewmodels.GroupModel
import org.jetbrains.anko.toast
import java.util.*
import javax.inject.Inject

class GroupActivity : BaseActivity() {
    lateinit var dataBinding: GroupDataBinding
    private lateinit var openDateEditText: TextInputEditText
    private lateinit var strDate: String
//    private var groupInfoId: Long = 0
    private lateinit var groupModel: GroupModel
    private lateinit var calender: Calendar
    private var year: Int = 0
    private var month: Int = 0
    private var day: Int = 0
    private lateinit var dialog: DatePickerDialog
    private var centerList = arrayOf("")
    @Inject
    lateinit var database: MfExpertLmsDatabase
    private lateinit var centerListSpinner: MaterialBetterSpinner
    private val LIB_CAMERA_RESULT = 420

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_group)
        openDateEditText = findViewById(R.id.txtOpenDate)

        openDateEditText.setOnClickListener() {
            calender = Calendar.getInstance()
            year = calender.get(Calendar.YEAR)
            month = calender.get(Calendar.MONTH)
            day = calender.get(Calendar.DAY_OF_MONTH)
            dialog = DatePickerDialog(this, mDateSetListener, year, month, day)
            dialog.show()
        }


        val depComponent = DaggerInjectActivityComponent.builder()
                .applicationComponent(MfExpertApp.applicationComponent)
                .build()
        depComponent.injectGroupActivity(this)
        groupModel = GroupModel("", "", "", "", ", ", "", "", null)
        dataBinding.groupInfoVm = groupModel

        compositeDisposable.add(Single.fromCallable {
            database.centerDao().getCentersInfo()
        }.processRequest({ centerNameList ->
            centerList = centerNameList!!.toTypedArray();
            val centerListAdapter = ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, centerList)
            centerListSpinner = findViewById(R.id.chooseCenter)
            centerListSpinner.setAdapter<ArrayAdapter<String>>(centerListAdapter)
        }, { err ->
            //  hideProgress(true, err)
            logE("Error : $err")
        }))

    }

//    private fun getCurrentDate() {
//        val calendar = Calendar.getInstance()
//        val mdformat = SimpleDateFormat("dd-MMM-yyyy ")
//        strDate = mdformat.format(calendar.time)
//        // strDate=date
//        openDateEditText.setText(strDate)
//    }

    override fun getSelfNavDrawerItem() = R.id.nav_group

    fun saveDetailsClick(view: View) {
        saveGroupDetails()
    }

    private fun saveGroupDetails() {
        if (!validate())
            return
        compositeDisposable.add(Single.fromCallable {
            database.runInTransaction {
//                groupInfoId = database.groupDao().insertGroupInfo(groupModel)
                database.groupDao().insertGroupInfo(groupModel)
            }
        }.processRequest(
                { saved ->
                    // hideProgress()
                    Toast.makeText(this, "Successfully Saved", Toast.LENGTH_SHORT).show()
                    startActivity(intent)
//                    clearTexts()
//                    startActivity<CbmActivity>()
                },
                { err ->
                    //  hideProgress(true, err)
                    logE("Error : $err")
                }
        ))
    }

    private fun validate(): Boolean {
        if (groupModel.groupName.isBlank()) {
            lblGroupName.error = "Group Name required."
            return false
        } else {
            lblGroupName.isErrorEnabled = false
        }

        if (groupModel.center.isBlank()) {
            chooseCenter.error = "Center Name required"
            return false
        } else {
            chooseCenter.error = null
        }

        if (groupModel.openDate.isBlank()){
            lblOpenDate.error = "Date required"
            return false
        }

        if (groupModel.imageByteArray == null) {
            toast("Please select or capture an Image !")
            return false
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
    }

    /*private fun clearTexts() {
        txtGroupName.text?.clear()
        txtGroupDescription.text?.clear()
        chooseCenter.text.clear()
        chooseCenter.isFocusable = false
        txtOpenDate.text?.clear()
    }*/

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_group, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_camera -> {
                startActivityForResult(Intent(this, LibCameraActivity::class.java), LIB_CAMERA_RESULT)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            LIB_CAMERA_RESULT -> {
                if (resultCode == Activity.RESULT_OK) {
                    groupModel.latitude = data?.getStringExtra(ImageData.Constants.IMAGE_LATITUDE).toString()
                    groupModel.longitude = data?.getStringExtra(ImageData.Constants.IMAGE_LONGITUDE).toString()
                    groupModel.timeStamp = data?.getStringExtra(ImageData.Constants.IMAGE_TIME_STAMP).toString()
                    val libCamera = LibCamera(this)
                    groupModel.imageByteArray = libCamera.getByteArrayFromBase64String(data?.getStringExtra(ImageData.Constants.IMAGE_STRING_BASE_64)!!)
                }
            }
        }
    }
}
