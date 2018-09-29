package net.rmitsolutions.mfexpert.lms.viewmodels

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Village_info")
class VillageInfoModel (var village: String, var description: String, var district: String, var openDate: String, var distance: String,var latitude: String,var longitude: String,var timeStamp: String,var imageByteArray: ByteArray?){
    @PrimaryKey(autoGenerate = true)
    var id:Long?=null

}


