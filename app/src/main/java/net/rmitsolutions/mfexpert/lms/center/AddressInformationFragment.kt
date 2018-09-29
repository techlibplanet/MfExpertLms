package net.rmitsolutions.mfexpert.lms.center

import android.app.Activity
import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import com.stepstone.stepper.Step
import com.stepstone.stepper.VerificationError
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_address_information.*
import net.rmitsolutions.libcam.ImageData
import net.rmitsolutions.libcam.LibCamera
import net.rmitsolutions.libcam.LibCameraActivity

import net.rmitsolutions.mfexpert.lms.R
import net.rmitsolutions.mfexpert.lms.database.MfExpertLmsDatabase
import net.rmitsolutions.mfexpert.lms.database.entities.CenterDetailsEntity
import net.rmitsolutions.mfexpert.lms.databinding.AddressInfoBinding
import net.rmitsolutions.mfexpert.lms.helpers.logD
import net.rmitsolutions.mfexpert.lms.helpers.logE
import net.rmitsolutions.mfexpert.lms.helpers.processRequest
import net.rmitsolutions.mfexpert.lms.helpers.toast
import net.rmitsolutions.mfexpert.lms.viewmodels.CenterAddressInformationModel
import java.util.regex.Pattern
import org.jetbrains.anko.startActivity

class AddressInformationFragment : Fragment(), Step {

    lateinit var dataBinding: AddressInfoBinding
    private lateinit var saveCenterDetailsButton: Button
    private lateinit var meetingPlaceListSpinner: MaterialBetterSpinner
    private var mattingPlaceList = arrayOf("Habsiguda", "Pocharam")
    var centerDetailsEntity: CenterDetailsEntity? = null
    var database: MfExpertLmsDatabase? = null
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    private val LIB_CAMERA_RESULT = 420

    companion object {
        private const val LAYOUT_RESOURCE_ID_ARG_KEY = "messageResourceId"

        fun newInstance(@LayoutRes layoutResId: Int, centerDetailsEntity: CenterDetailsEntity?, database: MfExpertLmsDatabase?): AddressInformationFragment {
            val args = Bundle()
            args.putInt(LAYOUT_RESOURCE_ID_ARG_KEY, layoutResId)
            val fragment = AddressInformationFragment()
            fragment.arguments = args
            fragment.centerDetailsEntity = centerDetailsEntity
            fragment.database = database
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_address_information, container, false)
        val v = dataBinding.root
        saveCenterDetailsButton = v.findViewById(R.id.saveDetails)

        val meetingPlaceListAdapter = ArrayAdapter<String>(activity, android.R.layout.simple_dropdown_item_1line, mattingPlaceList)
        meetingPlaceListSpinner = v.findViewById(R.id.chooseMeetingPlace)
        meetingPlaceListSpinner.setAdapter<ArrayAdapter<String>>(meetingPlaceListAdapter)

        if (centerDetailsEntity?.addressInformationModel == null) {
            centerDetailsEntity?.addressInformationModel = CenterAddressInformationModel("", "", "", "",
                    "", "", "", "", "", "", "", "", null)
        }
        dataBinding.addressInfoVm = centerDetailsEntity?.addressInformationModel
//        dataBinding.addressInfoVm=addressInfoModel

        saveCenterDetailsButton.setOnClickListener(View.OnClickListener {
            submitCenterDetails()
        })
        return v
    }

    private fun submitCenterDetails() {
        if (!validate())
            return
        compositeDisposable.add(Single.fromCallable {

            database?.runInTransaction {
                val primaryInfoId = database?.centerDao()!!.insertPrimaryInformationDetails(centerDetailsEntity?.primaryInformationModel!!)

                centerDetailsEntity?.addressInformationModel?.primaryInfoId = primaryInfoId;
                database?.centerDao()?.insertAddressInformationModel(centerDetailsEntity?.addressInformationModel!!)

            }
        }.processRequest(
                { cbmDetails ->
                    // hideProgress()
                    Toast.makeText(activity, "Successfully Saved", Toast.LENGTH_SHORT).show()
                    this.activity!!.startActivity<CenterActivity>()
                },
                { err ->
                    //  hideProgress(true, err)
                    logE("Error : $err")
                }
        ))
    }


