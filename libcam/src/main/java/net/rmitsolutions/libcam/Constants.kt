package net.rmitsolutions.libcam

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log

object Constants {

    val TAKE_PHOTO = 201
    val CROP_PHOTO = 203

    // Bitmap Format
    val JPEG: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG
    val PNG : Bitmap.CompressFormat = Bitmap.CompressFormat.PNG
    val WEBP : Bitmap.CompressFormat = Bitmap.CompressFormat.WEBP

    val DEFAULT_BITMAP_FORMAT = JPEG
    val DEFAULT_DIRECTORY_NAME = "LibCamera"
    val SAVE_DIRECTORY_NAME = "SavePhoto"
    val DEFAULT_FILE_PREFIX = DEFAULT_DIRECTORY_NAME

    var mCurrentImageName : String = ""
    var globalBitmapUri : Uri? = null

    //Constants for permissions
    val CAMERA = "android.permission.CAMERA"
    val EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE"
    val ACCESS_LOCATION = "android.permission.ACCESS_COARSE_LOCATION"

    var mCurrentPhotoPath : String = ""

    fun tag(activity: Activity): String {
        return activity::class.java.simpleName
    }

    fun tag(context: Context): String? {
        return context::class.java.simpleName
    }
    fun logD(tag: String, message : String){
        Log.d(tag, message)
    }

    fun logE(tag: String, message: String){
        Log.e(tag, message)
    }

    //validate if the string isnull or empty
    fun notNullNotFill(validate: String?): Boolean {
        return if (validate != null) {
            validate.trim { it <= ' ' } != ""
        } else {
            false
        }
    }

}