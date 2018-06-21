package net.rmitsolutions.mfexpert.lms.auth

import android.app.Service
import android.content.Intent
import android.os.IBinder

class MfExpertAuthenticatorService : Service() {
    override fun onBind(intent: Intent?): IBinder {
        return MfExpertAccountAuthenticator(this).iBinder
    }
}