    private fun isValidPincode(pincode: String): Boolean {
        var check = false
        check = if (!Pattern.matches("[a-zA-Z]+", pincode)) {
            !(pincode.length <= 5 || pincode.length > 6)
        } else {
            false
        }
        return check
    }

    override fun onSelected() {

    }

    override fun verifyStep(): VerificationError? {
        return null
    }

    override fun onError(error: VerificationError) {

    }

    private fun validate(): Boolean {
        if (centerDetailsEntity?.addressInformationModel?.route!!.isBlank()) {
            lblRoute.error = "Route name is required."
            return false
        } else {
            lblRoute.isErrorEnabled = false
        }

        if (centerDetailsEntity?.addressInformationModel!!.location.isBlank()) {
            lblLocation.error = "Location name is required."
            return false
        } else {
            lblLocation.isErrorEnabled = false
        }

        if (centerDetailsEntity?.addressInformationModel!!.landmark.isBlank()) {
            lblLandMark.error = "Land Mark is required."
            return false
        } else {
            lblLandMark.isErrorEnabled = false
        }

        if (centerDetailsEntity?.addressInformationModel!!.meetingPlace.isBlank()) {
            chooseMeetingPlace.error = "Meeting Place Type is required"
            return false
        } else {
            chooseMeetingPlace.error = null
        }
        if (centerDetailsEntity?.addressInformationModel!!.premisesOwnerName.isBlank()) {
            lblPremisesOwnerName.error = "Premises Owner Name is required."
            return false
        } else {
            lblPremisesOwnerName.isErrorEnabled = false
        }
        if (centerDetailsEntity?.addressInformationModel!!.houseNo.isBlank()) {
            lblHouseNo.error = "House No is required."
            return false
        } else {
            lblHouseNo.isErrorEnabled = false
        }

        if (centerDetailsEntity?.addressInformationModel!!.streetName.isBlank()) {
            lblStreetName.error = "Street Name is required."
            return false
        } else {
            lblStreetName.isErrorEnabled = false

        }
        if (centerDetailsEntity?.addressInformationModel!!.villageName.isBlank()) {
            lblVillage.error = "Village Name is required."
            return false
        } else {
            lblVillage.isErrorEnabled = false

        }

        if (centerDetailsEntity?.addressInformationModel!!.pincode.isBlank()) {
            lblPincode.error = "Pin code is required."
            return false
        }

        if (centerDetailsEntity?.addressInformationModel!!.imageByteArray == null) {
            toast("Please select or capture an Image !")
            return false
        } else {
            lblPincode.isErrorEnabled = false
        }

        if (isValidPincode(txtPincode.getText().toString())) {
            lblPincode.isErrorEnabled = false
        } else {
            lblPincode.error = "Please Enter Valid Pin Number."
            return false
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        activity?.menuInflater?.inflate(R.menu.menu_center, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.menu_camera -> {
                startActivityForResult(Intent(activity, LibCameraActivity::class.java), LIB_CAMERA_RESULT)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            LIB_CAMERA_RESULT -> {
                if (resultCode == Activity.RESULT_OK)
                    logD("Result successful")
                centerDetailsEntity?.addressInformationModel?.latitude = data?.getStringExtra(ImageData.Constants.IMAGE_LATITUDE).toString()
                centerDetailsEntity?.addressInformationModel?.longitude = data?.getStringExtra(ImageData.Constants.IMAGE_LONGITUDE).toString()
                centerDetailsEntity?.addressInformationModel?.timeStamp = data?.getStringExtra(ImageData.Constants.IMAGE_TIME_STAMP).toString()
                val libCamera = LibCamera(activity!!)
                centerDetailsEntity?.addressInformationModel?.imageByteArray = libCamera.getByteArrayFromBase64String(data?.getStringExtra(ImageData.Constants.IMAGE_STRING_BASE_64)!!)
            }
        }
    }

}

