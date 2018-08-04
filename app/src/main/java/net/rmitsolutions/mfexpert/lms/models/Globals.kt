package net.rmitsolutions.mfexpert.lms.models

import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.widget.Toast
import com.example.mayank.libraries.androidkeystore.DeCryptor
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import net.openid.appauth.AppAuthConfiguration
import net.openid.appauth.AuthState
import net.openid.appauth.browser.BrowserWhitelist
import net.openid.appauth.browser.VersionedBrowserMatcher
import net.rmitsolutions.libcam.Constants.logD
import net.rmitsolutions.libcam.Constants.logE
import net.rmitsolutions.mfexpert.lms.Constants
import net.rmitsolutions.mfexpert.lms.WelcomeActivity
import net.rmitsolutions.mfexpert.lms.helpers.*
import net.rmitsolutions.mfexpert.lms.refreshTokenCallback.RefreshTokenCallback
import net.rmitsolutions.mfexpert.lms.refreshTokenCallback.RefreshTokenListener
import net.rmitsolutions.mfexpert.lms.settings.adapter.adaptersyncsettings.SyncSettingsViewHolder
import org.jetbrains.anko.startActivity
import org.json.JSONException
import retrofit2.Response
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.cert.CertificateException
import java.util.ArrayList

class Globals {
    companion object {
        fun isEmptyString(s: String?): Boolean {
            return s == null || s.trim { it <= ' ' }.isEmpty()
        }

        fun logout(context: Context) {
            context.removePref(SharedPrefKeys.SP_ENCRYPTED_TOKEN_KEY, SharedPrefKeys.SP_ENCRYPTED_IV)
            Constants.accessToken = null
            Constants.ACCESS_TOKEN = ""
            context.startActivity<WelcomeActivity>()
        }

        fun refreshToken(context: Context) {
            val refreshTokenCallbacks = RefreshTokenCallback()
            refreshTokenCallbacks.setRefreshTokenListener(object : RefreshTokenListener {
                override fun onTokenRefreshFinished(response: String) {
                    when (response) {
                        Constants.TOKEN_REFRESH_SUCCESS -> {
                            logD("RxJavaHelper", "Token refresh success")
                            context.sendBroadcast(Intent(SyncSettingsViewHolder.ACTION_FINISHED_SYNC).putExtra("position", 0).putExtra(Constants.TOKEN_REFRESH_STATUS, Constants.TOKEN_REFRESH_SUCCESS));
                        }
                        Constants.TOKEN_REFRESH_FAILED -> {
                            logE("RxJavaHelper", "Token refresh Failed")
                            context.sendBroadcast(Intent(SyncSettingsViewHolder.ACTION_FINISHED_SYNC).putExtra("position", 0).putExtra(Constants.TOKEN_REFRESH_STATUS, Constants.TOKEN_REFRESH_FAILED));
                            logout(context)
                        }
                    }
                }
            })
            refreshTokenCallbacks.startTokenRefresh(context, context.apiRefreshToken)
        }
    }


}