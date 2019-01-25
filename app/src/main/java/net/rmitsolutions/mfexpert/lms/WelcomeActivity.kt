package net.rmitsolutions.mfexpert.lms

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import androidx.databinding.DataBindingUtil
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import android.view.View
import com.bumptech.glide.Glide
import net.rmitsolutions.mfexpert.lms.keystore.EnCryptor
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_welcome.*
import kotlinx.android.synthetic.main.login_layout.*
import net.rmitsolutions.mfexpert.lms.dashboard.DashboardActivity
import net.rmitsolutions.mfexpert.lms.databinding.ActivityWelcomeBinding
import net.rmitsolutions.mfexpert.lms.dependency.components.DaggerInjectActivityComponent
import net.rmitsolutions.mfexpert.lms.helpers.*
import net.rmitsolutions.mfexpert.lms.network.IUser
import net.rmitsolutions.mfexpert.lms.network.TokenService
import net.rmitsolutions.mfexpert.lms.viewmodels.UserViewModel
import org.jetbrains.anko.startActivity
import android.os.Build
import android.telephony.TelephonyManager
import android.provider.Settings
import net.rmitsolutions.libcam.LibPermissions
import net.rmitsolutions.mfexpert.lms.viewmodels.Users


class WelcomeActivity : AppCompatActivity() {

    private val tokenService: TokenService by lazy { TokenService() }
    lateinit var userViewModel: UserViewModel
    lateinit var dataBinding: ActivityWelcomeBinding
    private lateinit var compositeDisposable: CompositeDisposable
    private var encryptor: EnCryptor? = null
    private lateinit var libPermissions: LibPermissions


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (apiTokens != null) {
            Constants.accessToken = apiTokens
            logD("WelcomeActivity - AccessToken - ${apiTokens?.accessToken} Refresh Token - ${apiTokens?.refreshToken}")
            startActivity<DashboardActivity>()
            finishNoAnim()
            return
        }

        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_welcome)
        userViewModel = UserViewModel("", "")

        Glide.with(this).load(R.drawable.mfexpert).into(welcomeLogo)
        dataBinding.user = userViewModel
        compositeDisposable = CompositeDisposable()
        val depComponent = DaggerInjectActivityComponent.builder()
                .applicationComponent(MfExpertApp.applicationComponent)
                .build()
        depComponent.injectWelcomeActivity(this)
        encryptor = EnCryptor(this)
    }

    fun onLoginClick(view: View) {
        val permissions = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.CAMERA,
                Manifest.permission.READ_CONTACTS)
        libPermissions = LibPermissions(this, permissions)
        val runnable = Runnable {
            login()
        }
        libPermissions.askPermissions(runnable)
    }

    fun login() {
        if (!validate()) return
        compositeDisposable.add(tokenService.getService<IUser>().validateUser(Constants.GRANT_TYPE, Constants.CLIENT_ID, Constants.CLIENT_SECRET, userViewModel.userName, userViewModel.password)
                .processRequest({ token ->
                    Constants.accessToken = token
                    encryptor?.encryptText(Constants.TOKEN_OBJECT_KEY, JsonHelper.KtToJson(token))
//                    registerUserDevice(userViewModel.userName,token.toString())
                    startActivity<DashboardActivity>()
                    finishNoAnim()
                }, { errMsg ->
                    snackBar(buttonLogin, errMsg, Snackbar.LENGTH_INDEFINITE)
                    logE(errMsg)
                }))

    }

    private fun validate(): Boolean {
        if (userViewModel.userName.isBlank()) {
            lblUserName.error = "User name is required."
            return false
        } else {
            lblUserName.isErrorEnabled = false
        }

        if (userViewModel.password.isBlank()) {
            lblPassword.error = "Password is required."
            return false
        } else {
            lblPassword.isErrorEnabled = false
        }

        if (!isNetConnected(false)) {
            snackBar(buttonLogin, getString(R.string.you_are_offline))
            return false
        }
        return true
    }

    @SuppressLint("HardwareIds", "MissingPermission")
    private fun registerUserDevice(userId: String, accessToken: String) {
        try {
            val registerUser = Users.RegisterUser()
            registerUser.userId = userId
            registerUser.deviceId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
            val telephonyManager = this.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            registerUser.imei = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) telephonyManager.imei else telephonyManager.deviceId
            registerUser.mobile = telephonyManager.line1Number

            if (registerUser.deviceId.length < 5 || registerUser.deviceId.length > 50) {
                showDialog(this, "Device id not found or invalid.")
                return
            }

            if (registerUser.imei.isEmpty() || registerUser.imei.length != 15) {
                showDialog(this, "IMEI not found or invalid.")
                return
            }

            registerUser.mobile = registerUser.mobile.substring(0, 10)
            registerUser.mobile = "0000000000"
            if (registerUser.mobile.isEmpty() || registerUser.mobile.length != 10) {
                showDialog(this, "Mobile number not found or invalid.")
                return
            }

            logD("User Id - ${registerUser.userId}\nDevice Id - ${registerUser.deviceId}\nIMEI ${registerUser.imei}\n Phone - ${registerUser.mobile}")

        } catch (e: Exception) {
            logD("Exception - ${e.message}")
        }

    }
}
