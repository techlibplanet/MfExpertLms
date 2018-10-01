package net.rmitsolutions.mfexpert.lms.cbm

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import android.view.*
import android.widget.*
import com.stepstone.stepper.Step
import com.stepstone.stepper.VerificationError
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.identification_info_fragment.*
import net.rmitsolutions.libcam.ImageData
import net.rmitsolutions.libcam.LibCamera
import net.rmitsolutions.libcam.LibCameraActivity
import net.rmitsolutions.mfexpert.lms.Constants
import net.rmitsolutions.mfexpert.lms.MfExpertApp
import net.rmitsolutions.mfexpert.lms.R
import net.rmitsolutions.mfexpert.lms.database.MfExpertLmsDatabase
import net.rmitsolutions.mfexpert.lms.database.entities.CBMDataEntity
import net.rmitsolutions.mfexpert.lms.database.entities.LoanPurpose
import net.rmitsolutions.mfexpert.lms.database.entities.PrimaryKYC
import net.rmitsolutions.mfexpert.lms.database.entities.SecondaryKYC
import net.rmitsolutions.mfexpert.lms.databinding.IdentificationInfoFragmentBinding
import net.rmitsolutions.mfexpert.lms.dependency.components.DaggerInjectActivityComponent
import net.rmitsolutions.mfexpert.lms.dependency.components.DaggerInjectFragmentComponent
import net.rmitsolutions.mfexpert.lms.helpers.*
import net.rmitsolutions.mfexpert.lms.network.ICBMember
import net.rmitsolutions.mfexpert.lms.scanner.ChooseIntentDialog
import net.rmitsolutions.mfexpert.lms.scanner.DocumentImageListener
import net.rmitsolutions.mfexpert.lms.viewmodels.CBMember
import net.rmitsolutions.mfexpert.lms.viewmodels.IdentificationInfoModel
import org.jetbrains.anko.find
import javax.inject.Inject


class IdentificationInfoFragment : Fragment(), DocumentImageListener, Step {

    private var primaryKYCList = arrayOf("Aadhaar", "Pan")
    private var secondaryKYCList = arrayOf("Ration Card", "Voter Card")

    @Inject
    lateinit var cbMemberService: ICBMember

    lateinit var dataBinding: IdentificationInfoFragmentBinding
    private lateinit var identificationInfoModel: IdentificationInfoModel
    private lateinit var submitButton: Button
    private lateinit var successMessageText: TextView
    private lateinit var primaryKYCListSpinner: MaterialBetterSpinner
    private lateinit var secondaryKYCListSpinner: MaterialBetterSpinner
    private lateinit var loanPurposeSpinner: MaterialBetterSpinner
    private lateinit var primaryKycImageButton : ImageButton
    private lateinit var secondaryKycImageButton : ImageButton
    private lateinit var statusPrimaryKycImage : ImageView
    private lateinit var statusSecondaryKycImage : ImageView
    private val CBM_IMAGE = 420
    private val PRIMARY_KYC_IMAGE_RESULT = 501
    private val SECONDARY_KYC_IMAGE_RESULT = 502
    private lateinit var libCamera: LibCamera
    private var primaryKycId: Int = 0
    private var secondaryKycId : Int = 0
    private var loanPurposeId : Int = 0

    private lateinit var cbmDataEntity: CBMDataEntity
    var database: MfExpertLmsDatabase? = null
    private var compositeDisposable: CompositeDisposable = CompositeDisposable();


