package net.rmitsolutions.mfexpert.lms.scanner

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore
import androidx.fragment.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.scanlibrary.ScanActivity
import com.scanlibrary.ScanConstants
import net.rmitsolutions.mfexpert.lms.Constants
import net.rmitsolutions.mfexpert.lms.R
import net.rmitsolutions.mfexpert.lms.cbm.IdentificationInfoFragment
import java.io.IOException

class ChooseIntentDialog : DialogFragment() {

    private lateinit var alertDialog: AlertDialog
    val OPEN_CAMERA = ScanConstants.OPEN_CAMERA
    val OPEN_MEDIA = ScanConstants.OPEN_MEDIA
    private val REQUEST_CODE = 99
    private lateinit var dialogInterface: DialogInterface
    private var listener: DocumentImageListener? = null
    private var identificationInfoFragment: IdentificationInfoFragment? = null

    fun setListener(identificationInfoFragment: IdentificationInfoFragment) {
        this.identificationInfoFragment = identificationInfoFragment
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        listener = identificationInfoFragment as? DocumentImageListener
        alertDialog = AlertDialog.Builder(activity!!).setTitle("Choose one")
                .setPositiveButton("Ok", null)
                .setNegativeButton("Cancel", null).create()
        alertDialog.setOnShowListener { dialogInterface ->
            this.dialogInterface = dialogInterface
            val okButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            val cancelButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)

            okButton.setOnClickListener {
                dialogInterface.dismiss()
            }

            cancelButton.setOnClickListener {
                dialogInterface.dismiss()
            }
        }
        return alertDialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.choose_intent_layout, container, false)
        val buttonCamera = view.findViewById<Button>(R.id.buttonCamera)
        val buttonGallery = view.findViewById<Button>(R.id.buttonGallery)
        buttonCamera.setOnClickListener {
            val intent = Intent(activity, ScanActivity::class.java)
            intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, OPEN_CAMERA)
            startActivityForResult(intent, REQUEST_CODE)
        }
        buttonGallery.setOnClickListener {
            val intent = Intent(activity, ScanActivity::class.java)
            intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, OPEN_MEDIA)
            startActivityForResult(intent, REQUEST_CODE)
        }
        alertDialog.setView(view)
        return view
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val uri = data?.extras?.getParcelable(ScanConstants.SCANNED_RESULT) as Parcelable
            var bitmap: Bitmap? = null
            try {
                bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, uri as Uri?)
                activity?.contentResolver?.delete(uri, null, null)
                if (bitmap != null) {
                    val reqCode = arguments?.getInt(Constants.SCAN_DOCUMENT)
                    listener?.onCaptureDocumentImage(reqCode!!, bitmap)
                } else {
                    Toast.makeText(activity, "Error - Bitmap is null", Toast.LENGTH_SHORT).show()
                }
                dialogInterface.dismiss()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}