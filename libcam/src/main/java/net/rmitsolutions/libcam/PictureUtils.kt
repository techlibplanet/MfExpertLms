package net.rmitsolutions.libcam

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import java.io.File

internal class PictureUtils {

    /**
     * Rotate the bitmap if the image is in landscape camera
     * @param source
     * @param angle
     * @return
     */
    fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        val retVal: Bitmap
        val matrix = Matrix()
        matrix.postRotate(angle)
        retVal = Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
        return retVal
    }

    // This method is used to create scaled bitmap
    fun createScaledBitmap(bitmap: Bitmap, width: Int, height: Int, filter: Boolean): Bitmap? {
        return Bitmap.createScaledBitmap(bitmap, width, height, filter)
    }

    /**
     * This method resize the photo
     *
     * @param realImage    the bitmap of image
     * @param maxImageSize the max image size percentage
     * @param filter       the filter
     * @return a bitmap of the photo resize
     */
    fun resizePhoto(realImage: Bitmap, maxImageSize: Float,
                    filter: Boolean): Bitmap {
        val ratio = Math.min(
                maxImageSize / realImage.width,
                maxImageSize / realImage.height)
        val width = Math.round(ratio * realImage.width)
        val height = Math.round(ratio * realImage.height)

        val newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter)
        return newBitmap
    }

    // Decode bitmap using string path with required width and required height
    fun decodeBitmapFromPath(currentPath: String, reqWidth: Int, reqHeight: Int): Bitmap? {
        val bitmapFile = File(currentPath)
        return if (bitmapFile.exists()) {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(bitmapFile.absolutePath, options)
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
            options.inJustDecodeBounds = false
            BitmapFactory.decodeFile(bitmapFile.absolutePath, options)
        } else {
            null
        }
    }

    // Calculating sample size of bitmap with required width and height
    private fun calculateInSampleSize(
            options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        // Raw height and width of image
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }
}