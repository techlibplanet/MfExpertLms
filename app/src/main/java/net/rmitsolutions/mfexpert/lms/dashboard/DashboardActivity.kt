package net.rmitsolutions.mfexpert.lms.dashboard

import android.Manifest
import android.accounts.Account
import android.accounts.AccountManager
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.View
import android.widget.TextView
import com.example.mayank.libraries.androidkeystore.DeCryptor
import com.example.mayank.libraries.androidkeystore.EnCryptor
import net.rmitsolutions.libcam.LibPermissions
import net.rmitsolutions.mfexpert.lms.*
import net.rmitsolutions.mfexpert.lms.Constants.ACCOUNT_AUTHORITY_ASSIGN_CATEGORY
import net.rmitsolutions.mfexpert.lms.Constants.ACCOUNT_AUTHORITY_BANKS
import net.rmitsolutions.mfexpert.lms.Constants.ACCOUNT_AUTHORITY_CASTE
import net.rmitsolutions.mfexpert.lms.Constants.ACCOUNT_AUTHORITY_DISTRICTS
import net.rmitsolutions.mfexpert.lms.Constants.ACCOUNT_AUTHORITY_HOUSE_OWNERSHIP
import net.rmitsolutions.mfexpert.lms.Constants.ACCOUNT_AUTHORITY_INCOME_PROOF
import net.rmitsolutions.mfexpert.lms.Constants.ACCOUNT_AUTHORITY_KYC_DETAILS
import net.rmitsolutions.mfexpert.lms.Constants.ACCOUNT_AUTHORITY_LITERACY
import net.rmitsolutions.mfexpert.lms.Constants.ACCOUNT_AUTHORITY_LOAN_CLOSE_TYPE
import net.rmitsolutions.mfexpert.lms.Constants.ACCOUNT_AUTHORITY_LOAN_PURPOSE
import net.rmitsolutions.mfexpert.lms.Constants.ACCOUNT_AUTHORITY_LOAN_REJECTION
import net.rmitsolutions.mfexpert.lms.Constants.ACCOUNT_AUTHORITY_MEMBER_REJECTION
import net.rmitsolutions.mfexpert.lms.Constants.ACCOUNT_AUTHORITY_NATIONALITY
import net.rmitsolutions.mfexpert.lms.Constants.ACCOUNT_AUTHORITY_OCCUPATIONS
import net.rmitsolutions.mfexpert.lms.Constants.ACCOUNT_AUTHORITY_PRIMARY_KYC
import net.rmitsolutions.mfexpert.lms.Constants.ACCOUNT_AUTHORITY_PRODUCTS
import net.rmitsolutions.mfexpert.lms.Constants.ACCOUNT_AUTHORITY_RELATIONS
import net.rmitsolutions.mfexpert.lms.Constants.ACCOUNT_AUTHORITY_RELIGION
import net.rmitsolutions.mfexpert.lms.Constants.ACCOUNT_AUTHORITY_SECONDARY_KYC
import net.rmitsolutions.mfexpert.lms.dependency.components.DaggerInjectActivityComponent
import net.rmitsolutions.mfexpert.lms.helpers.*
import net.rmitsolutions.mfexpert.lms.refreshTokenCallback.RefreshTokenCallback
import net.rmitsolutions.mfexpert.lms.refreshTokenCallback.RefreshTokenListener
import net.rmitsolutions.mfexpert.lms.models.Globals
import net.rmitsolutions.mfexpert.lms.network.IMasters
import org.jetbrains.anko.toast
import javax.inject.Inject

class DashboardActivity : BaseActivity(), RefreshTokenListener {


    private val TAG = DashboardActivity::class.java.simpleName
    @Inject
    lateinit var mastersService: IMasters

    private lateinit var relationsList: TextView

    private lateinit var libPermissions: LibPermissions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        relationsList = findViewById(R.id.relationsList)

        val permissions = arrayOf<String>(Manifest.permission.GET_ACCOUNTS)

