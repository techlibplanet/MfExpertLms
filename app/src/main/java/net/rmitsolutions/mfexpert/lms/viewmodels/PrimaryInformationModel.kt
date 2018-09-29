package net.rmitsolutions.mfexpert.lms.viewmodels

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "primary_info")
class PrimaryInformationModel ( var centerName: String, var description: String, var villageName: String, var openDate: String,
                               var distance: String,var paymentType: String,var category: String,@PrimaryKey(autoGenerate = true) var id: Long? = null){
    /*@PrimaryKey(autoGenerate = true)
    var id:Long?=null*/


}