package net.rmitsolutions.mfexpert.lms.viewmodels

import net.rmitsolutions.mfexpert.lms.models.CommonResult


class Loans {
    class Loan( val memberId:String, val name:String, val loanDetails:MutableList<LoanDetails>): CommonResult()
    class LoanDetails(val id:String, val name:String)

    class LoanUtilizationResponse(val isSuccess : Boolean, val message : String)
}