package net.rmitsolutions.mfexpert.lms.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import net.rmitsolutions.mfexpert.lms.viewmodels.CenterAddressInformationModel
import net.rmitsolutions.mfexpert.lms.viewmodels.CenterPrimaryInformationModel

@Entity(tableName = "center_data")
class CenterDetailsEntity {
    @PrimaryKey
var id:Long?=0
    var primaryInformationModel: CenterPrimaryInformationModel?=null
    var addressInformationModel: CenterAddressInformationModel?=null
}