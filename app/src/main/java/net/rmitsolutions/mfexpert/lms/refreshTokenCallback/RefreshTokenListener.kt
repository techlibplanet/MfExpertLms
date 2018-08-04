package net.rmitsolutions.mfexpert.lms.refreshTokenCallback

import android.content.Context

interface RefreshTokenListener {

    fun onTokenRefreshFinished(response : String)
}