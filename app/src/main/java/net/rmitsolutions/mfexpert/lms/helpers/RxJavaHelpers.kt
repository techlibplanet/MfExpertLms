package net.rmitsolutions.mfexpert.lms.helpers

import android.app.Activity
import android.content.Context
import android.util.Log
import com.example.mayank.libraries.androidkeystore.DeCryptor
import com.example.mayank.libraries.androidkeystore.EnCryptor
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import net.rmitsolutions.libcam.Constants.logD
import net.rmitsolutions.mfexpert.lms.Constants
import net.rmitsolutions.mfexpert.lms.network.IUser
import net.rmitsolutions.mfexpert.lms.network.TokenService
import net.rmitsolutions.mfexpert.lms.refreshTokenCallback.RefreshTokenCallback
import net.rmitsolutions.mfexpert.lms.refreshTokenCallback.RefreshTokenListener
import timber.log.Timber

/**
 * Created by Madhu on 24-Apr-2018.
 */

//private val tokenService: TokenService by lazy { TokenService() }
internal var encryptor: EnCryptor? = null
private var decryptor: DeCryptor? = null
private var refreshToken : String? = null



inline fun <T> Observable<T>.processRequest(context : Context, crossinline onNext: (result: T) -> Unit, crossinline onError: (message: String?) -> Unit): Disposable {
    return subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                    { result ->
                        onNext(result)
                    },
                    { err ->
                        val message = ProcessThrowable.getMessage(err)
                        Log.d("RxJavaHelper", "Message - $message")
//                        Timber.e(err)
//                        onError(message)

                        if (message == Constants.UNAUTHORIZED_CODE){
//                            val response = refreshToken(context)
//                            onError("Unauthorized - $response")
                            val refreshTokenCallback = RefreshTokenCallback()
                            refreshTokenCallback.setRefreshTokenListener(object : RefreshTokenListener{
                                override fun onTokenRefreshFinished(response: String) {
                                    if (response == Constants.TOKEN_REFRESH_SUCCESS){
                                        onError(Constants.TOKEN_REFRESH_SUCCESS)
                                    }else if (response == Constants.TOKEN_REFRESH_FAILED){
                                        onError(Constants.TOKEN_REFRESH_FAILED)
                                    }else {
                                        onError(message)
                                    }
                                }
                            })
                            refreshTokenCallback.startTokenRefresh(context, context.apiRefreshToken)
                        } else {
                            Timber.e(err)
                            onError(message)
                        }
                    }
            )
}


inline fun <T> Single<T>.processRequest(crossinline onSuccess: (result: T) -> Unit, crossinline onError: (message: String?) -> Unit): Disposable {
    return subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                    { result ->
                        onSuccess(result)
                    },
                    { err ->
                        val message = ProcessThrowable.getMessage(err)
                        Timber.e(err)
                        onError(message)
                    }
            )
}


fun refreshToken(context: Context): String?{
    var tokenStatus : String? = null
    val refreshTokenCallback = RefreshTokenCallback()
    refreshTokenCallback.setRefreshTokenListener(object : RefreshTokenListener{
        override fun onTokenRefreshFinished(response: String) {
            when (response) {
                Constants.TOKEN_REFRESH_SUCCESS -> {
                    logD("RxJavaHelper", "Token refresh success")
                    tokenStatus= Constants.TOKEN_REFRESH_SUCCESS}
                Constants.TOKEN_REFRESH_FAILED -> {
                    logD("RxJavaHelper", "Token refresh Failed")
                    tokenStatus= Constants.TOKEN_REFRESH_FAILED}
            }
        }
    })
    refreshTokenCallback.startTokenRefresh(context, context.apiRefreshToken)
    logD("RxJavaHelper", "Token Status - $tokenStatus")
    return tokenStatus
}

//fun refreshToken(context: Context): String?{
//    var tokenStatus : String?  =null
//    val tokenService = TokenService()
//    logD("RxJavaHelper","Old Refresh Token - ${context.apiRefreshToken}")
//    val compositeDisposable = CompositeDisposable()
//    compositeDisposable.add(tokenService.getService<IUser>().refreshToken(Constants.REFRESH_TOKEN_GRANT_TYPE, Constants.CLIENT_ID, Constants.CLIENT_SECRET,context.apiRefreshToken)
//            .processRequest(context, {token ->
//                Constants.accessToken = token
//
//                logD("RxJavaHelper","New Access token - ${token.accessToken} New Refresh Token - ${token.refreshToken}")
//                encryptor?.encryptText(Constants.TOKEN_OBJECT_KEY, JsonHelper.KtToJson(token))
//                Log.d("RefreshToken", "Token refresh successfully!")
//                tokenStatus = Constants.TOKEN_REFRESH_SUCCESS
//
////                hideProgress()
//            },{err ->
//                Log.d("RefreshToken", "Error = $err")
////                hideProgress()
//                tokenStatus = Constants.TOKEN_REFRESH_FAILED
//            }))
//
//    return tokenStatus
//}

inline fun <T> Single<T>.processRequest(crossinline onSuccess: (result: T) -> Unit): Disposable {
    return subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                    { result ->
                        onSuccess(result)
                    }
            )
}

fun <T> Single<T>.processRequest(): Disposable {
    return subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
}

inline fun <T> Flowable<T>.processRequest(crossinline onNext: (result: T) -> Unit, crossinline onError: (message: String?) -> Unit): Disposable {
    return subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                    { result ->
                        onNext(result)
                    },
                    { err ->
                        val message = ProcessThrowable.getMessage(err)
                        Timber.e(err)
                        onError(message)
                    }
            )
}

inline fun <T> Flowable<T>.processRequest(crossinline onNext: (result: T) -> Unit): Disposable {
    return subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                    { result ->
                        onNext(result)
                    }
            )
}

fun <T> Flowable<T>.processRequest(): Disposable {
    return subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
}