        libPermissions = LibPermissions(this, permissions)

        // To enable Master data sync
        enableMasterSync()

        val depComponent = DaggerInjectActivityComponent.builder()
                .applicationComponent(MfExpertApp.applicationComponent)
                .build()

        depComponent.injectDashboardActivity(this)

        logD("Access token - ${apiTokens?.accessToken}")
        logD("Refresh Token - ${apiTokens?.refreshToken}")
    }

    override fun getSelfNavDrawerItem() = R.id.nav_dashboard

    private lateinit var enCryptor: EnCryptor
    private var decryptor: DeCryptor? = null
    private var refreshToken : String? = null

    fun getRelations(view: View) {
        showProgress()

//        logD("Access token - $apiAccessToken")

        //syncRelations()


//        val tokenService = TokenService()
//        encryptor = EnCryptor(this)
//        val encryptedTokenKey = Base64.decode(getPref(SharedPrefKeys.SP_ENCRYPTED_TOKEN_KEY, ""), Base64.DEFAULT)
//        val encryptedIv = Base64.decode(getPref(SharedPrefKeys.SP_ENCRYPTED_IV, ""), Base64.DEFAULT)
//        try {
//            decryptor = DeCryptor()
//        } catch (e: CertificateException) {
//            e.printStackTrace()
//        } catch (e: NoSuchAlgorithmException) {
//            e.printStackTrace()
//        } catch (e: KeyStoreException) {
//            e.printStackTrace()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//
//        val userAccessToken = decryptor?.decryptData(Constants.TOKEN_OBJECT_KEY, encryptedTokenKey, encryptedIv)
//        if (userAccessToken!=null && !userAccessToken.isEmpty()){
//            refreshToken = JsonHelper.jsonToKt<AccessToken>(userAccessToken).refreshToken
//        }
        //logD("Old Refresh Token - $refreshToken")
//        val apiAccessToken = Globals.getAccessToken(this)
//        refreshToken = apiAccessToken?.refreshToken
//        logD("Refresh Token - $refreshToken")

        logD("Refresh Token = ${apiRefreshToken}")
        val refreshTokenCallback = RefreshTokenCallback()
        refreshTokenCallback.setRefreshTokenListener(this)
        refreshTokenCallback.startTokenRefresh(this, apiRefreshToken)


//        val compositeDisposable = CompositeDisposable()
//        compositeDisposable.add(tokenService.getService<IUser>().refreshToken(Constants.REFRESH_TOKEN_GRANT_TYPE, Constants.CLIENT_ID, Constants.CLIENT_SECRET,refreshToken!!)
//                .processRequest(this, {token ->
//                    Constants.accessToken = token
//
//                    logD("New Access token - ${token.accessToken} New Refresh Token - ${token.refreshToken}")
//                    encryptor?.encryptText(Constants.TOKEN_OBJECT_KEY, JsonHelper.KtToJson(token))
//                    Log.d("RefreshToken", "Token refresh successfully!")
//                    hideProgress()
//                },{err ->
//                    Log.d("RefreshToken", "Error = $err")
//                    if (err == "401"){
//                        val intent = Intent(this, WelcomeActivity::class.java)
//                        startActivity(intent)
//
//                    }
//                    hideProgress()
//                }))

//        syncRelations()
    }

    private fun syncRelations(){
        val accessToken = Globals.getAccessToken(this)
        logD("SyncRelation ApiAccessToken - ${apiAccessToken}")
        compositeDisposable.add(mastersService.getRelation(apiAccessToken)
                .processRequest(this,
                        { relations ->
                            hideProgress()

                            relations.forEach { r ->
                                logD("Relation Id : ${r.id}")
                                logD("Relation Name  : ${r.name}")
                            }

//                            var rel = ""
//                            relations.forEach { r -> rel += r.name + " == " }
//                            relationsList.text = rel
                        },
                        { err ->
                            logD("Error $err")
                            hideProgress(true, err)
                            if (err == Constants.TOKEN_REFRESH_SUCCESS){
                                toast("Token refresh successfully")
//                                syncRelations()
                            }else {

                                logD("Unknown error occurred - $err")
                            }
//                            Globals.getErrorMessage(this, err!!)
                        }
                )
        )
    }

    override fun onTokenRefreshed(response: String) {
        toast("Response - $response")
        hideProgress()
        if (response  == Constants.TOKEN_REFRESH_SUCCESS){
            logD("Response - $response")
            syncRelations()
        }else{
            logD("Response - $response")
        }
    }

    private fun enableMasterSync(){
        val runnable = Runnable {
            logD("Account authenticator permission enabled")
            val mAccount = createSyncAccount(this)
            // Periodic sync of 24 Hours
//            ContentResolver.addPeriodicSync(mAccount, ACCOUNT_AUTHORITY_MASTERS,Bundle(), 30000)
//            ContentResolver.setSyncAutomatically(mAccount, ACCOUNT_AUTHORITY_DISTRICTS, true)
//            ContentResolver.setSyncAutomatically(mAccount, ACCOUNT_AUTHORITY_OCCUPATIONS, true)
//            ContentResolver.setSyncAutomatically(mAccount, ACCOUNT_AUTHORITY_LITERACY, true)
//            ContentResolver.setSyncAutomatically(mAccount, ACCOUNT_AUTHORITY_PRIMARY_KYC, true)
//            ContentResolver.setSyncAutomatically(mAccount, ACCOUNT_AUTHORITY_SECONDARY_KYC, true)
//            ContentResolver.setSyncAutomatically(mAccount, ACCOUNT_AUTHORITY_LOAN_PURPOSE, true)

//            ContentResolver.setMasterSyncAutomatically(true)

            ContentResolver.addPeriodicSync(mAccount, ACCOUNT_AUTHORITY_DISTRICTS, Bundle(),1200)
            ContentResolver.addPeriodicSync(mAccount, ACCOUNT_AUTHORITY_RELATIONS, Bundle(), 1500)
            ContentResolver.addPeriodicSync(mAccount, ACCOUNT_AUTHORITY_OCCUPATIONS, Bundle(),1800)
            ContentResolver.addPeriodicSync(mAccount, ACCOUNT_AUTHORITY_LITERACY, Bundle(), 1500)
            ContentResolver.addPeriodicSync(mAccount, ACCOUNT_AUTHORITY_PRIMARY_KYC, Bundle(), 1200)
            ContentResolver.addPeriodicSync(mAccount, ACCOUNT_AUTHORITY_SECONDARY_KYC, Bundle(), 1800)
            ContentResolver.addPeriodicSync(mAccount, ACCOUNT_AUTHORITY_LOAN_PURPOSE, Bundle(), 1500)
            ContentResolver.addPeriodicSync(mAccount, ACCOUNT_AUTHORITY_KYC_DETAILS, Bundle(),1200)
            ContentResolver.addPeriodicSync(mAccount, ACCOUNT_AUTHORITY_PRODUCTS, Bundle(),1200)
            ContentResolver.addPeriodicSync(mAccount, ACCOUNT_AUTHORITY_BANKS, Bundle(),1200)
            ContentResolver.addPeriodicSync(mAccount, ACCOUNT_AUTHORITY_NATIONALITY, Bundle(),1200)
            ContentResolver.addPeriodicSync(mAccount, ACCOUNT_AUTHORITY_RELIGION, Bundle(),1200)
            ContentResolver.addPeriodicSync(mAccount, ACCOUNT_AUTHORITY_CASTE, Bundle(),1200)
            ContentResolver.addPeriodicSync(mAccount, ACCOUNT_AUTHORITY_HOUSE_OWNERSHIP, Bundle(),1200)
            ContentResolver.addPeriodicSync(mAccount, ACCOUNT_AUTHORITY_INCOME_PROOF, Bundle(),1200)
            ContentResolver.addPeriodicSync(mAccount, ACCOUNT_AUTHORITY_ASSIGN_CATEGORY, Bundle(),1200)
            ContentResolver.addPeriodicSync(mAccount, ACCOUNT_AUTHORITY_LOAN_CLOSE_TYPE, Bundle(),1200)
            ContentResolver.addPeriodicSync(mAccount, ACCOUNT_AUTHORITY_LOAN_REJECTION, Bundle(),1200)
            ContentResolver.addPeriodicSync(mAccount, ACCOUNT_AUTHORITY_MEMBER_REJECTION, Bundle(),1200)


            ContentResolver.setSyncAutomatically(mAccount, ACCOUNT_AUTHORITY_DISTRICTS, true)
            ContentResolver.setSyncAutomatically(mAccount, ACCOUNT_AUTHORITY_OCCUPATIONS, true)
            ContentResolver.setSyncAutomatically(mAccount, ACCOUNT_AUTHORITY_RELATIONS, true)
            ContentResolver.setSyncAutomatically(mAccount, ACCOUNT_AUTHORITY_LITERACY, true)
            ContentResolver.setSyncAutomatically(mAccount, ACCOUNT_AUTHORITY_PRIMARY_KYC, true)
            ContentResolver.setSyncAutomatically(mAccount, ACCOUNT_AUTHORITY_SECONDARY_KYC, true)
            ContentResolver.setSyncAutomatically(mAccount, ACCOUNT_AUTHORITY_LOAN_PURPOSE, true)
            ContentResolver.setSyncAutomatically(mAccount, ACCOUNT_AUTHORITY_KYC_DETAILS, true)
            ContentResolver.setSyncAutomatically(mAccount, ACCOUNT_AUTHORITY_PRODUCTS, true)
            ContentResolver.setSyncAutomatically(mAccount, ACCOUNT_AUTHORITY_BANKS, true)
            ContentResolver.setSyncAutomatically(mAccount, ACCOUNT_AUTHORITY_NATIONALITY, true)
            ContentResolver.setSyncAutomatically(mAccount, ACCOUNT_AUTHORITY_RELIGION, true)
            ContentResolver.setSyncAutomatically(mAccount, ACCOUNT_AUTHORITY_CASTE, true)
            ContentResolver.setSyncAutomatically(mAccount, ACCOUNT_AUTHORITY_HOUSE_OWNERSHIP, true)
            ContentResolver.setSyncAutomatically(mAccount, ACCOUNT_AUTHORITY_INCOME_PROOF, true)
            ContentResolver.setSyncAutomatically(mAccount, ACCOUNT_AUTHORITY_ASSIGN_CATEGORY, true)
            ContentResolver.setSyncAutomatically(mAccount, ACCOUNT_AUTHORITY_LOAN_CLOSE_TYPE, true)
            ContentResolver.setSyncAutomatically(mAccount, ACCOUNT_AUTHORITY_LOAN_REJECTION, true)
            ContentResolver.setSyncAutomatically(mAccount, ACCOUNT_AUTHORITY_MEMBER_REJECTION, true)

        }
        libPermissions.askPermissions(runnable)
    }


    private fun createSyncAccount(context: Context): Account? {
        // Create the account type and default account
        val newAccount = Account(Constants.ACCOUNT_NAME, Constants.ACCOUNT_TYPE)
        val accountManager = context.getSystemService(Context.ACCOUNT_SERVICE) as AccountManager

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            logE("Permission not granted. Please check !")
            return newAccount
        }
        val accounts = accountManager.getAccountsByType(Constants.ACCOUNT_TYPE)
        if (accounts.isNotEmpty()) {
            Log.d(TAG, "Account ${Constants.ACCOUNT_NAME} already exists.")
            return newAccount
        }
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call context.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
            Log.d(TAG, "Account ${Constants.ACCOUNT_NAME} added successfully.")
        }
        return newAccount
    }
}
