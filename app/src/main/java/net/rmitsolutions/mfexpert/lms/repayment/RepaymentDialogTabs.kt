package net.rmitsolutions.mfexpert.lms.repayment

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.DialogFragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import net.rmitsolutions.mfexpert.lms.R
import net.rmitsolutions.mfexpert.lms.repayment.adapter.RepaymentPagerAdapter

class RepaymentDialogTabs : DialogFragment() {
    private lateinit var alertDialog: AlertDialog
    private lateinit var repaymentPagerAdapter: RepaymentPagerAdapter

    // Initialize listener
    private var mListener: OnCompleteListener? = null

    // Creating Interface
    interface OnCompleteListener {
        fun onComplete(textAmount: String)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repaymentPagerAdapter = RepaymentPagerAdapter(childFragmentManager)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        alertDialog = AlertDialog.Builder(activity!!)
                .setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, which ->
//                    val editTextAmount = view?.findViewById<EditText>(R.id.editTextAmount)
                    // Pass the value to the listener to change the text in activity
//                    mListener?.onComplete(editTextAmount?.text.toString())
                    Toast.makeText(activity, "Okay Button clicked", Toast.LENGTH_SHORT).show()
                }).setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
                    Toast.makeText(activity, "Cancel button clicked", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }).create()
        return alertDialog
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
//        try {
//            // Registering listener
//            this.mListener = activity as OnCompleteListener
//        } catch (e: ClassCastException) {
//            throw ClassCastException(activity.toString() + " must implement OnCompleteListener")
//        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_repayment, container, false)
        val tabLayout = view.findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = view.findViewById<ViewPager>(R.id.masterViewPager)
        repaymentPagerAdapter.addFragment("Loan 1", RepaymentFragment.createInstance("Loan detail 1"));
        repaymentPagerAdapter.addFragment("Loan 2", RepaymentFragment.createInstance("Loan detail 2"));
        repaymentPagerAdapter.addFragment("Loan 3", RepaymentFragment.createInstance("Loan detail 3"));
        repaymentPagerAdapter.addFragment("Loan 4", RepaymentFragment.createInstance("Loan detail 4"));
        viewPager.adapter = repaymentPagerAdapter;
        tabLayout.setupWithViewPager(viewPager);
        alertDialog.setView(view)
        return view;
    }
}