package net.rmitsolutions.mfexpert.lms.repayment.callback

import net.rmitsolutions.mfexpert.lms.databinding.DialogRepaymentBinding


class TotalAmountCallback {

    private lateinit var listener: TotalAmountListener

    fun setListener(totalAmountListener: TotalAmountListener) {
        this.listener = totalAmountListener
    }

    fun onTotalAmountChanged(binding: DialogRepaymentBinding?) {
        listener.onTotalAmountChanged(binding)
    }
}