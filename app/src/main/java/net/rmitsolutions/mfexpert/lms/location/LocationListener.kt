package net.rmitsolutions.mfexpert.lms.location

import android.location.Location

interface LocationListener {
    fun onSuccessListener(location : Location)
    fun onFailedListener(exception : Exception)
}