package net.rmitsolutions.mfexpert.lms.sample

import android.widget.CheckBox
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.repayment_recycler_row.view.*
import net.rmitsolutions.mfexpert.lms.R
import net.rmitsolutions.mfexpert.lms.databinding.RepaymentActivityBinding
import net.rmitsolutions.mfexpert.lms.repayment.RepaymentModel
import net.rmitsolutions.mfexpert.lms.repayment.callback.RepaymentDetailsCallback
import org.jetbrains.anko.find

class ClientHolder(val dataBinding : RepaymentActivityBinding) : RecyclerView.ViewHolder(dataBinding.root) {

    private lateinit var editTextRepaymentAmount : EditText
    private lateinit var prePaymentCheckBox : CheckBox

    fun bind(holder: ClientHolder, model : RepaymentModel) {
        dataBinding.repaymentModel = model
        editTextRepaymentAmount = holder.itemView.find(R.id.edit_text_repayment_amount)
        prePaymentCheckBox = holder.itemView.find(R.id.prepayment_checkbox)
        editTextRepaymentAmount.setText((dataBinding.repaymentModel!!.pastDue + dataBinding.repaymentModel!!.currentDue + dataBinding.repaymentModel!!.otherCharges).toString())
        dataBinding.executePendingBindings()

    }
}