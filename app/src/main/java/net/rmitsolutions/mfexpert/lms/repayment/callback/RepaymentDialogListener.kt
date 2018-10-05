package net.rmitsolutions.mfexpert.lms.repayment.callback


interface RepaymentDialogListener {
    fun onComplete(result : String,  clientId: Long)
    fun onGettingTotalAmount(amount : Double)
}