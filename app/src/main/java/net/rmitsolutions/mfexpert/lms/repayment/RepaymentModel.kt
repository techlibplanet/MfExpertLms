package net.rmitsolutions.mfexpert.lms.repayment

class RepaymentModel(val id: Long, val memberCode: String, val memberName: String, val productName: String, val productId: Long,
                     val pastDue: Double, val currentDue: Double, val otherCharges: Double, val paidAmount: Double)