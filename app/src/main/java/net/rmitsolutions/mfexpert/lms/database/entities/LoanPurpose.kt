package net.rmitsolutions.mfexpert.lms.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "loan_purpose")
class LoanPurpose {
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Long = 0

    @ColumnInfo(name = "name")
    var name: String? = null
}