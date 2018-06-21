package net.rmitsolutions.mfexpert.lms

import android.os.Bundle
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import net.rmitsolutions.appauthwebview.AppAuthWebView
import net.rmitsolutions.mfexpert.lms.helpers.SharedPrefKeys
import net.rmitsolutions.mfexpert.lms.helpers.isNetConnected
import net.rmitsolutions.mfexpert.lms.helpers.removePref
import net.rmitsolutions.mfexpert.lms.helpers.snackBar
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast


class LogoutActivity : BaseActivity() {
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        webView = findViewById(R.id.LoginWebView)

        if (!isNetConnected(false)) {
            snackBar(webView, getString(R.string.you_are_offline), actionText = "OK, Logout") {
                onLogout()
                finish()
            }
            return
        }

        onLogout()
        webView.loadUrl(Constants.BASE_URL + "Account/Logout")
        webView.webViewClient = object : WebViewClient() {

            override fun onPageFinished(view: WebView, url: String) {
                toast("Logged out")
                startActivity<LoginActivity>()
                finish()
            }
        }

    }

    private fun onLogout() {
        Constants.ACCESS_TOKEN = null
        removePref(SharedPrefKeys.SP_ACCESS_TOKEN_KEY)
        AppAuthWebView.updateAuthState(this, null)
        CookieManager.getInstance().removeAllCookies(null);
        CookieManager.getInstance().flush();
    }
}