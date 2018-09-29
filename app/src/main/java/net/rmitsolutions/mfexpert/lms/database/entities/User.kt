package net.rmitsolutions.mfexpert.lms.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_info")
class User {
    @PrimaryKey
    var id: Int = 0
}