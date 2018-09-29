package net.rmitsolutions.mfexpert.lms.viewmodels

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "LoanUtilizationCheck_info")
class LoanUtilizationCheckModel (var memberCode: String, var loanDetails: String,
                                  var utilizationStatus: String, var loanUtilizedForSamePurpose: String,
                                 var aveargeMonthlyIncome: String,var remarks: String)
                                {
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
}