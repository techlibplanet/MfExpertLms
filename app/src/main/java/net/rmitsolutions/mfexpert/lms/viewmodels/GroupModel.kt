package net.rmitsolutions.mfexpert.lms.viewmodels

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
@Entity(tableName = "Group_info")
class GroupModel(var groupName: String, var description: String, var openDate: String, var center: String, var latitude: String, var longitude: String, var timeStamp: String, var imageByteArray: ByteArray?){
   @PrimaryKey(autoGenerate = true)
   var id:Long?=null
   }