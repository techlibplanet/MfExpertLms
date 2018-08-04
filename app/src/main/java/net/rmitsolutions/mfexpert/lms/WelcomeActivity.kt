package net.rmitsolutions.mfexpert.lms

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.util.Base64
import android.view.View
import com.bumptech.glide.Glide
import com.example.mayank.libraries.androidkeystore.DeCryptor
import com.example.mayank.libraries.androidkeystore.EnCryptor
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_welcome.*
import kotlinx.android.synthetic.main.login_layout.*
import net.rmitsolutions.mfexpert.lms.dashboard.DashboardActivity
import net.rmitsolutions.mfexpert.lms.databinding.ActivityWelcomeBinding
import net.rmitsolutions.mfexpert.lms.dependency.components.DaggerInjectActivityComponent
import net.rmitsolutions.mfexpert.lms.helpers.*
import net.rmitsolutions.mfexpert.lms.models.AccessToken
import net.rmitsolutions.mfexpert.lms.network.IUser
import net.rmitsolutions.mfexpert.lms.network.TokenService
import net.rmitsolutions.mfexpert.lms.viewmodels.UserViewModel
import org.jetbrains.anko.startActivity
import java.io.IOException
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.cert.CertificateException

class WelcomeActivity : AppCompatActivity(){

    private val tokenService: TokenService by lazy { TokenService() }
    lateinit var userViewModel : UserViewModel
    lateinit var dataBinding : ActivityWelcomeBinding
    private lateinit var compositeDisposable : CompositeDisposable
    private var decryptor: DeCryptor? = null
    private var encryptor: EnCryptor? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (apiTokens!=null){
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
        compositeDisposable = CompositeDisposable()

        encryptor = EnCryptor(this)


    }

    fun onLoginClick(view: View){
        login()
    }

    fun login(){
        if(!validate()) return
        compositeDisposable.add(tokenService.getService<IUser>().validateUser(Constants.GRANT_TYPE, Constants.CLIENT_ID, Constants.CLIENT_SECRET, userViewModel.userName, userViewModel.password)
                .processRequest(this,{token ->
                    Constants.accessToken = token
                    encryptor?.encryptText(Constants.TOKEN_OBJECT_KEY, JsonHelper.KtToJson(token))
                    startActivity<DashboardActivity>()
                    finishNoAnim()
                },{errMsg ->
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
}
