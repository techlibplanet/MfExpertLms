package net.rmitsolutions.mfexpert.lms.repayment.callback


import net.rmitsolutions.mfexpert.lms.viewmodels.Repayment

interface RepaymentDetailsListener {
    fun onSuccess(memberLoanDetails : Repayment.MemberLoanDetails, id : Long)
}