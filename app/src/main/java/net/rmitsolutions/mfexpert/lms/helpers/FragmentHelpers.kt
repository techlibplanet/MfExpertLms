package net.rmitsolutions.mfexpert.lms.helpers

import android.support.v4.app.Fragment
import org.jetbrains.anko.toast
import timber.log.Timber

/**
 * Created by Madhu on 24-Apr-2018.
 */

val Fragment.apiAccessToken: String
    get() = activity!!.apiAccessToken

fun Fragment.toast(message: String?) = activity!!.toast("$message")

fun Fragment.showProgress() = activity!!.showProgress()

fun Fragment.hideProgress(showStatus: Boolean = false, message: String? = null) {
    activity!!.hideProgress(showStatus = showStatus, message = message)
}

fun Fragment.showStatus(message: String? = null) = activity!!.showStatus(message)

fun Fragment.hideStatus() = activity!!.hideStatus()

//check network
fun Fragment.isNetConnected(showStatus: Boolean = true) = activity!!.isNetConnected(showStatus)

//logging extensions

fun Fragment.logE(t: Throwable, message: String?) = Timber.e(t, message)

fun Fragment.logE(message: String?) = Timber.e(message)

fun Fragment.logD(t: Throwable, message: String?) = Timber.d(t, message)

fun Fragment.logD(message: String?) = Timber.d(message)