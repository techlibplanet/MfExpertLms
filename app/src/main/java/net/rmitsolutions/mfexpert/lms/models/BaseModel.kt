package net.rmitsolutions.mfexpert.lms.models

import androidx.room.Ignore
import com.google.gson.annotations.SerializedName

/**
 * Created by Madhu on 13-Jun-2017.
 */

open class BaseModel {
    @Ignore
    @SerializedName("isSuccess")
    var isSuccess: Boolean = false
    @Ignore
    @SerializedName("message")
    var message: String = ""
}