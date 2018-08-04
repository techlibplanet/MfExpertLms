package net.rmitsolutions.mfexpert.lms.refreshTokenCallback

import android.app.Activity
import android.content.Context
import android.util.Log
import com.example.mayank.libraries.androidkeystore.EnCryptor
import io.reactivex.disposables.CompositeDisposable
import net.rmitsolutions.libcam.Constants.logD
import net.rmitsolutions.libcam.Constants.logE
import net.rmitsolutions.mfexpert.lms.Constants
import net.rmitsolutions.mfexpert.lms.helpers.*
import net.rmitsolutions.mfexpert.lms.network.IUser
import net.rmitsolutions.mfexpert.lms.network.TokenService

class RefreshTokenCallback {

    var listener: RefreshTokenListener? = null
    private var encryptor: EnCryptor? = null

    fun setRefreshTokenListener(refreshTokenListener: RefreshTokenListener) {
        listener = refreshTokenListener
    }

    fun startTokenRefresh(context: Context, refreshToken: String) {
        if (listener != null) {
            val tokenService = TokenService()
            val compositeDisposable = CompositeDisposable()
            compositeDisposable.add(tokenService.getService<IUser>().refreshToken(Constants.REFRESH_TOKEN_GRANT_TYPE, Constants.CLIENT_ID, Constants.CLIENT_SECRET, refreshToken)
                    .processRequest(context, { token ->
                        Constants.accessToken = token
                        encryptor?.encryptText(Constants.TOKEN_OBJECT_KEY, JsonHelper.KtToJson(token))
                        onTokenRefreshed(Constants.TOKEN_REFRESH_SUCCESS)
                    }, { err ->
                        onTokenRefreshed(Constants.TOKEN_REFRESH_FAILED)
                    }))
        } else {
            logE("RefreshTokenCallback", "Listener is null")
        }

    }

    private fun onTokenRefreshed(response: String) {
        if (listener != null) {
            listener?.onTokenRefreshFinished(response)
        }
    }
}