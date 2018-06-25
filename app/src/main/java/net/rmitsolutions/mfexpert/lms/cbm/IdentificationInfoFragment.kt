package net.rmitsolutions.mfexpert.lms.cbm

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.stepstone.stepper.Step
import com.stepstone.stepper.VerificationError
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.identification_info_fragment.*
import net.rmitsolutions.libcam.ImageData
import net.rmitsolutions.libcam.LibCamera
import net.rmitsolutions.libcam.LibCameraActivity
import net.rmitsolutions.mfexpert.lms.R
import net.rmitsolutions.mfexpert.lms.database.MfExpertLmsDatabase
import net.rmitsolutions.mfexpert.lms.database.entities.CBMDataEntity
import net.rmitsolutions.mfexpert.lms.databinding.IdentificationInfoFragmentBinding
import net.rmitsolutions.mfexpert.lms.helpers.*
import net.rmitsolutions.mfexpert.lms.viewmodels.IdentificationInfoModel
import org.jetbrains.anko.support.v4.startActivity


class IdentificationInfoFragment : Fragment(), Step {
    private var primaryKYCList = arrayOf("Aadhaar", "Pan")
    private var secondaryKYCList = arrayOf("Ration Card", "Voter Card")

    lateinit var dataBinding: IdentificationInfoFragmentBinding
    private lateinit var identificationInfoModel: IdentificationInfoModel
    private lateinit var submitButton: Button
    private lateinit var successMessageText: TextView
    private lateinit var primaryKYCListSpinner: MaterialBetterSpinner
    private lateinit var secondaryKYCListSpinner: MaterialBetterSpinner
    private lateinit var loanPurposeSpinner: MaterialBetterSpinner
    private val LIB_CAMERA_RESULT = 420

    var cbmDataEntity: CBMDataEntity? = null
    var database: MfExpertLmsDatabase? = null
    private var compositeDisposable: CompositeDisposable = CompositeDisposable();

