package net.rmitsolutions.mfexpert.lms.scanner

import android.graphics.Bitmap

interface DocumentImageListener {

    fun onCaptureDocumentImage(requestCode : Int,bitmap : Bitmap)
}