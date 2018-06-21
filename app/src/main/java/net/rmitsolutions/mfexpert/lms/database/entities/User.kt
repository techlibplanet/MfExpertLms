package net.rmitsolutions.mfexpert.lms.database.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "user_info")
class User {
    @PrimaryKey
    var id: Int = 0
}