package net.rmitsolutions.mfexpert.lms.database.entities

import android.arch.persistence.room.PrimaryKey

abstract class BaseEntity {
    @PrimaryKey(autoGenerate = true)
    var id:Long?=0;
}