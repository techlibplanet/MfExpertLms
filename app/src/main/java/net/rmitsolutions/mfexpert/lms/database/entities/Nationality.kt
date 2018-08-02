package net.rmitsolutions.mfexpert.lms.database.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "nationality")
class Nationality {

    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Long = 0

    @ColumnInfo(name = "name")
    var name: String? = null
}