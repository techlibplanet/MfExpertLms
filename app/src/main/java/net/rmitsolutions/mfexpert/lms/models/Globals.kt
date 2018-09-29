package net.rmitsolutions.mfexpert.lms.models

import android.Manifest
import android.accounts.Account
import android.accounts.AccountManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import android.util.Log
import net.rmitsolutions.libcam.Constants.logD
import net.rmitsolutions.libcam.Constants.logE
import net.rmitsolutions.mfexpert.lms.Constants
import net.rmitsolutions.mfexpert.lms.WelcomeActivity
import net.rmitsolutions.mfexpert.lms.helpers.SharedPrefKeys
import net.rmitsolutions.mfexpert.lms.helpers.apiRefreshToken
import net.rmitsolutions.mfexpert.lms.helpers.removePref
import net.rmitsolutions.mfexpert.lms.helpers.showDialog
import net.rmitsolutions.mfexpert.lms.refreshTokenCallback.RefreshTokenCallback
import net.rmitsolutions.mfexpert.lms.refreshTokenCallback.RefreshTokenListener
import net.rmitsolutions.mfexpert.lms.settings.adapter.adaptersyncsettings.SyncSettingsViewHolder
import org.jetbrains.anko.startActivity
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.regex.Pattern

class Globals {

    companion object {
        private val TAG = Globals::class.java.simpleName

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
                            logD("Globals", "Token refresh success")
                            context.sendBroadcast(Intent(SyncSettingsViewHolder.ACTION_FINISHED_SYNC).putExtra("position", 0).putExtra(Constants.TOKEN_REFRESH_STATUS, Constants.TOKEN_REFRESH_SUCCESS));
                        }
                        Constants.TOKEN_REFRESH_FAILED -> {
                            logE("Globals", "Token refresh Failed")
                            context.sendBroadcast(Intent(SyncSettingsViewHolder.ACTION_FINISHED_SYNC).putExtra("position", 0).putExtra(Constants.TOKEN_REFRESH_STATUS, Constants.TOKEN_REFRESH_FAILED));
                            logout(context)
                        }
                    }
                }
            })

            if (context.apiRefreshToken != null){
                refreshTokenCallbacks.startTokenRefresh(context, context.apiRefreshToken)
            }else{
                context.showDialog(context, "Token Expired ! Please login again.")
                context.startActivity<WelcomeActivity>()
            }

        }

        fun getSyncAccount(context: Context): Account {
            // Create the account type and default account
            val newAccount = Account(Constants.ACCOUNT_NAME, Constants.ACCOUNT_TYPE)
            val accountManager = context.getSystemService(Context.ACCOUNT_SERVICE) as AccountManager

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
                logE(TAG, "Permission not granted. Please check !")
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

        fun isValidPhone(phone: String): Boolean {
            var check = false
            check = if (!Pattern.matches("[a-zA-Z]+", phone)) {
                !(phone.length <= 9 || phone.length > 10)
            } else {
                false
            }
            return check
        }

        fun getRoundOffDecimalFormat(amount: Double): Double {
            val decimalFormat = DecimalFormat("#.##")
            decimalFormat.roundingMode = RoundingMode.CEILING
            return decimalFormat.format(amount).toDouble()
        }
    }
}