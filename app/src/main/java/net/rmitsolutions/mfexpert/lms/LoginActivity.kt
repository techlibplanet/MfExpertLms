package net.rmitsolutions.mfexpert.lms

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.widget.FrameLayout
import net.rmitsolutions.appauthwebview.AppAuthWebView
import net.rmitsolutions.appauthwebview.IAppAuthWebViewListener
import net.rmitsolutions.mfexpert.lms.dashboard.DashboardActivity
import net.rmitsolutions.mfexpert.lms.helpers.*
import org.jetbrains.anko.startActivity


class LoginActivity : BaseActivity(), IAppAuthWebViewListener {
    private lateinit var webView: WebView
    private lateinit var errorLayout: FrameLayout
    private lateinit var loadingLayout: FrameLayout
    private lateinit var appAuthWebView: AppAuthWebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        webView = findViewById(R.id.LoginWebView)
        errorLayout = findViewById(R.id.LoginErrorLayout)
        loadingLayout = findViewById(R.id.LoginLoadingLayout)

        if (!isNetConnected(false)) {
            errorLayout.visibility = View.INVISIBLE
            loadingLayout.visibility = View.INVISIBLE
            snackBar(errorLayout, getString(R.string.you_are_offline)) {
                finishAfterTransition()
            }
            return
        }

        val data = Constants.getAppAuthWebViewData()
        appAuthWebView = AppAuthWebView.Builder()
                .webView(webView)
                .authData(data)
                .listener(this)
                .build()

        appAuthWebView.performLoginRequest()
    }

    override fun onUserAuthorize(authState: net.openid.appauth.AuthState?) {
        Constants.ACCESS_TOKEN = authState!!.accessToken
        putPref(SharedPrefKeys.SP_ACCESS_TOKEN_KEY, authState.accessToken)
        startActivity<DashboardActivity>()
        finish()
    }

    override fun showConnectionErrorLayout() {
        errorLayout.visibility = View.VISIBLE
    }

    override fun hideConnectionErrorLayout() {
        errorLayout.visibility = View.INVISIBLE
    }

    override fun showLoadingLayout() {
        loadingLayout.visibility = View.VISIBLE
    }

    override fun hideLoadingLayout() {
        loadingLayout.visibility = View.INVISIBLE
    }

    override fun onLogoutFinish() {

    }
}
