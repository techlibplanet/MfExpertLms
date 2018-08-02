package net.rmitsolutions.mfexpert.lms.models

import com.google.gson.annotations.SerializedName

class AccessToken {

    @SerializedName("access_token")
    var accessToken: String = ""
    @SerializedName("token_type")
    var tokenType: String? = null
    @SerializedName("expires_in")
    var expiresIn: Int = 0
    @SerializedName("refresh_token")
    var refreshToken: String = ""
}