    companion object {

        private const val LAYOUT_RESOURCE_ID_ARG_KEY = "messageResourceId"

        fun newInstance(@LayoutRes layoutResId: Int, cbmDataEntity: CBMDataEntity?, database: MfExpertLmsDatabase?): IdentificationInfoFragment {
            val args = Bundle()
            args.putInt(LAYOUT_RESOURCE_ID_ARG_KEY, layoutResId)
            val fragment = IdentificationInfoFragment()
            fragment.arguments = args
            fragment.cbmDataEntity = cbmDataEntity
            fragment.database = database
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        //initialize your UI
        dataBinding = DataBindingUtil.inflate(
                inflater, R.layout.identification_info_fragment, container, false)

        val v = dataBinding.root
        submitButton = v.findViewById(R.id.sumbitDetails)
        successMessageText = v.findViewById(R.id.sucessMessage)


        identificationInfoModel = IdentificationInfoModel("", "", "", "", "", "", "", "", null)

        if (cbmDataEntity?.identificationInfoModel != null) {
            identificationInfoModel = cbmDataEntity?.identificationInfoModel!!;
        } else {
            cbmDataEntity?.identificationInfoModel = identificationInfoModel;
        }
        dataBinding.identificationDetailsInfoVm = identificationInfoModel

        // Primary Kyc Spinner
        val primaryKYCListAdapter = ArrayAdapter<String>(activity,
                android.R.layout.simple_dropdown_item_1line, getAllPrimaryKycNames())
        primaryKYCListSpinner = v.findViewById(R.id.choosePrimaryKYC)
        primaryKYCListSpinner.setAdapter<ArrayAdapter<String>>(primaryKYCListAdapter)

        // Secondary Kyc Spinner
        val secondaryKYCListAdapter = ArrayAdapter<String>(activity,
                android.R.layout.simple_dropdown_item_1line, getAllSecondaryNames())
        secondaryKYCListSpinner = v.findViewById(R.id.chooseSecondaryKYC)
        secondaryKYCListSpinner.setAdapter<ArrayAdapter<String>>(secondaryKYCListAdapter)

        // Loan Purpose Spinner
        loanPurposeSpinner = v.findViewById(R.id.chooseLoanPurpose)
        val chooseLoanPurposeAdapter = ArrayAdapter<String>(activity,
                android.R.layout.simple_dropdown_item_1line, getAllLoanPurposeNames())
        loanPurposeSpinner.setAdapter<ArrayAdapter<String>>(chooseLoanPurposeAdapter)

        submitButton.setOnClickListener() {
            submitCBMDetails()
        }

        return v
    }

    private fun getAllPrimaryKycNames(): List<String> {
        return database?.primaryKycDao()?.getAllPrimaryKycNames()!!
    }

    private fun getAllSecondaryNames(): List<String> {
        return database?.secondaryKycDao()?.getAllSecondaryKycNames()!!
    }

    private fun getAllLoanPurposeNames(): List<String> {
        return database?.loanPurposeDao()?.getAllLoanPurposeNames()!!
    }

    private fun submitCBMDetails() {

        if (!validate())
            return
        //  showProgress()
        if (cbmDataEntity?.id == null) {
            cbmDataEntity?.id = System.currentTimeMillis();
        }

        compositeDisposable.add(Single.fromCallable {

            database?.runInTransaction {
                val personInfoId: Long = database?.cbmDao()!!.insertPersonalDetailsInfo(cbmDataEntity?.personalInfoModel!!)

                cbmDataEntity?.permanentAddresInfo?.personInfoId = personInfoId;
                cbmDataEntity?.familyDetailsInfo?.personInfoId = personInfoId;
                cbmDataEntity?.presentAddresInfo?.personInfoId = personInfoId;
                cbmDataEntity?.identificationInfoModel?.personInfoId = personInfoId;
                cbmDataEntity?.familyDetailsInfoList!!.forEach { familyDetailsModelObj ->
                    familyDetailsModelObj.personInfoId = personInfoId
                }
                database?.cbmDao()?.insertidentificationDoc(cbmDataEntity?.identificationInfoModel!!)
                database?.cbmDao()?.insertPresentadd(cbmDataEntity?.presentAddresInfo!!)
                database?.cbmDao()?.insertPresentadd(cbmDataEntity?.permanentAddresInfo!!)
                database?.cbmDao()?.insertFamilyDetailsList(cbmDataEntity?.familyDetailsInfoList!!)
                database?.cbmDao()?.insertFamily(cbmDataEntity?.familyDetailsInfo!!)

            }

        }.processRequest(
                { cbmDetails ->
                  //  sucessMessage.visibility=View.VISIBLE
                   // hideProgress()
                    Toast.makeText(activity,"Successfully Saved", Toast.LENGTH_SHORT).show()
                   // submitButton.visibility=View.GONE
                    startActivity<CbmActivity>()
                },
                { err ->
                    //  hideProgress(true, err)
                    logD("Error : $err")
                }
        ))
    }

    private fun validate(): Boolean {
        if (identificationInfoModel.primaryKYC.isBlank()) {
            choosePrimaryKYC.error = "Primary KYC is required."
            return false
        } else {
            choosePrimaryKYC.setError(null)
        }

        if (identificationInfoModel.docNo.isBlank()) {
            lblPrimaryKycNo.error = "Primary KYC Doc No is required"
            return false
        } else {
            lblPrimaryKycNo.isErrorEnabled = false
        }

        if (identificationInfoModel.secondaryKYC.isBlank()) {
            chooseSecondaryKYC.error = "Secondary KYC is required"
            return false
        } else {
            chooseSecondaryKYC.error = null
        }

        if (identificationInfoModel.secondaryDocNo.isBlank()) {
            lblSecondaryKycNO.error = "Secondary KYC DOC No is required"
            return false
        } else {
            lblSecondaryKycNO.isErrorEnabled = false
        }

//        if (identificationInfoModel.loanPurpose.isBlank()) {
//            lblLoanPurpose.error = "Loan Purpose is required"
//            return false
//        } else {
//            lblLoanPurpose.isErrorEnabled = false
//        }

        if (identificationInfoModel.loanPurpose.isBlank()) {
            chooseLoanPurpose.error = "Loan Purpose is required"
            return false
        } else {
            chooseLoanPurpose.error = null
        }

        if (identificationInfoModel.imageByteArray == null) {
            toast("Please select or capture an Image !")
            return false
        }
        return true
    }

    override fun verifyStep(): VerificationError? {
        //return null if the user can go to the next personal_info_fragment, create a new VerificationError instance otherwise
        return null
    }

    override fun onSelected() {
        //update UI when selected
    }

    override fun onError(error: VerificationError) {
        //handle error inside of the fragment, e.g. show error on EditText
    }

    fun Fragment.showProgress() = activity!!.showProgress()

    fun Fragment.hideProgress(showStatus: Boolean = false, message: String? = null) {
        activity!!.hideProgress(showStatus = showStatus, message = message)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        activity?.menuInflater?.inflate(R.menu.menu_cbm, menu)
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
                identificationInfoModel.latitude = data?.getStringExtra(ImageData.Constants.IMAGE_LATITUDE).toString()
                identificationInfoModel.longitude = data?.getStringExtra(ImageData.Constants.IMAGE_LONGITUDE).toString()
                identificationInfoModel.timeStamp = data?.getStringExtra(ImageData.Constants.IMAGE_TIME_STAMP).toString()
                val libCamera = LibCamera(activity!!)
                identificationInfoModel.imageByteArray = libCamera.getByteArrayFromBase64String(data?.getStringExtra(ImageData.Constants.IMAGE_STRING_BASE_64)!!)
            }
        }
    }
}