package net.rmitsolutions.mfexpert.lms.helpers

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.os.Environment
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AlertDialog
import android.util.Base64
import android.view.View
import net.rmitsolutions.mfexpert.lms.keystore.DeCryptor
import com.madhuteja.checknet.CheckConnection
import kotlinx.android.synthetic.main.include_status_view.*
import net.rmitsolutions.mfexpert.lms.Constants
import net.rmitsolutions.mfexpert.lms.R
import net.rmitsolutions.mfexpert.lms.models.AccessToken
import timber.log.Timber
import java.io.IOException
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.cert.CertificateException


/**
 * Created by Madhu on 24-Apr-2018.
 */
val Context.apiTokens: AccessToken?
    get() = getAccessToken(applicationContext)



val Activity.apiTokens: AccessToken?
    get() = getAccessToken(applicationContext)

val Context.apiAccessToken: String
    get() = "Bearer ${apiTokens?.accessToken}"

val Activity.apiAccessToken: String
    get() = "Bearer ${apiTokens?.accessToken}"

val Activity.apiRefreshToken: String
    get() = apiTokens!!.refreshToken

val Context.apiRefreshToken: String
    get() = apiTokens?.refreshToken!!


val Context.isExternalStorageWritable: Boolean
    get() = isExternalStorageAvail()

fun Activity.finishNoAnim() {
    finish()
    overridePendingTransition(0, 0)
}


fun Context.getNewRefreshToken(): String {
    return getRefreshToken(applicationContext)
}

fun Activity.getNewRefreshToken(): String {
    return getRefreshToken(applicationContext)
}

fun Activity.showProgress() {
    hideStatus()
    progressBar?.visibility = View.VISIBLE
}

fun Activity.hideProgress(showStatus: Boolean = false, message: String? = null) {
    progressBar?.visibility = View.INVISIBLE
    if (showStatus) showStatus(message) else hideStatus()
}

fun Activity.showStatus(message: String? = null) {
    statusView?.visibility = View.VISIBLE
    if (!message.isNullOrBlank()) statusView?.setSubtitle(message)
}

fun Activity.hideStatus() {
    statusView?.visibility = View.INVISIBLE
}


//check network
fun Activity.isNetConnected(showStatus: Boolean = true): Boolean {
    return if (CheckConnection.with(this).isConnected) {
        hideStatus()
        true
    } else {
        if (showStatus) {
            showStatus(getString(R.string.you_are_offline))
        }
        false
    }
}

fun Context.isNetConnected() = CheckConnection.with(this).isConnected

//snack bar
fun Activity.snackBar(view: View, message: String?, duration: Int = Snackbar.LENGTH_LONG) {
    Snackbar.make(view, "$message", duration).show()
}

fun Context.snackBar(view: View, message: String?, duration: Int = Snackbar.LENGTH_LONG) {
    Snackbar.make(view, "$message", duration).show()
}

fun Activity.snackBar(view: View, message: String?, duration: Int = Snackbar.LENGTH_INDEFINITE,
                      actionText: String = "Ok", action: () -> Unit) {
    Snackbar.make(view, "$message", duration)
            .setAction(actionText
            ) {
                action()
            }.show()
}

// logging extensions
fun Activity.logE(t: Throwable, message: String?) = Timber.e(t, message)

fun Activity.logE(message: String?) = Timber.e(message)

fun Activity.logD(t: Throwable, message: String?) = Timber.d(t, message)

fun Activity.logD(message: String?) = Timber.d(message)

//external storage check
val Activity.isExternalStorageWritable: Boolean
    get() = isExternalStorageAvail()

private fun isExternalStorageAvail() = Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()

//private fun getAccessToken(context: Context): String {
//    if (Constants.ACCESS_TOKEN == null) {
//        Constants.ACCESS_TOKEN = context.getPref(SharedPrefKeys.SP_ACCESS_TOKEN_KEY, "")
//    }
//
//    return "Bearer ${Constants.ACCESS_TOKEN}"
//}

private var decryptor: DeCryptor? = null


private fun getAccessToken(context: Context): AccessToken?{
    if (Constants.accessToken == null) {
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
            Constants.accessToken = JsonHelper.jsonToKt<AccessToken>(userAccessToken)
            Constants.ACCESS_TOKEN = Constants.accessToken!!.accessToken
        }
    }
    return Constants.accessToken
}

private fun getRefreshToken(context: Context): String {
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

fun Activity.showDialog(context: Context,message : String){
    AlertDialog.Builder(context).setCancelable(false).setMessage(message).setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, which ->

        dialog.dismiss()
    }).setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
        dialog.dismiss()
    }).show()
}


fun Context.showDialog(context: Context,message : String){
    AlertDialog.Builder(context).setMessage(message).setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, which ->

        dialog.dismiss()
    }).setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
        dialog.dismiss()
    }).show()
}

