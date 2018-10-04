package net.rmitsolutions.mfexpert.lms.repayment.callback

import android.view.View


class TotalAmountCallback {

    private lateinit var listener : TotalAmountListener

    fun setListener(totalAmountListener: TotalAmountListener){
        this.listener = totalAmountListener
    }

    fun onTotalAmountChanged(view: View?){
        listener.onTotalAmountChanged(view)
    }
}