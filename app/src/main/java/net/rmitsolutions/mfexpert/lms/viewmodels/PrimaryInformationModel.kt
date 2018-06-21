package net.rmitsolutions.mfexpert.lms.viewmodels

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "primary_info")
class PrimaryInformationModel ( var centerName: String, var description: String, var villageName: String, var openDate: String,
                               var distance: String,var paymentType: String,var category: String,@PrimaryKey(autoGenerate = true) var id: Long? = null){
    /*@PrimaryKey(autoGenerate = true)
    var id:Long?=null*/


}