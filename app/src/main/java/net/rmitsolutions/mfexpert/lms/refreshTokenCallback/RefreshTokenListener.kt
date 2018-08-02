package net.rmitsolutions.mfexpert.lms.refreshTokenCallback

interface RefreshTokenListener {

    fun onTokenRefreshed(response : String)
}