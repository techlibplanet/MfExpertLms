package net.rmitsolutions.libcam

import android.media.ExifInterface
import net.rmitsolutions.libcam.Constants.notNullNotFill
import java.io.IOException

internal class ImageInformation {

    val imageInformationObject: ImageInformationObject


    init {
        imageInformationObject = ImageInformationObject()
    }


    private fun getAllFeatures(realPath: String): ExifInterface? {
        if (realPath != "") {
            var exif: ExifInterface? = null
            try {
                exif = ExifInterface(realPath)
                return exif
            } catch (e: IOException) {
                return exif
            }

        } else {
            return null
        }
    }

    fun getImageInformation(realPath: String): ImageInformationObject? {
        try {
            val exif = getAllFeatures(realPath)
            if (exif != null) {

//                val latLong = FloatArray(2)
//                try {
//                    exif.getLatLong(latLong)
//                    imageInformationObject.latitude = latLong[0]
//                    imageInformationObject.longitude =latLong[1]
//                } catch (ex: Exception) {
//                }

                if (notNullNotFill(exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE))){
                   imageInformationObject.latitude =  exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE)
                }

                if (notNullNotFill(exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE))){
                    imageInformationObject.longitude = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE)
                }

                if (notNullNotFill(exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF))) {
                    imageInformationObject.latitudeReference = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF)
                }

                if (notNullNotFill(exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF))) {
                    imageInformationObject.longitudeReference= exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF)
                }

                if (notNullNotFill(exif.getAttribute(ExifInterface.TAG_DATETIME))) {
                    imageInformationObject.dateTimeTakePhoto = exif.getAttribute(ExifInterface.TAG_DATETIME)
                }

                if (notNullNotFill(exif.getAttribute(ExifInterface.TAG_ORIENTATION))) {
                    imageInformationObject.orientation = exif.getAttribute(ExifInterface.TAG_ORIENTATION)
                }

                if (notNullNotFill(exif.getAttribute(ExifInterface.TAG_ISO))) {
                    imageInformationObject.iso = exif.getAttribute(ExifInterface.TAG_ISO)
                }

                if (notNullNotFill(exif.getAttribute(ExifInterface.TAG_GPS_DATESTAMP))) {
                    imageInformationObject.dateStamp = exif.getAttribute(ExifInterface.TAG_GPS_DATESTAMP)
                }

                if (notNullNotFill(exif.getAttribute(ExifInterface.TAG_IMAGE_LENGTH))) {
                    imageInformationObject.imageLength = exif.getAttribute(ExifInterface.TAG_IMAGE_LENGTH)
                }

                if (notNullNotFill(exif.getAttribute(ExifInterface.TAG_IMAGE_WIDTH))) {
                    imageInformationObject.imageWidth = exif.getAttribute(ExifInterface.TAG_IMAGE_WIDTH)
                }

                if (notNullNotFill(exif.getAttribute(ExifInterface.TAG_MODEL))) {
                    imageInformationObject.modelDevice = exif.getAttribute(ExifInterface.TAG_MODEL)
                }

                if (notNullNotFill(exif.getAttribute(ExifInterface.TAG_MAKE))) {
                    imageInformationObject.makeCompany = exif.getAttribute(ExifInterface.TAG_MAKE)
                }
                return imageInformationObject
            } else {
                return null
            }
        } catch (ex: Exception) {
            return null
        }

    }
}