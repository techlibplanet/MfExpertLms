package net.rmitsolutions.mfexpert.lms.models

import android.content.Context
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
import net.rmitsolutions.mfexpert.lms.Constants
import net.rmitsolutions.mfexpert.lms.WelcomeActivity
import net.rmitsolutions.mfexpert.lms.helpers.*
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
        val PREF_AUTH_STATE = "oauth2_auth_state_preference"
        val PREF_USER_INFO = "pref_user_info"
        private var decryptor: DeCryptor? = null

        fun persistAuthState(context: Context, authState: AuthState) {
            val sp = PreferenceManager.getDefaultSharedPreferences(context)
            sp.edit().putString(PREF_AUTH_STATE, authState.jsonSerializeString()).apply()
        }

        fun isEmptyString(s: String?): Boolean {
            return s == null || s.trim { it <= ' ' }.isEmpty()
        }

//        fun getErrorMessage(response: Response<*>): Error {
//            try {
//                var message = response.message()
//                if (response.code() == Constants.UNAUTHORIZED_CODE) {
//                    message = "Session expired."
//                } else {
//                    val body = response.errorBody()
//                    if (body != null) {
//                        val gson = Gson()
//                        try {
//                            val json = body.string()
//                            val type = object : TypeToken<Map<String, ArrayList<String>>>() {
//
//                            }.type
//                            val map = gson.fromJson<Map<String, ArrayList<String>>>(json, type)
//                            for (key in map.keys) {
//                                message += "\n" + map[key]!![0]
//                            }
//                        } catch (e: Exception) {
//                            //ignore
//                            Log.e("Globals.getErrorMessage", e.message)
//                        }
//
//                    }
//                }
//                return Error(response.code(), message)
//            } catch (e: Exception) {
//                Log.e("Globals.getErrorMessage", e.message)
//                return Error(0, e.message!!)
//            }
//
//        }

        fun getErrorMessage(context : Context, message : String){
            if (message == "401"){
                Toast.makeText(context, "Session expired! Login again .", Toast.LENGTH_LONG).show()
                context.removePref(SharedPrefKeys.SP_ENCRYPTED_TOKEN_KEY, SharedPrefKeys.SP_ENCRYPTED_IV)
                Constants.accessToken= null
                Constants.ACCESS_TOKEN = ""
                context.startActivity<WelcomeActivity>()
            }
        }

        public fun getAppAuthConfiguration(): AppAuthConfiguration {
            return AppAuthConfiguration.Builder()
                    .setBrowserMatcher(BrowserWhitelist(
                            VersionedBrowserMatcher.CHROME_CUSTOM_TAB))
                    .setConnectionBuilder { uri ->
                        val url = URL(uri.toString())
                        url.openConnection() as HttpURLConnection
                    }
                    .build()
        }



        fun restoreAuthState(context: Context): AuthState? {
            val sp = PreferenceManager.getDefaultSharedPreferences(context)
            val jsonString = sp.getString(PREF_AUTH_STATE, null)
            if (!TextUtils.isEmpty(jsonString)) {
                try {
                    return AuthState.jsonDeserialize(jsonString!!)
                } catch (jsonException: JSONException) {
                    // should never happen
                }

            }
            return null
        }

        fun clearAuthState(context: Context) {
            val sp = PreferenceManager.getDefaultSharedPreferences(context)
            sp.edit().remove(PREF_AUTH_STATE).apply()
        }

        fun clearUserInfo(context: Context) {
            val sp = PreferenceManager.getDefaultSharedPreferences(context)
            sp.edit().remove(PREF_USER_INFO).apply()
        }

        fun getRefreshToken(context: Context): String {
            val encryptedTokenKey = Base64.decode(context.getPref(SharedPrefKeys.SP_ENCRYPTED_TOKEN_KEY, ""), Base64.DEFAULT)
            val encryptedIv = Base64.decode(context.getPref(SharedPrefKeys.SP_ENCRYPTED_IV, ""), Base64.DEFAULT)
            try {
                decryptor = DeCryptor()
            } catch (e: CertificateException) {
                e.printStackTrace()
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            } catch (e: KeyStoreException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            val userAccessToken = decryptor?.decryptData(Constants.TOKEN_OBJECT_KEY, encryptedTokenKey, encryptedIv)
            if (userAccessToken != null && !userAccessToken.isEmpty()) {
                Constants.REFRESH_TOKEN = JsonHelper.jsonToKt<AccessToken>(userAccessToken).refreshToken
            }

            return "${Constants.REFRESH_TOKEN}"
        }

        fun getAccessToken(context: Context): AccessToken? {

            if (Constants.ACCESS_TOKEN == null) {
                val encryptedTokenKey = Base64.decode(context.getPref(SharedPrefKeys.SP_ENCRYPTED_TOKEN_KEY, ""), Base64.DEFAULT)
                val encryptedIv = Base64.decode(context.getPref(SharedPrefKeys.SP_ENCRYPTED_IV, ""), Base64.DEFAULT)
                try {
                    decryptor = DeCryptor()
                } catch (e: CertificateException) {
                    e.printStackTrace()
                } catch (e: NoSuchAlgorithmException) {
                    e.printStackTrace()
                } catch (e: KeyStoreException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                val userAccessToken = decryptor?.decryptData(Constants.TOKEN_OBJECT_KEY, encryptedTokenKey, encryptedIv)
                logD("Access Token", "User Access Token - $userAccessToken")
                if (userAccessToken != null && !userAccessToken.isEmpty()) {
                    Constants.accessToken = JsonHelper.jsonToKt<AccessToken>(userAccessToken)
                    logD("Globals", "AccessToken - ${Constants.accessToken}")
                    Constants.ACCESS_TOKEN = Constants.accessToken!!.accessToken
                }
            }
            return Constants.accessToken
        }
    }


}