package net.rmitsolutions.mfexpert.lms.viewmodels

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey


@Entity(tableName = "Village_info")
class VillageInfoModel (var village: String, var description: String, var district: String, var openDate: String, var distance: String,var latitude: String,var longitude: String,var timeStamp: String,var imageByteArray: ByteArray?){
    @PrimaryKey(autoGenerate = true)
    var id:Long?=null

}


