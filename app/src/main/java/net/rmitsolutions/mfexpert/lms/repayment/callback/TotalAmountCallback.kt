package net.rmitsolutions.mfexpert.lms.repayment.callback


class TotalAmountCallback {

    private lateinit var listener : TotalAmountListener

    fun setListener(totalAmountListener: TotalAmountListener){
        this.listener = totalAmountListener
    }

    fun onTotalAmountChanged(){
        listener.onTotalAmountChanged()
    }
}