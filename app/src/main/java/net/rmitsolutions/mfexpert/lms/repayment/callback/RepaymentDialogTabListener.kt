package net.rmitsolutions.mfexpert.lms.repayment.callback



interface RepaymentDialogTabListener {
    fun onComplete(result : String,  clientId: Long)
    fun onGettingTotalAmount(amount : Double)
}