package net.rmitsolutions.mfexpert.lms.village

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.location.Location

import android.os.Bundle
import com.google.android.material.textfield.TextInputEditText
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner
import io.reactivex.Single
import kotlinx.android.synthetic.main.activity_village.*
import net.rmitsolutions.libcam.ImageData
import net.rmitsolutions.libcam.LibCamera
import net.rmitsolutions.libcam.LibCameraActivity
import net.rmitsolutions.mfexpert.lms.BaseActivity
import net.rmitsolutions.mfexpert.lms.MfExpertApp
import net.rmitsolutions.mfexpert.lms.R
import net.rmitsolutions.mfexpert.lms.database.MfExpertLmsDatabase
import net.rmitsolutions.mfexpert.lms.databinding.VillageInfoBinding
import net.rmitsolutions.mfexpert.lms.dependency.components.DaggerInjectActivityComponent
import net.rmitsolutions.mfexpert.lms.helpers.logD
import net.rmitsolutions.mfexpert.lms.helpers.logE
import net.rmitsolutions.mfexpert.lms.helpers.processRequest
import net.rmitsolutions.mfexpert.lms.location.LocationHelper
import net.rmitsolutions.mfexpert.lms.location.LocationListener

import net.rmitsolutions.mfexpert.lms.viewmodels.VillageInfoModel
import org.jetbrains.anko.toast
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class VillageActivity : BaseActivity(), LocationListener {

    private lateinit var villageInfoModel: VillageInfoModel
    lateinit var dataBinding: VillageInfoBinding
    private lateinit var districtListSpinner: MaterialBetterSpinner
//    private var districtList = arrayOf("East Godavari", "West Godavari", "Kurnool", "Prakasham")
//    private var distList : ArrayList<String>? = null
    private lateinit var calender: Calendar
    private lateinit var dialog: DatePickerDialog
//    private var villageInfoId: Long = 0
    private lateinit var openDateEditText: TextInputEditText
    private lateinit var strDate: String
    private var year: Int = 0
    private var month: Int = 0
    private var day: Int = 0
    private val LIB_CAMERA_RESULT = 420
    private lateinit var locationHelper: LocationHelper

    @Inject
    lateinit var database: MfExpertLmsDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_village)

        val depComponent = DaggerInjectActivityComponent.builder()
                .applicationComponent(MfExpertApp.applicationComponent)
                .build()
        depComponent.injectVillageActivity(this)

        locationHelper = LocationHelper(this)

        districtListSpinner = findViewById(R.id.chooseDistrict)
        openDateEditText = findViewById(R.id.txtOpenDate)
        getCurrentDate()
        //   compositeDisposable= CompositeDisposable()

        openDateEditText.setOnClickListener() {
            calender = Calendar.getInstance()

            year = calender.get(Calendar.YEAR)
            month = calender.get(Calendar.MONTH)
            day = calender.get(Calendar.DAY_OF_MONTH)

            dialog = DatePickerDialog(this,
                    mDateSetListener, year, month, day)
            dialog.show()
        }
        //   dataBinding.villageInfoVm= VillageInfoModel("","","","","","")


        villageInfoModel = VillageInfoModel("", "", "", "", "", "", "", "", null)
        dataBinding.villageInfoVm = villageInfoModel

        val villageInfoAdapter = ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,getDistricts())
        districtListSpinner = findViewById(R.id.chooseDistrict)
        districtListSpinner.setAdapter<ArrayAdapter<String>>(villageInfoAdapter)
    }

    private fun getDistricts(): List<String> {
        return database.districtDao().getDistrictNames()
    }


    override fun getSelfNavDrawerItem() = R.id.nav_village

    fun saveDetailsClick(view: View) {
        saveVillageDetails()
    }


    /* fun dateClick(view: View) {

         calender = Calendar.getInstance()

         year = calender.get(Calendar.YEAR)
         month = calender.get(Calendar.MONTH)
         day = calender.get(Calendar.DAY_OF_MONTH)

         dialog = DatePickerDialog(this,
                 mDateSetListener, year, month, day)

         dialog.show()
     }

     val mDateSetListener = DatePickerDialog.OnDateSetListener { view, y, m, d ->
         year = y
         month = m + 1
         day = d

         val date = StringBuilder().append(day).append("-").append(month)
                 .append("-").append(year).append("").toString()

         openDateEditText.setText(date)

     }*/

    private fun saveVillageDetails() {
        if (!validate())
            return
        compositeDisposable.add(Single.fromCallable {
            database.runInTransaction {
//                villageInfoId = database.villageDao().insertVillageInfo(villageInfoModel)
                database.villageDao().insertVillageInfo(villageInfoModel)
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

    private fun clearTexts() {
        txtVillageName.text?.clear()
        txtVillageDescription.text?.clear()
        chooseDistrict.text.clear()
        txtOpenDate.text?.clear()
        txtDistance.text?.clear()
        txtDistance.isFocusable = false
    }

    private fun getCurrentDate() {
        val calendar = Calendar.getInstance()
        val mdFormat = SimpleDateFormat("dd-MMM-yyyy ")
        strDate = mdFormat.format(calendar.time)
        // strDate=date
        openDateEditText.setText(strDate)
    }

    private fun validate(): Boolean {
        if (villageInfoModel.village.isBlank()) {
            lblVillagename.error = "Village Name is required."
            return false
        } else {
            lblVillagename.isErrorEnabled = false
        }


        if (villageInfoModel.district.isBlank()) {
            chooseDistrict.error = "District Name is required"
            return false
        } else {
            chooseDistrict.error = null
        }

        if (villageInfoModel.distance.isBlank()) {
            lblDistance.error = "Distance is required"
            return false
        } else {
            lblDistance.isErrorEnabled = false
        }

        if (villageInfoModel.imageByteArray == null) {
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_village, menu)
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
                    villageInfoModel.latitude = data?.getStringExtra(ImageData.Constants.IMAGE_LATITUDE).toString()
                    villageInfoModel.longitude = data?.getStringExtra(ImageData.Constants.IMAGE_LONGITUDE).toString()
                    villageInfoModel.timeStamp = data?.getStringExtra(ImageData.Constants.IMAGE_TIME_STAMP).toString()
                    logD("Latitude - ${villageInfoModel.latitude}, Longitude - ${villageInfoModel.longitude}")
                    val libCamera = LibCamera(this)
                    villageInfoModel.imageByteArray = libCamera.getByteArrayFromBase64String(data?.getStringExtra(ImageData.Constants.IMAGE_STRING_BASE_64)!!)
                }
            }
        }
    }

    override fun onSuccessListener(location: Location) {
        logD("Village Activity OnSuccessListenerCalled")
    }

    override fun onFailedListener(error: Exception) {
        logD("Village Activity OnFailedListener Called")
    }

}