    companion object {

        private const val LAYOUT_RESOURCE_ID_ARG_KEY = "messageResourceId"

        fun newInstance(@LayoutRes layoutResId: Int, cbmDataEntity: CBMDataEntity?, database: MfExpertLmsDatabase?): IdentificationInfoFragment {
            val args = Bundle()
            args.putInt(LAYOUT_RESOURCE_ID_ARG_KEY, layoutResId)
            val fragment = IdentificationInfoFragment()
            fragment.arguments = args
            fragment.cbmDataEntity = cbmDataEntity!!
            fragment.database = database
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val depComponent = DaggerInjectFragmentComponent.builder()
                .applicationComponent(MfExpertApp.applicationComponent)
                .build()

        depComponent.injectIdentificationInfoFragment(this)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        //initialize your UI
        dataBinding = DataBindingUtil.inflate(
                inflater, R.layout.identification_info_fragment, container, false)

        val v = dataBinding.root
        submitButton = v.find(R.id.sumbitDetails)
        primaryKycImageButton = v.find(R.id.primaryKycImage)
        secondaryKycImageButton = v.find(R.id.secondaryKycImage)
        statusPrimaryKycImage = v.find(R.id.statusPrimaryKycImage)
        statusSecondaryKycImage = v.find(R.id.statusSecondaryKycImage)
        successMessageText = v.find(R.id.sucessMessage)


        identificationInfoModel = IdentificationInfoModel("", "", "", "", "", "", "", "", null, null, null)

        if (cbmDataEntity.identificationInfoModel != null) {
            identificationInfoModel = cbmDataEntity.identificationInfoModel!!;
        } else {
            cbmDataEntity.identificationInfoModel = identificationInfoModel;
        }
        dataBinding.identificationDetailsInfoVm = identificationInfoModel

        // Primary Kyc Spinner
        val primaryKYCListAdapter = ArrayAdapter<String>(activity,
                android.R.layout.simple_dropdown_item_1line, getAllPrimaryKycNames())
        primaryKYCListSpinner = v.findViewById(R.id.choosePrimaryKYC)
        primaryKYCListSpinner.setAdapter<ArrayAdapter<String>>(primaryKYCListAdapter)

        primaryKYCListSpinner.setOnItemClickListener { adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->
            val list = getAllPrimaryKyc()
            primaryKycId= list!![i].id.toInt()
        }

        // Secondary Kyc Spinner
        val secondaryKYCListAdapter = ArrayAdapter<String>(activity,
                android.R.layout.simple_dropdown_item_1line, getAllSecondaryNames())
        secondaryKYCListSpinner = v.findViewById(R.id.chooseSecondaryKYC)
        secondaryKYCListSpinner.setAdapter<ArrayAdapter<String>>(secondaryKYCListAdapter)

        secondaryKYCListSpinner.setOnItemClickListener { adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->
            val list = getAllSecondaryKyc()
            secondaryKycId= list!![i].id.toInt()

        }

        // Loan Purpose Spinner
        loanPurposeSpinner = v.findViewById(R.id.chooseLoanPurpose)
        val chooseLoanPurposeAdapter = ArrayAdapter<String>(activity,
                android.R.layout.simple_dropdown_item_1line, getAllLoanPurposeNames())
        loanPurposeSpinner.setAdapter<ArrayAdapter<String>>(chooseLoanPurposeAdapter)

        loanPurposeSpinner.setOnItemClickListener { adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->
            val list = getAllLoanPurpose()
            loanPurposeId= list!![i].id.toInt()
        }

        submitButton.setOnClickListener() {
            submitCBMDetails()
        }

        primaryKycImageButton.setOnClickListener {
//            startActivityForResult(Intent(activity, LibCameraActivity::class.java), PRIMARY_KYC_IMAGE_RESULT)

            captureDocument(PRIMARY_KYC_IMAGE_RESULT)
        }

        secondaryKycImageButton.setOnClickListener {
//            startActivityForResult(Intent(activity, LibCameraActivity::class.java), SECONDARY_KYC_IMAGE_RESULT)
            captureDocument(SECONDARY_KYC_IMAGE_RESULT)
        }

        return v
    }

    private fun getAllPrimaryKycNames(): List<String> {
        return database?.primaryKycDao()?.getAllPrimaryKycNames()!!
    }

    private fun getAllPrimaryKyc(): List<PrimaryKYC>? {
        return database?.primaryKycDao()?.getAllPrimaryKyc()
    }

    private fun getAllSecondaryNames(): List<String> {
        return database?.secondaryKycDao()?.getAllSecondaryKycNames()!!
    }

    private fun getAllSecondaryKyc(): List<SecondaryKYC>? {
        return database?.secondaryKycDao()?.getAllSecondaryKyc()
    }

    private fun getAllLoanPurposeNames(): List<String> {
        return database?.loanPurposeDao()?.getAllLoanPurposeNames()!!
    }

    private fun getAllLoanPurpose(): List<LoanPurpose>? {
        return database?.loanPurposeDao()?.getAllLoanPurpose()
    }

    private fun submitCBMDetails() {

        if (!validate())
            return
        //  showProgress()
        if (cbmDataEntity?.id == null) {
            cbmDataEntity?.id = System.currentTimeMillis();
        }

//        compositeDisposable.add(Single.fromCallable {
//
//            database?.runInTransaction {
//                val personInfoId: Long = database?.cbmDao()!!.insertPersonalDetailsInfo(cbmDataEntity?.personalInfoModel!!)
//
//                cbmDataEntity?.permanentAddresInfo?.personInfoId = personInfoId;
//                cbmDataEntity?.familyDetailsInfo?.personInfoId = personInfoId;
//                cbmDataEntity?.presentAddresInfo?.personInfoId = personInfoId;
//                cbmDataEntity?.identificationInfoModel?.personInfoId = personInfoId;
//                cbmDataEntity?.familyDetailsInfoList!!.forEach { familyDetailsModelObj ->
//                    familyDetailsModelObj.personInfoId = personInfoId
//                }
//                database?.cbmDao()?.insertidentificationDoc(cbmDataEntity?.identificationInfoModel!!)
//                database?.cbmDao()?.insertPresentadd(cbmDataEntity?.presentAddresInfo!!)
//                database?.cbmDao()?.insertPresentadd(cbmDataEntity?.permanentAddresInfo!!)
//                database?.cbmDao()?.insertFamilyDetailsList(cbmDataEntity?.familyDetailsInfoList!!)
//                database?.cbmDao()?.insertFamily(cbmDataEntity?.familyDetailsInfo!!)
//
//            }
//
//        }.processRequest(
//                { cbmDetails ->
//                  //  sucessMessage.visibility=View.VISIBLE
//                   // hideProgress()
//                    Toast.makeText(activity,"Successfully Saved", Toast.LENGTH_SHORT).show()
//                   // submitButton.visibility=View.GONE
//                    startActivity<CbmActivity>()
//                },
//                { err ->
//                    //  hideProgress(true, err)
//                    logD("Error : $err")
//                }
//        ))

//        logD("First Name ${cbmDataEntity.personalInfoModel?.firstName}")
//        logD("Middle Name ${cbmDataEntity.personalInfoModel?.middleName}")
//        logD("Last Name ${cbmDataEntity.personalInfoModel?.surName}")
//        logD("NameAsPerKyc Name ${cbmDataEntity.personalInfoModel?.nameAsPerKYC}")
//        logD("Gender ${cbmDataEntity.personalInfoModel?.gender}")
//        logD("Martial Status ${cbmDataEntity.personalInfoModel?.maritalStatus}")
//        logD("DOB ${cbmDataEntity.personalInfoModel?.dob}")
//        logD("Age ${cbmDataEntity.personalInfoModel?.age}")
//        logD("Age As oN ${cbmDataEntity.personalInfoModel?.ageAsOn}")
//        logD("Mobile - ${cbmDataEntity.personalInfoModel?.mobile}")
//
//        logD("District Id - ${Constants.districtIdPresent}")
//        logD("Same as present - ${cbmDataEntity.permanentAddresInfo?.permanentAddressSameAsPresent}")
//
//        logD("Relation Id - ${Constants.familyDetailsRelationId}")
//        logD("Occupation Id - ${Constants.familyDetailsOccupationId}")
//        logD("Literacy Id - ${Constants.familyDetailsLiteracyId}")
//        logD("Primary Kyc Id - $primaryKycId")
//        logD("Secondary Kyc Id - $secondaryKycId")

//        val personInfoId: Long = database?.cbmDao()!!.insertPersonalDetailsInfo(cbmDataEntity.personalInfoModel!!)
//        cbmDataEntity.familyDetailsInfo?.personInfoId = personInfoId;
//        cbmDataEntity.familyDetailsInfoList!!.forEach { familyDetailsModelObj ->
//            familyDetailsModelObj.personInfoId = personInfoId
//        }

        postData()
    }

    private var familyDetailsList = ArrayList<CBMember.CbMemberFamilyDetails>()

    private fun postData(){
        val libCamera = LibCamera(activity!!)
        val cbMember = CBMember().CBMemberPersonalInfo()

//        cbMember.firstName = cbmDataEntity.personalInfoModel?.firstName
//        cbMember.middleName = cbmDataEntity.personalInfoModel?.middleName
//        cbMember.lastName = cbmDataEntity.personalInfoModel?.surName
        cbMember.nameAsPerKYC = cbmDataEntity.personalInfoModel?.nameAsPerKYC
////        cbMember.gender = 0
////        cbMember.martialStatus = 0
//        if (cbmDataEntity.personalInfoModel?.gender.equals("Male")){
//            cbMember.gender = 0
//        }else {
//            cbMember.gender = 1
//        }
//
//        logD("Gender - ${cbMember.gender}")
//
//        if (cbmDataEntity.personalInfoModel?.maritalStatus .equals("Married")){
//            cbMember.martialStatus =1
//        }else {
//            cbMember.martialStatus =0
//        }
//        logD("Martial Status - ${cbMember.martialStatus}")
////        cbMember.dobAsPerKYC = "1985-09-17T04:28:21.093Z"
        cbMember.dobAsPerKYC = cbmDataEntity.personalInfoModel?.dob
        cbMember.age = cbmDataEntity.personalInfoModel?.age?.toInt()
//        cbMember.mobileNo = cbmDataEntity.personalInfoModel?.mobile
//        cbMember.ageUptoDate = cbmDataEntity.personalInfoModel?.ageAsOn

        cbMember.firstName = "test"
        cbMember.middleName = ""
        cbMember.lastName = "asd"
        cbMember.nameAsPerKYC = "test"
        cbMember.gender = 0
        cbMember.martialStatus = 0
//        cbMember.dobAsPerKYC = "1975-09-17T04:28:21.093Z"
//        cbMember.age = 28
        cbMember.mobileNo = "9573111130"
        cbMember.ageUptoDate = "2018-09-15T06:29:00.855Z"
        cbMember.cbMemberDate = "2018-08-14T06:29:00.855Z"



//        cbMember.age = cbmDataEntity.personalInfoModel?.age?.toInt()
//        cbMember.mobileNo = cbmDataEntity.personalInfoModel?.mobile
//        cbMember.ageUptoDate = cbmDataEntity.personalInfoModel?.ageAsOn
        cbMember.applicationDate = "2018-09-17"

        cbMember.sameAsCurrentAddress = cbmDataEntity.permanentAddresInfo?.permanentAddressSameAsPresent!!
        cbMember.primaryKYCId = primaryKycId
//        cbMember.primaryKYCNo = "WAP062302300190"
//        cbMember.primaryKYCNo = "863112785437"
        cbMember.primaryKYCNo = cbmDataEntity.identificationInfoModel?.docNo
        cbMember.secondaryKYCId = secondaryKycId
//        cbMember.secondaryKYCNo = "747594413036"
        cbMember.secondaryKYCNo = cbmDataEntity.identificationInfoModel?.secondaryDocNo
        cbMember.purposeId = loanPurposeId
        logD("Base 64 - ${libCamera.getBase64StringFromByteArray(cbmDataEntity.identificationInfoModel?.cbmImageByteArray!!)}")
        cbMember.cbMemberImage = ""

        val currentAddress = CBMember.CBMemberCurrentAddress()
        currentAddress.houseNo = cbmDataEntity.presentAddresInfo?.houseNo
        currentAddress.street = cbmDataEntity.presentAddresInfo?.street
        currentAddress.mandal = cbmDataEntity.presentAddresInfo?.mandal
        currentAddress.city = cbmDataEntity.presentAddresInfo?.city
        currentAddress.mobileNo = cbmDataEntity.presentAddresInfo?.phone
        currentAddress.district = Constants.districtIdPresent
        currentAddress.pinCode = cbmDataEntity.presentAddresInfo?.pincode
        cbMember.currentAddress = currentAddress

        val permanentAddress = CBMember.CBMemberPermanentAddress()
        permanentAddress.houseNo = cbmDataEntity.permanentAddresInfo?.houseNo
        permanentAddress.street = cbmDataEntity.permanentAddresInfo?.street
        permanentAddress.mandal = cbmDataEntity.permanentAddresInfo?.mandal
        permanentAddress.city = cbmDataEntity.permanentAddresInfo?.city
        permanentAddress.mobileNo = cbmDataEntity.permanentAddresInfo?.phone
        permanentAddress.district = 1
        permanentAddress.pinCode = cbmDataEntity.permanentAddresInfo?.pincode
        cbMember.permanentAddress = permanentAddress




        for (data in cbmDataEntity.familyDetailsInfoList!!){
            val familyDetails = CBMember.CbMemberFamilyDetails()
            logD("Relation Name - ${data.relation}")
            logD("Literacy - ${data.literacy}")
            logD("Literacy Id - ${data.literacyId}")
            logD("Occupation Id - ${data.occupationId}")
            logD("Relation Id - ${data.relationId}")
            familyDetails.priority = 1
            familyDetails.name = data.name
            familyDetails.relationId = data.relationId
            familyDetails.dateOfBirth = data.dob
            familyDetails.age = data.age.toInt()
            if (data.gender == "Male"){
                familyDetails.gender = 0
            }else {
                familyDetails.gender = 1
            }

            familyDetails.occupationId = 1
            familyDetails.literacyId = data.literacyId

            familyDetailsList.add(familyDetails)

        }

//        val familyDetails = CBMember.CbMemberFamilyDetails()
//        familyDetails.priority = 1
//        familyDetails.name = cbmDataEntity.familyDetailsInfo?.name
//        familyDetails.relationId = 1
//        familyDetails.dateOfBirth = cbmDataEntity.familyDetailsInfo?.dob
//        familyDetails.age = cbmDataEntity.familyDetailsInfo?.age?.toInt()!!
//        if (cbmDataEntity.familyDetailsInfo?.gender.equals("Male")){
//            cbMember.gender = 0
//        }else {
//            cbMember.gender = 1
//        }
////        familyDetails.gender = 0
//        familyDetails.occupationId = Constants.familyDetailsOccupationId
//        familyDetails.literacyId = Constants.familyDetailsLiteracyId
//
//        familyDetailsList.add(familyDetails)
//
//        val familyDetail = CBMember.CbMemberFamilyDetails()
//        familyDetail.priority = 2
//        familyDetail.name = "Pappa"
//        familyDetail.relationId = 3
//        familyDetail.dateOfBirth = "1955-09-17T04:28:21.093Z"
//        familyDetail.age = 70
//        familyDetail.gender = 0
//        familyDetail.occupationId = 1
//        familyDetail.literacyId = 1
//
//        familyDetailsList.add(familyDetail)
//
//        val familyDetail3 = CBMember.CbMemberFamilyDetails()
//        familyDetail3.priority = 3
//        familyDetail3.name = "Pappa"
//        familyDetail3.relationId = 9
//        familyDetail3.dateOfBirth = "1955-09-17T04:28:21.093Z"
//        familyDetail3.age = 70
//        familyDetail3.gender = 0
//        familyDetail3.occupationId = 1
//        familyDetail3.literacyId = 1
//
//        familyDetailsList.add(familyDetail3)

        cbMember.cbMemberFamilyDetails = familyDetailsList

        compositeDisposable.add(cbMemberService.addCBMember(apiAccessToken, cbMember)
                .processRequest(activity!!,
                        { response ->
                            hideProgress()
                            if (response.isSuccess!!){
                                logD("Message - ${response.message}")
                                toast("Member added successfully")
                                showError(response.message)
                            }else{
                                logD("Response is not success")
                                logD("Message - ${response.message}")
                                showError(response.message)
                            }

//                            relations.loanDetails.forEach { r ->
//                                logD("Relation Id : ${r.id}")
//                                logD("Relation Name  : ${r.name}")
//                            }

                        }
                ) { err ->
                    logD("Status -  $err")
                    hideProgress()
                    when (err) {
                        Constants.TOKEN_REFRESH_SUCCESS -> context?.showDialog(activity!!,"Access Token Expired !\nAgain click on submit button to process the transaction.")
                        Constants.TOKEN_REFRESH_FAILED -> {
                            toast("Session expired! Login again!")
                           // logout()
                        }
                        else -> {logD("Unknown error occurred - $err")
                                showError(err.toString())
                        }
                    }
                }
        )
    }

    private fun showError(message : String) {
        AlertDialog.Builder(activity).setMessage(message).setPositiveButton("Ok", DialogInterface.OnClickListener({ dialog, which ->
            dialog.dismiss()
        }))
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

        if (identificationInfoModel.primaryKycImageByteArray == null){
            toast("Insert Primary Kyc Image")
            statusPrimaryKycImage.visibility = View.VISIBLE
            statusPrimaryKycImage.setImageResource(R.drawable.ic_error_red_800_24dp)
            return false
        }

        if (identificationInfoModel.secondaryKycImageByteArray == null){
            toast("Insert Secondary Kyc Image")
            statusSecondaryKycImage.visibility = View.VISIBLE
            statusSecondaryKycImage.setImageResource(R.drawable.ic_error_red_800_24dp)
            return false
        }

        if (identificationInfoModel.cbmImageByteArray == null) {
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
                startActivityForResult(Intent(activity, LibCameraActivity::class.java), CBM_IMAGE)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val libCamera = LibCamera(activity!!)
        when (requestCode) {
            CBM_IMAGE -> {
                if (resultCode == Activity.RESULT_OK)
                identificationInfoModel.latitude = data?.getStringExtra(ImageData.Constants.IMAGE_LATITUDE).toString()
                identificationInfoModel.longitude = data?.getStringExtra(ImageData.Constants.IMAGE_LONGITUDE).toString()
                identificationInfoModel.timeStamp = data?.getStringExtra(ImageData.Constants.IMAGE_TIME_STAMP).toString()

                if (ImageData.Constants.IMAGE_STRING_BASE_64!=null){
                    identificationInfoModel.cbmImageByteArray = libCamera.getByteArrayFromBase64String(data?.getStringExtra(ImageData.Constants.IMAGE_STRING_BASE_64)!!)
                }
            }
        }
    }

    private fun captureDocument(requestCode : Int){
        val manager = activity?.supportFragmentManager
        val ft = manager?.beginTransaction()
        val prev = manager?.findFragmentByTag("dialog")
        if (prev != null) {
            ft?.remove(prev)
        }
        ft?.addToBackStack(null)
        // Create and show the dialog.
        val bundle = Bundle()
        bundle.putInt(Constants.SCAN_DOCUMENT, requestCode)

        val dialogFragment = ChooseIntentDialog()
        dialogFragment.setListener(this)
        dialogFragment.arguments = bundle
        dialogFragment.show(ft, "dialog")
    }

    override fun onCaptureDocumentImage(requestCode: Int, bitmap: Bitmap) {
        libCamera = LibCamera(activity!!)
        when(requestCode){
            PRIMARY_KYC_IMAGE_RESULT ->{
                identificationInfoModel.primaryKycImageByteArray = libCamera.getByteArrayFromBitmap(bitmap, 100)
                if (identificationInfoModel.primaryKycImageByteArray!=null){
                    view?.find<ImageView>(R.id.statusPrimaryKycImage)?.setImageResource(R.drawable.ic_check_circle_green_800_24dp)
                }else{
                    view?.find<ImageView>(R.id.statusPrimaryKycImage)?.setImageResource(R.drawable.ic_error_red_800_24dp)
                    toast("Error getting primary kyc image.")
                }
            }

            SECONDARY_KYC_IMAGE_RESULT ->{
                identificationInfoModel.secondaryKycImageByteArray = libCamera.getByteArrayFromBitmap(bitmap, 100)
                if (identificationInfoModel.secondaryKycImageByteArray!=null){
                    view?.find<ImageView>(R.id.statusSecondaryKycImage)?.setImageResource(R.drawable.ic_check_circle_green_800_24dp)
                }else{
                    toast("Error getting secondary kyc image.")
                    view?.find<ImageView>(R.id.statusSecondaryKycImage)?.setImageResource(R.drawable.ic_error_red_800_24dp)
                }
            }
        }
    }
}