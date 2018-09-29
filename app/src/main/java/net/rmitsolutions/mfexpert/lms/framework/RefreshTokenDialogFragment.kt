package net.rmitsolutions.mfexpert.lms.framework

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialogFragment
import android.widget.Button
import net.rmitsolutions.mfexpert.lms.R


/**
 * Created by Mayank on 06/11/2017.
 */


class RefreshTokenDialogFragment : AppCompatDialogFragment() {
    private val TAG = RefreshTokenDialogFragment::class.java.simpleName
    internal var alertDialog: AlertDialog? = null
    internal lateinit var builder: AlertDialog.Builder
    internal lateinit var positiveButton: Button
    internal lateinit var negativeButton: Button

    override fun onStart() {
        super.onStart()
        if (alertDialog != null) {
            positiveButton = alertDialog!!.getButton(Dialog.BUTTON_POSITIVE)
            negativeButton = alertDialog!!.getButton(Dialog.BUTTON_NEGATIVE)
            negativeButton.isEnabled = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        builder = AlertDialog.Builder(activity, R.style.AlertDialogTheme)
        builder.setTitle("")
        builder.setMessage("Session Expired ! Again press submit to process the transaction.")
        builder.setPositiveButton("Ok") { dialog, which ->
            dismiss()
        }

//        builder.setNegativeButton("Ok") { dialog, which ->
//            dismiss() }

        alertDialog = builder.create()
        alertDialog!!.setCanceledOnTouchOutside(false)
        return alertDialog as AlertDialog
    }



    companion object {
        internal lateinit var mError: Error

        fun newInstance(error: Error): RefreshTokenDialogFragment {
            mError = error
            return RefreshTokenDialogFragment()
        }
    }
}