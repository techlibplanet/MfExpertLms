package net.rmitsolutions.libcam

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import net.rmitsolutions.libcam.Constants.mCurrentImageName

class LibCamera(private val activity: Activity) {

    private var actionCamera = ActionCamera(activity)

    // This method is used to capture photo
    fun takePhoto() {
        actionCamera.takePhoto()
    }

    // This method called at the time of onActivityResult called for getting image from gallery or camera
    fun getPickImageResultUri(data : Intent?): Uri? {
        return actionCamera.getPickImageResultUri(data)
    }

    // This method is used to crop Image
    fun cropImage(uri: Uri) {
        actionCamera.cropImage(uri)
    }

    // This method called at the time of onActivityResult called for crop image activity result
    fun cropImageActivityResult(requestCode: Int, resultCode: Int,data: Intent): Uri? {
        return actionCamera.cropImageActivityResult(requestCode, resultCode, data)
    }

    // This method is used to get bitmap from uri
    fun getBitmapFromUri(uri: Uri): Bitmap? {
        return actionCamera.getBitmapFromUri(uri)
    }

    // This method is used to get Uri from bitmap
    fun getUriFromBitmap(bitmap: Bitmap): Uri? {
        return actionCamera.getUriFromBitmap(bitmap)
    }

    // This method is used to get byte array from bitmap
    // compressQuality : Quality of Image (0-100)
    fun getByteArrayFromBitmap(bitmap: Bitmap, compressQuality : Int): ByteArray? {
        return actionCamera.getByteArrayFromBitmap(bitmap,compressQuality)
    }

    // This method is used to get bitmap from byte array
    fun getBitmapFromByteArray(byteArray: ByteArray): Bitmap? {
        return actionCamera.getBitmapFromByteArray(byteArray)
    }

    // This method is used to get Uri from byte array
    fun getUriFromByteArray(byteArray: ByteArray): Uri? {
        return actionCamera.getUriFromByteArray(byteArray)
    }

    // This method is used to get byte array from Uri and
    // compressQuality : Quality of Image (0-100)
    fun getByteArrayFromUri(uri: Uri, compressQuality: Int): ByteArray? {
        return actionCamera.getByteArrayFromUri(uri, compressQuality)
    }

    // This method is used to geet base 64 string from bitmap,
    // compressQuality : Quality of Image (0-100)
    fun getBase64StringFromBitmap(bitmap : Bitmap, compressQuality: Int): String? {
        return actionCamera.getBase64StringFromBitmap(bitmap, compressQuality)
    }

    // This method is used to get bitmap from base 64 string
    fun getBitmapFromBase64String(base64String : String): Bitmap? {
        return actionCamera.getBitmapFromBase64String(base64String)
    }

    // This method is used to get base 64 string from Uri,
    // compressQuality : Quality of Image (0-100)
    fun getBase64StringFromUri(uri: Uri, compressQuality: Int): String? {
        return actionCamera.getBase64StringFromUri(uri, compressQuality)
    }

    // This method is used to get Uri from base 64 string
    fun getUriFromBase64String(base64String: String): Uri? {
        return actionCamera.getUriFromBase64String(base64String)
    }

    // This method is used to get byte array from base 64 string
    fun getByteArrayFromBase64String(base64String: String): ByteArray? {
        return actionCamera.getByteArrayFromBase64String(base64String)
    }

    // This method is used to get base 64 string from byte array
    fun getBase64StringFromByteArray(byteArray: ByteArray): String? {
        return actionCamera.getBase64StringFromByteArray(byteArray)
    }

    fun savePhotoInDeviceMemory(bitmap: Bitmap, imagePrefix : String, autoConcatenateNameByDate : Boolean): String? {
        return actionCamera.savePhotoInDeviceMemory(bitmap,imagePrefix,autoConcatenateNameByDate)
    }

    fun savePhotoInDeviceMemory(bitmap: Bitmap, imagePrefix: String, format : Bitmap.CompressFormat, autoConcatenateNameByDate: Boolean): String? {
        return actionCamera.savePhotoInDeviceMemory(bitmap, imagePrefix, format,autoConcatenateNameByDate)
    }

    fun savePhotoInDeviceMemory(bitmap: Bitmap, imagePrefix: String, directoryName : String, autoConcatenateNameByDate: Boolean): String? {
        return actionCamera.savePhotoInDeviceMemory(bitmap, imagePrefix, directoryName,autoConcatenateNameByDate)
    }

    fun savePhotoInDeviceMemory(bitmap: Bitmap, imagePrefix: String, directoryName: String, format: Bitmap.CompressFormat, autoConcatenateNameByDate: Boolean): String? {
        return actionCamera.savePhotoInDeviceMemory(bitmap, imagePrefix, directoryName, format, autoConcatenateNameByDate )
    }

    fun savePhotoInDeviceMemory(uri: Uri, imagePrefix : String, autoConcatenateNameByDate : Boolean): String? {
        return actionCamera.savePhotoInDeviceMemory(uri,imagePrefix,autoConcatenateNameByDate)
    }

    fun savePhotoInDeviceMemory(uri: Uri, imagePrefix: String, format : Bitmap.CompressFormat, autoConcatenateNameByDate: Boolean): String? {
        return actionCamera.savePhotoInDeviceMemory(uri, imagePrefix,format, autoConcatenateNameByDate)
    }

    fun savePhotoInDeviceMemory(uri: Uri, imagePrefix: String, directoryName : String, autoConcatenateNameByDate: Boolean): String? {
        return actionCamera.savePhotoInDeviceMemory(uri, imagePrefix, directoryName,autoConcatenateNameByDate)
    }

    fun savePhotoInDeviceMemory(uri: Uri, imagePrefix: String, directoryName: String, format: Bitmap.CompressFormat, autoConcatenateNameByDate: Boolean): String? {
        return actionCamera.savePhotoInDeviceMemory(uri, imagePrefix, directoryName, format,autoConcatenateNameByDate)
    }

    // This method is used to rotate Image
    fun rotatePicture(bitmap: Bitmap, rotate: Int){
        actionCamera.rotatePicture(bitmap, rotate)
    }

    // This method is used to create scaled bitmap
    fun createScaledBitmap(bitmap: Bitmap, width : Int, height : Int, filter : Boolean): Bitmap? {
        return actionCamera.createScaledBitmap(bitmap, width, height, filter)
    }

    // This method is used to get the current Image name
    fun getCurrentBitmapName(): String? {
        return if (mCurrentImageName!=null) mCurrentImageName else null
    }


}