package net.rmitsolutions.mfexpert.lms.repayment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import net.rmitsolutions.mfexpert.lms.R

class RepaymentFragment : Fragment() {
    private var mText = ""
    private lateinit var editTextAmount : EditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.repayment_tab_layout, container, false)
//        (v.findViewById(R.id.textView) as TextView).text = mText
//        editTextAmount = v.findViewById(R.id.editTextAmount)
//        editTextAmount.setText(mText)
        return v
    }

    companion object {
        fun createInstance(txt: String): RepaymentFragment {
            val fragment = RepaymentFragment()
            fragment.mText = txt
            return fragment
        }
    }
}