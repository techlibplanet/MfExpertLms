package net.rmitsolutions.mfexpert.lms.helpers

import android.content.Context
import android.util.Log
import com.example.mayank.libraries.androidkeystore.DeCryptor
import com.example.mayank.libraries.androidkeystore.EnCryptor
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import net.rmitsolutions.libcam.Constants.logD
import net.rmitsolutions.mfexpert.lms.Constants
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
                        Timber.e(err)
                        onError(message)

                        /*if (message == "401"){
                            refreshToken(context)
                            onError(Constants.TOKEN_REFRESH_SUCCESS)
                            return@subscribe
                        }
                        Timber.e(err)
                        onError(message)*/


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

fun refreshToken(context: Context){
    val refreshTokenCallback =RefreshTokenCallback()
    refreshTokenCallback.startTokenRefresh(context, context.apiRefreshToken)
    refreshTokenCallback.setRefreshTokenListener(object : RefreshTokenListener{
        override fun onTokenRefreshed(response: String) {
            if (response == Constants.TOKEN_REFRESH_SUCCESS){
                logD("RxJavaHelper", "Token Refreshed Successfully !")
            }else {
                logD("RxJavaHelper", "Token Refresh Failed !")
            }
        }

    })
}

//fun refreshToken(context: Context){
//    val tokenService = TokenService()
//    encryptor = EnCryptor(context)
//    val encryptedTokenKey = Base64.decode(context.getPref(SharedPrefKeys.SP_ENCRYPTED_TOKEN_KEY, ""), Base64.DEFAULT)
//    val encryptedIv = Base64.decode(context.getPref(SharedPrefKeys.SP_ENCRYPTED_IV, ""), Base64.DEFAULT)
//    try {
//        decryptor = DeCryptor()
//    } catch (e: CertificateException) {
//        e.printStackTrace()
//    } catch (e: NoSuchAlgorithmException) {
//        e.printStackTrace()
//    } catch (e: KeyStoreException) {
//        e.printStackTrace()
//    } catch (e: IOException) {
//        e.printStackTrace()
//    }
//
//    val userAccessToken = decryptor?.decryptData(Constants.TOKEN_OBJECT_KEY, encryptedTokenKey, encryptedIv)
//    if (userAccessToken!=null && !userAccessToken.isEmpty()){
//        refreshToken = JsonHelper.jsonToKt<AccessToken>(userAccessToken).refreshToken
//    }
//    logD("RxJavaHelper","Old Refresh Token - $refreshToken")
//    val compositeDisposable = CompositeDisposable()
//    compositeDisposable.add(tokenService.getService<IUser>().refreshToken(Constants.REFRESH_TOKEN_GRANT_TYPE, Constants.CLIENT_ID, Constants.CLIENT_SECRET,refreshToken!!)
//            .processRequest(context, {token ->
//                Constants.accessToken = token
//
//                logD("RxJavaHelper","New Access token - ${token.accessToken} New Refresh Token - ${token.refreshToken}")
//                encryptor?.encryptText(Constants.TOKEN_OBJECT_KEY, JsonHelper.KtToJson(token))
//                Log.d("RefreshToken", "Token refresh successfully!")
//                return@processRequest
////                hideProgress()
//            },{err ->
//                Log.d("RefreshToken", "Error = $err")
////                hideProgress()
//                return@processRequest
//            }))
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