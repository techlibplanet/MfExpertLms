package net.rmitsolutions.mfexpert.lms.location

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.location.LocationManager
import android.os.Looper
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import android.util.Log
import android.widget.Toast
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import net.rmitsolutions.mfexpert.lms.Constants
import org.jetbrains.anko.toast

class LocationHelper(private var activity: Activity) {

    private val TAG = LocationHelper::class.java.simpleName
    private var fusedLocationProviderClient: FusedLocationProviderClient = FusedLocationProviderClient(activity)


    companion object {
        private lateinit var listener: LocationListener
    }

    init {
        // Register Fused Location Provider Client
        try {
            // Registering listener
            listener = activity as LocationListener
        } catch (e: ClassCastException) {
            throw ClassCastException("${activity::class.java.simpleName} must implement LocationListener")
        }
    }

//    fun setListener(activity : Activity,locationListener: LocationListener){
//        listener = locationListener
//        this.activity = activity
//        fusedLocationProviderClient = FusedLocationProviderClient(activity)
//    }

    // Use to get the Last Known Location
    @SuppressLint("MissingPermission")
    fun getLastLocation() {
        getSettingClient()?.checkLocationSettings(getLocationSettingRequest())?.addOnSuccessListener { locationSettingsResponse ->
            val locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        listener.onSuccessListener(location)
                    } else {
                        listener.onFailedListener(NullPointerException("Location is Null"))
                    }
                }.addOnFailureListener { exception ->
                    listener.onFailedListener(exception)
                }
            } else {
                // Show alert dialog with GPS not enable message
                buildAlertMessageNoGps()
            }
        }?.addOnFailureListener { exception ->
            val statusCode = (exception as ApiException).statusCode
            // Checking location setting resolution...
            checkResolution(statusCode, exception)
        }
    }

    // Use to Start Location Updates
    fun startLocationUpdates() {
        getSettingClient()?.checkLocationSettings(getLocationSettingRequest())?.addOnSuccessListener { locationSettingsResponse ->
            val locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                startLocationUpdate()
            } else {
                // Show alert dialog GPS not enable message
                buildAlertMessageNoGps()
            }
        }?.addOnFailureListener { exception ->
            val statusCode = (exception as ApiException).statusCode
            // Checking location setting resolution...
            checkResolution(statusCode, exception)
        }
    }

    // Checking resolution if location settings are not satisfied
    private fun checkResolution(statusCode: Int, exception: ApiException) {
        when (statusCode) {
            LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                try {
                    // Show the dialog by calling startResolutionForResult(), and check the
                    // result in onActivityResult().
                    val rae = exception as ResolvableApiException
                    rae.startResolutionForResult(activity, Constants.REQUEST_CHECK_SETTINGS)
                } catch (sie: IntentSender.SendIntentException) {
                    //Log.i(TAG, "PendingIntent unable to execute request.")
                }
            }
            LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                val errorMessage = "Location settings are inadequate, and cannot be " + "fixed here. Fix in Settings."
                //Log.e(TAG, errorMessage)
                Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    // Create Location Setting Request
    private fun getLocationSettingRequest(): LocationSettingsRequest? {
        // Create LocationSettingsRequest object using location request
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(getLocationRequest())
        return builder.build()
    }

    // Get Location Setting Client
    private fun getSettingClient(): SettingsClient? {
        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        val settingsClient = LocationServices.getSettingsClient(activity)
        settingsClient.checkLocationSettings(getLocationSettingRequest())
        return settingsClient
    }

    // Create Location Request
    private fun getLocationRequest(): LocationRequest {
        val locationRequest = LocationRequest()
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 5000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        return locationRequest
    }

    // Create Location Callback
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            val lastLocation = locationResult!!.lastLocation
            listener.onSuccessListener(lastLocation)
        }
    }

    // Start Location Updates
    @SuppressLint("MissingPermission")
    private fun startLocationUpdate() {
        fusedLocationProviderClient.requestLocationUpdates(getLocationRequest(), locationCallback, Looper.myLooper())
        activity.toast("Starting Location Updates")
    }

    // Stop Location Updates
    fun stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        activity.toast("Location update stopped !")
    }

    // Show alert dialog to enable GPS
    private fun buildAlertMessageNoGps() {
        val builder = AlertDialog.Builder(activity)
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, id -> activity.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
                .setNegativeButton("No") { dialog, id -> dialog.cancel() }
        val alert = builder.create()
        alert.show()
    }
}